package com.pdftoolkit.ui;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/** Executa trabalho fora da thread da UI e entrega o resultado na thread da UI. */
public interface BackgroundRunner {

  /**
   * Executa {@code work} em background.
   *
   * @param work trabalho a executar
   * @param onSuccess chamado na thread da UI com o resultado
   * @param onError chamado na thread da UI com a falha
   * @param <T> tipo do resultado
   */
  <T> void run(Callable<T> work, Consumer<T> onSuccess, Consumer<Throwable> onError);
}
