package com.sus.questbound.ui.renderer;

import com.sus.questbound.ui.view.MapItemView;
import com.sus.questbound.ui.view.MapRoomView;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Responsible for rendering rooms on the world map.
 * Creates room nodes, applies visual styling based on room state,
 * and generates tooltips with room information.
 */
public class RoomRenderer {

    private static final double ROOM_RADIUS = 20;
    private static final double NAME_OFFSET = 36;

    public Circle createRoomNode(MapRoomView room) {

        // Gradient color (blue for dungeon exit, brown for normal rooms)
        Paint fill = room.dungeonExit()
                ? new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#6BB5FF")),
                new Stop(1, Color.web("#3A7CC4")))
                : new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#C49A6C")),
                new Stop(1, Color.web("#8B5E3C")));

        Circle circle = new Circle(ROOM_RADIUS);
        circle.setFill(fill);
        circle.setEffect(new DropShadow(12, Color.rgb(0, 0, 0, 0.4)));

        return circle;
    }

    public Tooltip createRoomTooltip(MapRoomView room) {
        String text = room.name() + "\n" + room.description();

        if (!room.items().isEmpty()) {
            String itemList = room.items().stream()
                    .map(MapItemView::name)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            text += "\nItems: " + itemList;
        }

        return new Tooltip(text);
    }

    public void renderRoomName(Pane pane, MapRoomView room, double x, double y) {

        Text name = new Text("[ " + room.name() + " ]");
        name.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-fill: #3A2A1A;");

        name.applyCss();

        double textWidth = name.getLayoutBounds().getWidth();

        // Center horizontally, place under the circle
        name.setX(x - textWidth / 2);
        name.setY(y + NAME_OFFSET);

        pane.getChildren().add(name);
    }
}
