package com.pdftoolkit.ui;

import java.awt.image.BufferedImage;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

/** Conversão de imagens AWT para JavaFX sem depender do módulo {@code javafx.swing}. */
public final class FxImages {

  private FxImages() {}

  /**
   * Converte uma {@link BufferedImage} em {@link Image}.
   *
   * @param source imagem AWT
   * @return imagem JavaFX
   */
  public static Image toFxImage(BufferedImage source) {
    int width = source.getWidth();
    int height = source.getHeight();
    int[] argb = source.getRGB(0, 0, width, height, null, 0, width);
    WritableImage image = new WritableImage(width, height);
    image
        .getPixelWriter()
        .setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), argb, 0, width);
    return image;
  }
}
