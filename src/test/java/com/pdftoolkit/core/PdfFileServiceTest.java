package com.pdftoolkit.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PdfFileServiceTest {

  @TempDir Path dir;

  private final PdfFileService service = new PdfFileService();

  static Path createPdf(Path path, int pages) throws IOException {
    try (PDDocument document = new PDDocument()) {
      for (int i = 0; i < pages; i++) {
        document.addPage(new PDPage());
      }
      document.save(path.toFile());
    }
    return path;
  }

  @Test
  void openReturnsFileNameAndPageCount() throws Exception {
    Path pdf = createPdf(dir.resolve("doc.pdf"), 3);

    PdfInfo info = service.open(pdf);

    assertEquals("doc.pdf", info.fileName());
    assertEquals(3, info.pageCount());
  }

  @Test
  void openRejectsMissingFile() {
    PdfOperationException e =
        assertThrows(PdfOperationException.class, () -> service.open(dir.resolve("nope.pdf")));
    assertTrue(e.getMessage().contains("não encontrado"));
  }

  @Test
  void openRejectsNonPdf() throws IOException {
    Path fake = Files.writeString(dir.resolve("fake.pdf"), "not a pdf");

    PdfOperationException e = assertThrows(PdfOperationException.class, () -> service.open(fake));
    assertTrue(e.getMessage().contains("fake.pdf"));
  }

  @Test
  void openPreviewListsPagesAndRenders() throws Exception {
    Path pdf = createPdf(dir.resolve("doc.pdf"), 2);

    try (PdfPreview preview = service.openPreview(pdf)) {
      assertEquals(2, preview.info().pageCount());
      assertEquals(new PageInfo(1, 612f, 792f), preview.pages().get(0));
      BufferedImage image = preview.render(2, 1.0f);
      assertEquals(612, image.getWidth());
      assertEquals(792, image.getHeight());
    }
  }

  @Test
  void openPreviewSwapsSizeOfRotatedPage() throws Exception {
    Path pdf = dir.resolve("rotated.pdf");
    try (PDDocument document = new PDDocument()) {
      PDPage page = new PDPage(PDRectangle.A4);
      page.setRotation(90);
      document.addPage(page);
      document.save(pdf.toFile());
    }

    try (PdfPreview preview = service.openPreview(pdf)) {
      PageInfo page = preview.pages().get(0);
      assertEquals(PDRectangle.A4.getHeight(), page.width());
      assertEquals(PDRectangle.A4.getWidth(), page.height());
    }
  }

  @Test
  void previewRejectsRenderAfterCloseOrOutOfRange() throws Exception {
    PdfPreview preview = service.openPreview(createPdf(dir.resolve("doc.pdf"), 1));

    assertThrows(PdfOperationException.class, () -> preview.render(2, 1.0f));
    preview.close();
    assertThrows(PdfOperationException.class, () -> preview.render(1, 1.0f));
  }

  @Test
  void openPreviewRejectsNonPdf() throws IOException {
    Path fake = Files.writeString(dir.resolve("fake.pdf"), "not a pdf");

    assertThrows(PdfOperationException.class, () -> service.openPreview(fake));
  }

  @Test
  void saveAsWritesCopyWithSamePages() throws Exception {
    Path source = createPdf(dir.resolve("in.pdf"), 2);
    Path target = dir.resolve("out.pdf");

    PdfInfo saved = service.saveAs(source, target);

    assertEquals(2, saved.pageCount());
    assertEquals(2, service.open(target).pageCount());
  }

  @Test
  void saveAsRejectsSameFile() throws Exception {
    Path source = createPdf(dir.resolve("in.pdf"), 1);

    assertThrows(
        PdfOperationException.class, () -> service.saveAs(source, dir.resolve("./in.pdf")));
  }

  @Test
  void saveAsReportsUnwritableTarget() throws Exception {
    Path source = createPdf(dir.resolve("in.pdf"), 1);

    assertThrows(
        PdfOperationException.class,
        () -> service.saveAs(source, dir.resolve("missing-dir/out.pdf")));
  }
}
