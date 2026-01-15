package com.sus.questbound.ui;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public record OutputController(TextFlow outputFlow, ScrollPane scrollPane) {

    // ---------- System messages ----------
    public void printlnSystem(String msg) {
        appendStyled("[" + msg + "]", "msg-system");
    }

    // ---------- GM messages ----------
    public void printlnGM(String msg) {
        appendStyled("GM " + msg, "msg-gm");
    }

    // ---------- Player messages ----------
    public void printlnPlayer(String msg) {
        appendStyled("> " + msg, "msg-player");
    }

    // ---------- Internal helper ----------
    private void appendStyled(String message, String styleClass) {
        Text t = new Text(message + "\n");
        t.getStyleClass().addAll("text", styleClass);

        Platform.runLater(() -> {
            outputFlow.getChildren().add(t);
            scrollPane.layout();
            scrollPane.setVvalue(1.0); // scroll to bottom
        });
    }
}

