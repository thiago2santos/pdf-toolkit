package com.pdftoolkit.core;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Metadados de um PDF aberto.
 *
 * @param path caminho absoluto do arquivo
 * @param pageCount número de páginas
 */
public record PdfInfo(Path path, int pageCount) {

  /** Valida os campos. */
  public PdfInfo {
    Objects.requireNonNull(path, "path");
    if (pageCount < 0) {
      throw new IllegalArgumentException("pageCount must be >= 0");
    }
  }

  /**
   * Nome do arquivo sem diretório.
   *
   * @return nome do arquivo
   */
  public String fileName() {
    return path.getFileName().toString();
  }
}
