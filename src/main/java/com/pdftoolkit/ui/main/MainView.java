package com.pdftoolkit.ui.main;

import com.pdftoolkit.core.PageInfo;
import com.pdftoolkit.ui.UserNotifier;
import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/** Janela principal: só layout, bindings e diálogos. Estado fica no {@link MainViewModel}. */
public class MainView implements UserNotifier {

  private static final FileChooser.ExtensionFilter PDF_FILTER =
      new FileChooser.ExtensionFilter("PDF", "*.pdf", "*.PDF");
  private static final int IMAGE_CACHE_SIZE = 12;

  private final String appName;
  private final BorderPane root = new BorderPane();
  private final Map<PageInfo, Image> imageCache =
      new LinkedHashMap<>(16, 0.75f, true) {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<PageInfo, Image> eldest) {
          return size() > IMAGE_CACHE_SIZE;
        }
      };
  private MainViewModel viewModel;

  /**
   * Cria a view. Chame {@link #bind(MainViewModel)} antes de exibir.
   *
   * @param appName nome exibido em títulos
   */
  public MainView(String appName) {
    this.appName = appName;
  }

  /**
   * Liga a view ao ViewModel e monta o layout.
   *
   * @param model ViewModel da janela
   */
  public void bind(MainViewModel model) {
    this.viewModel = model;

    Label fileLabel = new Label();
    fileLabel.textProperty().bind(model.fileTextProperty());
    Label pagesLabel = new Label();
    pagesLabel.textProperty().bind(model.pagesTextProperty());
    HBox header = new HBox(24, fileLabel, pagesLabel);
    header.setPadding(new Insets(8, 16, 8, 16));

    Label statusLabel = new Label();
    statusLabel.textProperty().bind(model.statusProperty());
    statusLabel.setPadding(new Insets(4, 16, 8, 16));

    root.setTop(new VBox(createMenuBar(), header));
    root.setCenter(createPageList(model));
    root.setBottom(statusLabel);
    root.disableProperty().bind(model.busyProperty());
  }

  /**
   * Nó raiz para montar a {@code Scene}.
   *
   * @return nó raiz
   */
  public Parent getRoot() {
    return root;
  }

  @Override
  public void info(String header, String content) {
    showAlert(Alert.AlertType.INFORMATION, header, content);
  }

  @Override
  public void error(String header, String content) {
    showAlert(Alert.AlertType.ERROR, header, content);
  }

  private ListView<PageInfo> createPageList(MainViewModel model) {
    ListView<PageInfo> list = new ListView<>(model.getPages());
    list.setPlaceholder(new Label(MainViewModel.NO_FILE));
    list.setFocusTraversable(false);
    list.setStyle("-fx-background-color: #e8e8e8; -fx-control-inner-background: #e8e8e8;");
    list.setCellFactory(view -> new PageCell(model, imageCache));
    model
        .getPages()
        .addListener(
            (ListChangeListener<PageInfo>)
                change -> {
                  imageCache.clear();
                  list.scrollTo(0);
                });
    return list;
  }

  private MenuBar createMenuBar() {
    MenuItem openItem = new MenuItem("Abrir…");
    openItem.setAccelerator(KeyCombination.keyCombination("Shortcut+O"));
    openItem.setOnAction(event -> chooseAndOpen());

    MenuItem saveAsItem = new MenuItem("Salvar como…");
    saveAsItem.setAccelerator(KeyCombination.keyCombination("Shortcut+Shift+S"));
    saveAsItem.disableProperty().bind(viewModel.canSaveProperty().not());
    saveAsItem.setOnAction(event -> chooseAndSave());

    MenuItem exitItem = new MenuItem("Sair");
    exitItem.setOnAction(event -> Platform.exit());

    Menu fileMenu = new Menu("Arquivo");
    fileMenu.getItems().addAll(openItem, saveAsItem, new SeparatorMenuItem(), exitItem);

    MenuItem aboutItem = new MenuItem("Sobre");
    aboutItem.setOnAction(
        event -> info(appName, "Toolkit de PDF local-first.\nJavaFX + Apache PDFBox."));

    Menu helpMenu = new Menu("Ajuda");
    helpMenu.getItems().add(aboutItem);

    return new MenuBar(fileMenu, helpMenu);
  }

  private void chooseAndOpen() {
    FileChooser chooser = pdfChooser("Abrir PDF");
    File file = chooser.showOpenDialog(window());
    if (file != null) {
      viewModel.open(file.toPath());
    }
  }

  private void chooseAndSave() {
    FileChooser chooser = pdfChooser("Salvar PDF como");
    viewModel.suggestedSaveName().ifPresent(chooser::setInitialFileName);
    File file = chooser.showSaveDialog(window());
    if (file != null) {
      viewModel.saveAs(file.toPath());
    }
  }

  private FileChooser pdfChooser(String title) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle(title);
    chooser.getExtensionFilters().add(PDF_FILTER);
    viewModel
        .currentDirectory()
        .map(Path::toFile)
        .filter(File::isDirectory)
        .ifPresent(chooser::setInitialDirectory);
    return chooser;
  }

  private void showAlert(Alert.AlertType type, String header, String content) {
    Alert alert = new Alert(type);
    alert.initOwner(window());
    alert.setTitle(appName);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private Window window() {
    return root.getScene() == null ? null : root.getScene().getWindow();
  }
}
