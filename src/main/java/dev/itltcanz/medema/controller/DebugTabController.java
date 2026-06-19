package dev.itltcanz.medema.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class DebugTabController {

  @FXML
  public TextArea debugArea;

  @FXML
  public void addToDebug(String text) {
    debugArea.appendText(text);
    checkLinesCount();
  }

  public void checkLinesCount() {
    int maxLines = 500;
    String currentText = debugArea.getText();
    long currentLinesCount = currentText.lines().count();
    if (currentLinesCount > maxLines) {
      long linesToRemove = currentLinesCount - maxLines;
      int cutIndex = 0;
      for (int i = 0; i < linesToRemove; i++) {
        cutIndex = currentText.indexOf('\n', cutIndex) + 1;
      }
      if (cutIndex > 0 && cutIndex <= debugArea.getLength()) {
        debugArea.deleteText(0, cutIndex);
      }
    }
  }
}
