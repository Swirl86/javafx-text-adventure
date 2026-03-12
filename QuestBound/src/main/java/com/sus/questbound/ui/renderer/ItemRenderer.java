package com.sus.questbound.ui.renderer;

import com.sus.questbound.ui.view.MapItemView;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Handles the visual representation of items on the world map.
 * Creates item icons, assigns colors or symbols, and attaches tooltips
 * describing the item.
 */
public class ItemRenderer {

    private static final double ITEM_RADIUS = 7;

    public Circle createItemNode(MapItemView item) {
        Circle circle = new Circle(ITEM_RADIUS);

        circle.setFill(Color.web(item.colorHex()));
        circle.setStroke(Color.web("#3A2A1A"));
        circle.setStrokeWidth(1.5);
        circle.setEffect(new DropShadow(5, Color.rgb(0, 0, 0, 0.3)));

        Tooltip.install(circle, new Tooltip(item.name()));

        return circle;
    }
}
