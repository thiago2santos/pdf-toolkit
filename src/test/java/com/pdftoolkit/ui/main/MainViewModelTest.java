package com.pdftoolkit.ui.main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pdftoolkit.core.PageInfo;
import com.pdftoolkit.core.PdfFileService;
import com.pdftoolkit.ui.BackgroundRunner;
import com.pdftoolkit.ui.UserNotifier;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainViewModelTest {

  @TempDir Path dir;

  private final RecordingNotifier notifier = new RecordingNotifier();
  private final MainViewModel viewModel =
      new MainViewModel(new PdfFileService(), new SyncRunner(), new SyncRunner(), notifier);

  @AfterEach
  void tearDown() {
    viewModel.dispose();
  }

  @Test
  void initialStateHasNothingOpen() {
    assertEquals(MainViewModel.NO_FILE, viewModel.fileTextProperty().get());
    assertEquals("", viewModel.pagesTextProperty().get());
    assertFalse(viewModel.canSaveProperty().get());
    assertEquals(Optional.empty(), viewModel.suggestedSaveName());
  }

  @Test
  void openUpdatesFileAndPages() throws IOException {
    Path pdf = createPdf(dir.resolve("report.pdf"), 4);

    viewModel.open(pdf);

    assertEquals("Arquivo: report.pdf", viewModel.fileTextProperty().get());
    assertEquals("Páginas: 4", viewModel.pagesTextProperty().get());
    assertTrue(viewModel.canSaveProperty().get());
    assertFalse(viewModel.busyProperty().get());
    assertEquals(Optional.of("report-copia.pdf"), viewModel.suggestedSaveName());
    assertTrue(notifier.errors.isEmpty());
  }

  @Test
  void openListsPagesAndRendersOnDemand() throws IOException {
    viewModel.open(createPdf(dir.resolve("doc.pdf"), 3));
    List<BufferedImage> rendered = new ArrayList<>();

    assertEquals(3, viewModel.getPages().size());
    PageInfo second = viewModel.getPages().get(1);
    viewModel.renderPage(second, () -> true, rendered::add, () -> {});
    viewModel.renderPage(second, () -> false, rendered::add, () -> {});

    assertEquals(1, rendered.size());
    assertTrue(rendered.get(0).getWidth() > 0);
  }

  @Test
  void openingAnotherPdfReplacesPages() throws IOException {
    viewModel.open(createPdf(dir.resolve("a.pdf"), 3));
    viewModel.open(createPdf(dir.resolve("b.pdf"), 1));

    assertEquals(1, viewModel.getPages().size());
    assertEquals("Arquivo: b.pdf", viewModel.fileTextProperty().get());
  }

  @Test
  void openInvalidFileNotifiesErrorAndKeepsState() throws IOException {
    Path fake = Files.writeString(dir.resolve("fake.pdf"), "nope");

    viewModel.open(fake);

    assertEquals(1, notifier.errors.size());
    assertTrue(notifier.errors.get(0).contains("fake.pdf"));
    assertEquals(MainViewModel.NO_FILE, viewModel.fileTextProperty().get());
    assertFalse(viewModel.busyProperty().get());
  }

  @Test
  void saveAsAddsExtensionAndNotifiesSuccess() throws IOException {
    viewModel.open(createPdf(dir.resolve("in.pdf"), 2));

    viewModel.saveAs(dir.resolve("out"));

    assertTrue(Files.isRegularFile(dir.resolve("out.pdf")));
    assertEquals(1, notifier.infos.size());
    assertTrue(notifier.errors.isEmpty());
  }

  @Test
  void copyNameHandlesExtensionCase() {
    assertEquals("a-copia.pdf", MainViewModel.copyName("a.PDF"));
    assertEquals("a-copia.pdf", MainViewModel.copyName("a"));
  }

  private static Path createPdf(Path path, int pages) throws IOException {
    try (PDDocument document = new PDDocument()) {
      for (int i = 0; i < pages; i++) {
        document.addPage(new PDPage());
      }
      document.save(path.toFile());
    }
    return path;
  }

  private static final class SyncRunner implements BackgroundRunner {
    @Override
    public <T> void run(Callable<T> work, Consumer<T> onSuccess, Consumer<Throwable> onError) {
      T result;
      try {
        result = work.call();
      } catch (Exception e) {
        onError.accept(e);
        return;
      }
      onSuccess.accept(result);
    }
  }

  private static final class RecordingNotifier implements UserNotifier {
    final List<String> infos = new ArrayList<>();
    final List<String> errors = new ArrayList<>();

    @Override
    public void info(String header, String content) {
      infos.add(content);
    }

    @Override
    public void error(String header, String content) {
      errors.add(content);
    }
  }
}
