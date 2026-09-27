package com.pdftoolkit.ui;

/** Mensagens ao usuário, desacopladas de {@code Alert} para permitir testar ViewModels. */
public interface UserNotifier {

  /**
   * Informa sucesso.
   *
   * @param header título curto
   * @param content detalhe
   */
  void info(String header, String content);

  /**
   * Informa falha.
   *
   * @param header título curto
   * @param content mensagem legível
   */
  void error(String header, String content);
}
