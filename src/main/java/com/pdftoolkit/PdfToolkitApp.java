package com.pdftoolkit;

import com.pdftoolkit.core.PdfFileService;
import com.pdftoolkit.ui.FxBackgroundRunner;
import com.pdftoolkit.ui.main.MainView;
import com.pdftoolkit.ui.main.MainViewModel;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Entrypoint do PDF Toolkit: só monta dependências e a janela. */
public class PdfToolkitApp extends Application {

  public static final String APP_NAME = "PDF Toolkit";

  private MainViewModel viewModel;

  @Override
  public void start(Stage stage) {
    MainView view = new MainView(APP_NAME);
    viewModel =
        new MainViewModel(
            new PdfFileService(),
            new FxBackgroundRunner("pdf-io"),
            new FxBackgroundRunner("pdf-render"),
            view);
    view.bind(viewModel);
    stage.setTitle(APP_NAME);
    stage.setScene(new Scene(view.getRoot(), 900, 700));
    stage.show();
    getParameters().getUnnamed().stream().findFirst().map(Path::of).ifPresent(viewModel::open);
  }

  @Override
  public void stop() {
    if (viewModel != null) {
      viewModel.dispose();
    }
  }

  /**
   * Launches the JavaFX application.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
