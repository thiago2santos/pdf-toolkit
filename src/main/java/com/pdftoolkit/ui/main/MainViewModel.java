package com.pdftoolkit.ui.main;

import com.pdftoolkit.core.PageInfo;
import com.pdftoolkit.core.PdfFileService;
import com.pdftoolkit.core.PdfInfo;
import com.pdftoolkit.core.PdfOperationException;
import com.pdftoolkit.core.PdfPreview;
import com.pdftoolkit.ui.BackgroundRunner;
import com.pdftoolkit.ui.UserNotifier;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Estado e ações da janela principal (F1 Abrir + visualizar, F7 Salvar). */
public class MainViewModel {

  static final String NO_FILE = "Nenhum PDF aberto. Use Arquivo → Abrir.";
  static final String ERROR_HEADER = "Não foi possível concluir a operação.";

  /** Escala de renderização (1.0 = 72 DPI); acima de 1 para ficar nítido na tela. */
  public static final float RENDER_SCALE = 1.5f;

  private final PdfFileService fileService;
  private final BackgroundRunner ioRunner;
  private final BackgroundRunner renderRunner;
  private final UserNotifier notifier;

  private final ObjectProperty<PdfInfo> current = new SimpleObjectProperty<>();
  private final ObservableList<PageInfo> pages = FXCollections.observableArrayList();
  private final ReadOnlyBooleanWrapper busy = new ReadOnlyBooleanWrapper();
  private final ReadOnlyStringWrapper status = new ReadOnlyStringWrapper("");
  private final ObservableStringValue fileText =
      Bindings.createStringBinding(
          () -> current.get() == null ? NO_FILE : "Arquivo: " + current.get().fileName(), current);
  private final ObservableStringValue pagesText =
      Bindings.createStringBinding(
          () -> current.get() == null ? "" : "Páginas: " + current.get().pageCount(), current);
  private final BooleanBinding canSave = current.isNotNull().and(busy.not());

  private PdfPreview preview;

  /**
   * Cria o ViewModel.
   *
   * @param fileService serviço de abrir/salvar
   * @param ioRunner executor para abrir/salvar
   * @param renderRunner executor para renderizar páginas
   * @param notifier canal de mensagens ao usuário
   */
  public MainViewModel(
      PdfFileService fileService,
      BackgroundRunner ioRunner,
      BackgroundRunner renderRunner,
      UserNotifier notifier) {
    this.fileService = fileService;
    this.ioRunner = ioRunner;
    this.renderRunner = renderRunner;
    this.notifier = notifier;
  }

  /**
   * Abre o PDF em background e prepara a visualização (F1). Fecha o documento anterior.
   *
   * @param path arquivo escolhido
   */
  public void open(Path path) {
    execute(
        "Abrindo " + path.getFileName() + "…",
        () -> fileService.openPreview(path),
        opened -> {
          dispose();
          preview = opened;
          current.set(opened.info());
          pages.setAll(opened.pages());
          status.set("PDF aberto: " + opened.info().path());
        });
  }

  /**
   * Renderiza uma página em background. Se {@code stillNeeded} for falso quando a vez da página
   * chegar, a renderização é pulada e {@code onReady} não é chamado.
   *
   * @param page página a renderizar
   * @param stillNeeded consultado na thread de trabalho antes de renderizar
   * @param onReady recebe a imagem na thread da UI
   * @param onFailure chamado na thread da UI se a renderização falhar
   */
  public void renderPage(
      PageInfo page,
      BooleanSupplier stillNeeded,
      Consumer<BufferedImage> onReady,
      Runnable onFailure) {
    PdfPreview target = preview;
    if (target == null) {
      return;
    }
    renderRunner.run(
        () -> stillNeeded.getAsBoolean() ? target.render(page.number(), RENDER_SCALE) : null,
        image -> {
          if (image != null && target == preview) {
            onReady.accept(image);
          }
        },
        error -> {
          if (target == preview) {
            onFailure.run();
          }
        });
  }

  /**
   * Salva uma cópia do PDF aberto (F7). Adiciona {@code .pdf} se faltar.
   *
   * @param target destino escolhido
   */
  public void saveAs(Path target) {
    PdfInfo source = current.get();
    if (source == null) {
      return;
    }
    Path destination = withPdfExtension(target);
    execute(
        "Salvando " + destination.getFileName() + "…",
        () -> fileService.saveAs(source.path(), destination),
        saved -> {
          status.set("PDF salvo: " + saved.path());
          notifier.info("PDF salvo com sucesso.", saved.path().toString());
        });
  }

  /** Fecha o documento aberto, se houver. Chamar ao sair da aplicação. */
  public final void dispose() {
    if (preview != null) {
      preview.close();
      preview = null;
    }
  }

  /**
   * Nome sugerido no diálogo de salvar.
   *
   * @return nome sugerido, vazio se nada aberto
   */
  public Optional<String> suggestedSaveName() {
    return Optional.ofNullable(current.get()).map(info -> copyName(info.fileName()));
  }

  /**
   * Diretório do PDF aberto, para iniciar diálogos.
   *
   * @return diretório, vazio se nada aberto
   */
  public Optional<Path> currentDirectory() {
    return Optional.ofNullable(current.get()).map(info -> info.path().getParent());
  }

  private <T> void execute(String message, Callable<T> work, Consumer<T> done) {
    busy.set(true);
    status.set(message);
    ioRunner.run(
        work,
        result -> {
          busy.set(false);
          done.accept(result);
        },
        error -> {
          busy.set(false);
          status.set("");
          notifier.error(ERROR_HEADER, describe(error));
        });
  }

  static String describe(Throwable error) {
    return error instanceof PdfOperationException
        ? error.getMessage()
        : "Erro inesperado: " + error.getMessage();
  }

  static String copyName(String fileName) {
    String lower = fileName.toLowerCase(Locale.ROOT);
    String base = lower.endsWith(".pdf") ? fileName.substring(0, fileName.length() - 4) : fileName;
    return base + "-copia.pdf";
  }

  static Path withPdfExtension(Path path) {
    String name = path.getFileName().toString();
    return name.toLowerCase(Locale.ROOT).endsWith(".pdf")
        ? path
        : path.resolveSibling(name + ".pdf");
  }

  public ReadOnlyObjectProperty<PdfInfo> currentProperty() {
    return current;
  }

  public ObservableList<PageInfo> getPages() {
    return FXCollections.unmodifiableObservableList(pages);
  }

  public ReadOnlyBooleanProperty busyProperty() {
    return busy.getReadOnlyProperty();
  }

  public ReadOnlyStringProperty statusProperty() {
    return status.getReadOnlyProperty();
  }

  public ObservableStringValue fileTextProperty() {
    return fileText;
  }

  public ObservableStringValue pagesTextProperty() {
    return pagesText;
  }

  public BooleanBinding canSaveProperty() {
    return canSave;
  }
}
