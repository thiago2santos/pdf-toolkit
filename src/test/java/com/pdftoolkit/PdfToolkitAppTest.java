package com.pdftoolkit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PdfToolkitAppTest {

  @Test
  void appNameIsStable() {
    assertEquals("PDF Toolkit", PdfToolkitApp.APP_NAME);
  }
}
