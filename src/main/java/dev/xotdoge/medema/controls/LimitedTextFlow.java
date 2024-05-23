package dev.xotdoge.medema.controls;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class LimitedTextFlow extends TextFlow {
    private static final int MAX_LINES = 500;

    public LimitedTextFlow() {
        super();
    }

    public void appendInfoText(String text) {
        String[] lines = getCurrentText().split("\n", -1);
        // Проверка, если количество строк превышает ограничение
        if (lines.length > MAX_LINES) {
            this.getChildren().clear();
        }
        Text newText = new Text(text);
        this.getChildren().add(newText);
    }

    public void appendErrorText(String text) {
        String[] lines = getCurrentText().split("\n", -1);
        // Проверка, если количество строк превышает ограничение
        if (lines.length > MAX_LINES) {
            this.getChildren().clear();
        }
        Text coloredText = new Text(text);
        Color color = new Color(0.8, 0, 0.3, 1);
        coloredText.setFill(color);
        this.getChildren().add(coloredText);
    }

    private String getCurrentText() {
        StringBuilder currentText = new StringBuilder();
        for (var node : this.getChildren()) {
            if (node instanceof Text) {
                currentText.append(((Text) node).getText());
            }
        }
        return currentText.toString();
    }
}