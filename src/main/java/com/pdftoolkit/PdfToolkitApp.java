package com.pdftoolkit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/** Entrypoint do PDF Toolkit (skeleton JavaFX). */
public class PdfToolkitApp extends Application {

  public static final String APP_NAME = "PDF Toolkit";

  @Override
  public void start(Stage stage) {
    BorderPane root = new BorderPane();
    root.setTop(createMenuBar());
    root.setCenter(new Label("Hello, PDF Toolkit"));

    stage.setTitle(APP_NAME);
    stage.setScene(new Scene(root, 640, 400));
    stage.show();
  }

  private MenuBar createMenuBar() {
    MenuItem helloItem = new MenuItem("Hello World");
    helloItem.setOnAction(event -> showHelloWorld());

    MenuItem exitItem = new MenuItem("Exit");
    exitItem.setOnAction(event -> javafx.application.Platform.exit());

    Menu fileMenu = new Menu("File");
    fileMenu.getItems().addAll(helloItem, new SeparatorMenuItem(), exitItem);

    MenuItem aboutItem = new MenuItem("About");
    aboutItem.setOnAction(event -> showAbout());

    Menu helpMenu = new Menu("Help");
    helpMenu.getItems().add(aboutItem);

    return new MenuBar(fileMenu, helpMenu);
  }

  private void showHelloWorld() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(APP_NAME);
    alert.setHeaderText(null);
    alert.setContentText("Hello World");
    alert.showAndWait();
  }

  private void showAbout() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("About");
    alert.setHeaderText(APP_NAME);
    alert.setContentText("Local-first PDF toolkit (v1 skeleton).\nJavaFX + Apache PDFBox.");
    alert.showAndWait();
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
