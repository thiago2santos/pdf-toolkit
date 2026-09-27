package com.pdftoolkit.ui.main;

import com.pdftoolkit.core.PageInfo;
import com.pdftoolkit.ui.FxImages;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Célula que mostra uma página renderizada, com placeholder do tamanho real enquanto carrega. */
final class PageCell extends ListCell<PageInfo> {

  /** Pixels de tela por ponto PDF (1.0 = tamanho real a 72 DPI). */
  static final double DISPLAY_SCALE = 1.0;

  private final MainViewModel viewModel;
  private final Map<PageInfo, Image> cache;
  private final ImageView imageView = new ImageView();
  private final Label loading = new Label("Carregando…");
  private final StackPane sheet = new StackPane(imageView, loading);
  private final Label caption = new Label();
  private final VBox box = new VBox(4, sheet, caption);

  private volatile PageInfo requested;

  PageCell(MainViewModel viewModel, Map<PageInfo, Image> cache) {
    this.viewModel = viewModel;
    this.cache = cache;
    imageView.setPreserveRatio(true);
    imageView.setSmooth(true);
    sheet.setStyle(
        "-fx-background-color: white; -fx-effect: dropshadow(gaussian, #0004, 6, 0, 0, 1);");
    box.setAlignment(Pos.CENTER);
    setAlignment(Pos.CENTER);
    setStyle("-fx-background-color: transparent; -fx-padding: 8;");
  }

  @Override
  protected void updateItem(PageInfo page, boolean empty) {
    super.updateItem(page, empty);
    if (empty || page == null) {
      requested = null;
      setGraphic(null);
      return;
    }
    if (page == requested && getGraphic() != null) {
      return;
    }
    requested = page;
    double width = page.width() * DISPLAY_SCALE;
    double height = page.height() * DISPLAY_SCALE;
    sheet.setMinSize(width, height);
    sheet.setPrefSize(width, height);
    sheet.setMaxSize(width, height);
    imageView.setFitWidth(width);
    imageView.setFitHeight(height);
    caption.setText("Página " + page.number());
    setGraphic(box);

    Image cached = cache.get(page);
    if (cached != null) {
      show(cached);
      return;
    }
    imageView.setImage(null);
    loading.setText("Carregando…");
    loading.setVisible(true);
    viewModel.renderPage(
        page,
        () -> requested == page,
        image -> {
          Image fxImage = FxImages.toFxImage(image);
          cache.put(page, fxImage);
          if (requested == page) {
            show(fxImage);
          }
        },
        () -> {
          if (requested == page) {
            loading.setText("Falha ao renderizar a página.");
          }
        });
  }

  private void show(Image image) {
    imageView.setImage(image);
    loading.setVisible(false);
  }
}
