package com.sus.questbound.ui.renderer;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Renders exits (connections between rooms) on the world map.
 * Creates styled line segments for visible exits.
 */
public class ExitRenderer {

    public Line createExitLine(double fromX, double fromY, double toX, double toY) {
        Line line = new Line(fromX, fromY, toX, toY);
        line.setStroke(Color.web("#3A2A1A"));
        line.setStrokeWidth(3);
        line.setOpacity(0.35);
        line.setSmooth(true);
        return line;
    }
}