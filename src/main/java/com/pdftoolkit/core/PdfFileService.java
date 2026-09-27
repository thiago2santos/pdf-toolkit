package com.pdftoolkit.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;

/** Abrir (F1) e salvar (F7) PDFs no disco. Não depende de JavaFX. */
public class PdfFileService {

  /**
   * Abre um PDF e retorna seus metadados.
   *
   * @param path arquivo a abrir
   * @return metadados do PDF
   * @throws PdfOperationException se o arquivo não existir ou não for um PDF válido
   */
  public PdfInfo open(Path path) throws PdfOperationException {
    Path source = requireReadableFile(path);
    try (PDDocument document = load(source)) {
      return new PdfInfo(source, document.getNumberOfPages());
    } catch (IOException e) {
      throw new PdfOperationException("Não foi possível ler o PDF: " + source.getFileName(), e);
    }
  }

  /**
   * Abre um PDF e o mantém aberto para visualização. O chamador deve fechar o retorno.
   *
   * @param path arquivo a abrir
   * @return documento pronto para renderizar páginas
   * @throws PdfOperationException se o arquivo não existir ou não for um PDF válido
   */
  public PdfPreview openPreview(Path path) throws PdfOperationException {
    Path source = requireReadableFile(path);
    PDDocument document = null;
    try {
      document = load(source);
      return new PdfPreview(new PdfInfo(source, document.getNumberOfPages()), document);
    } catch (IOException | RuntimeException e) {
      closeQuietly(document);
      throw new PdfOperationException("Não foi possível ler o PDF: " + source.getFileName(), e);
    }
  }

  /**
   * Salva uma cópia do PDF de origem no destino, sobrescrevendo se já existir.
   *
   * @param source PDF de origem
   * @param target arquivo de destino
   * @return metadados do PDF salvo
   * @throws PdfOperationException se a origem for inválida ou o destino não puder ser gravado
   */
  public PdfInfo saveAs(Path source, Path target) throws PdfOperationException {
    Path from = requireReadableFile(source);
    Path to = target.toAbsolutePath().normalize();
    if (from.equals(to)) {
      throw new PdfOperationException("O destino deve ser diferente do arquivo original.");
    }
    try (PDDocument document = load(from)) {
      document.save(to.toFile());
      return new PdfInfo(to, document.getNumberOfPages());
    } catch (IOException e) {
      throw new PdfOperationException("Não foi possível salvar o PDF em: " + to, e);
    }
  }

  private static PDDocument load(Path path) throws PdfOperationException, IOException {
    try {
      return Loader.loadPDF(path.toFile());
    } catch (InvalidPasswordException e) {
      throw new PdfOperationException(
          "PDF protegido por senha não é suportado: " + path.getFileName(), e);
    }
  }

  private static void closeQuietly(PDDocument document) {
    if (document == null) {
      return;
    }
    try {
      document.close();
    } catch (IOException ignored) {
      // Já estamos reportando a falha original.
    }
  }

  private static Path requireReadableFile(Path path) throws PdfOperationException {
    Path normalized = path.toAbsolutePath().normalize();
    if (!Files.isRegularFile(normalized) || !Files.isReadable(normalized)) {
      throw new PdfOperationException("Arquivo não encontrado ou sem permissão: " + normalized);
    }
    return normalized;
  }
}
