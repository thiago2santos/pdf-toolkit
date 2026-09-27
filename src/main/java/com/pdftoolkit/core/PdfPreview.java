package com.pdftoolkit.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * PDF mantido aberto para renderizar páginas sob demanda. Feche com {@link #close()}.
 *
 * <p>Thread-safe: renderizações são serializadas porque o PDFBox não é seguro para acesso
 * concorrente ao mesmo documento.
 */
public final class PdfPreview implements AutoCloseable {

  private final PdfInfo info;
  private final List<PageInfo> pages;
  private final PDDocument document;
  private final PDFRenderer renderer;
  private boolean closed;

  PdfPreview(PdfInfo info, PDDocument document) {
    this.info = info;
    this.document = document;
    this.renderer = new PDFRenderer(document);
    this.pages = List.copyOf(readPages(document));
  }

  /**
   * Metadados do arquivo.
   *
   * @return metadados
   */
  public PdfInfo info() {
    return info;
  }

  /**
   * Páginas na ordem do documento.
   *
   * @return lista imutável de páginas
   */
  public List<PageInfo> pages() {
    return pages;
  }

  /**
   * Renderiza uma página.
   *
   * @param number número da página, começando em 1
   * @param scale escala (1.0 = 72 DPI)
   * @return imagem RGB da página
   * @throws PdfOperationException se o documento estiver fechado ou a página não renderizar
   */
  public synchronized BufferedImage render(int number, float scale) throws PdfOperationException {
    if (closed) {
      throw new PdfOperationException("O documento já foi fechado.");
    }
    if (number < 1 || number > pages.size()) {
      throw new PdfOperationException("Página inexistente: " + number);
    }
    try {
      return renderer.renderImage(number - 1, scale, ImageType.RGB);
    } catch (IOException e) {
      throw new PdfOperationException("Não foi possível renderizar a página " + number, e);
    }
  }

  @Override
  public synchronized void close() {
    if (closed) {
      return;
    }
    closed = true;
    try {
      document.close();
    } catch (IOException ignored) {
      // Nada a fazer: o arquivo é só leitura e o processo segue.
    }
  }

  private static List<PageInfo> readPages(PDDocument document) {
    List<PageInfo> result = new ArrayList<>(document.getNumberOfPages());
    int number = 1;
    for (PDPage page : document.getPages()) {
      PDRectangle box = page.getCropBox();
      boolean sideways = page.getRotation() % 180 != 0;
      float width = sideways ? box.getHeight() : box.getWidth();
      float height = sideways ? box.getWidth() : box.getHeight();
      result.add(new PageInfo(number++, width, height));
    }
    return result;
  }
}
