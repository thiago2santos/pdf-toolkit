package com.pdftoolkit.ui;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import javafx.concurrent.Task;

/** {@link BackgroundRunner} baseado em {@link Task}, com uma fila e uma thread daemon. */
public class FxBackgroundRunner implements BackgroundRunner {

  private final ExecutorService executor;

  /**
   * Cria o runner.
   *
   * @param threadName nome da thread de trabalho
   */
  public FxBackgroundRunner(String threadName) {
    this.executor =
        Executors.newSingleThreadExecutor(
            runnable -> {
              Thread thread = new Thread(runnable, threadName);
              thread.setDaemon(true);
              return thread;
            });
  }

  @Override
  public <T> void run(Callable<T> work, Consumer<T> onSuccess, Consumer<Throwable> onError) {
    Task<T> task =
        new Task<>() {
          @Override
          protected T call() throws Exception {
            return work.call();
          }
        };
    task.setOnSucceeded(event -> onSuccess.accept(task.getValue()));
    task.setOnFailed(event -> onError.accept(task.getException()));
    executor.execute(task);
  }
}
