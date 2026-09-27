package com.pdftoolkit.core;

/** Falha em uma operação de PDF, com mensagem legível para o usuário. */
public class PdfOperationException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Cria a exceção.
   *
   * @param message mensagem legível para o usuário
   */
  public PdfOperationException(String message) {
    super(message);
  }

  /**
   * Cria a exceção com causa.
   *
   * @param message mensagem legível para o usuário
   * @param cause causa original
   */
  public PdfOperationException(String message, Throwable cause) {
    super(message, cause);
  }
}
