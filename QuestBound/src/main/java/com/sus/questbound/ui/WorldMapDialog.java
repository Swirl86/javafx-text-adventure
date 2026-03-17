package com.sus.questbound.ui;

import com.sus.questbound.ui.map.MapController;
import com.sus.questbound.ui.map.MapLayoutCalculator;
import com.sus.questbound.ui.renderer.ExitRenderer;
import com.sus.questbound.ui.renderer.ItemRenderer;
import com.sus.questbound.ui.renderer.PlayerMarkerRenderer;
import com.sus.questbound.ui.renderer.RoomRenderer;
import com.sus.questbound.ui.view.MapExitView;
import com.sus.questbound.ui.view.MapItemView;
import com.sus.questbound.ui.view.MapRoomView;
import com.sus.questbound.util.Css;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Displays an interactive world map dialog based on the current game state.
 * Uses map view models from GameEngine to render rooms, exits, items, and player position.
 */
public class WorldMapDialog {

    private static final double WIDTH = 600;
    private static final double HEIGHT = 400;

    private final RoomRenderer roomRenderer = new RoomRenderer();
    private final ItemRenderer itemRenderer = new ItemRenderer();
    private final PlayerMarkerRenderer playerRenderer = new PlayerMarkerRenderer();
    private final ExitRenderer exitRenderer = new ExitRenderer();

    public static void show(GameEngine engine) {
        new WorldMapDialog().open(engine);
    }

    private void open(GameEngine engine) {

        // Controller layer
        MapController controller = new MapController(engine);
        var rooms = controller.getRooms();
        var exits = controller.getExits();
        var playerRoom = controller.getPlayerRoom();

        // Layout math layer
        MapLayoutCalculator layout = new MapLayoutCalculator(WIDTH, HEIGHT, rooms, playerRoom);

        // UI setup
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Map");

        Pane pane = new Pane();
        pane.setMinSize(0, 0);

        // ----------------------------
        // DRAW EXITS
        // ----------------------------
        for (MapExitView exit : exits) {

            if (!exit.visible())
                continue;

            double fromX = layout.screenX(exit.fromX());
            double fromY = layout.screenY(exit.fromY());
            double toX = layout.screenX(exit.toX());
            double toY = layout.screenY(exit.toY());

            Line line = exitRenderer.createExitLine(fromX, fromY, toX, toY);
            pane.getChildren().add(line);
        }

        // ----------------------------
        // DRAW ROOMS
        // ----------------------------
        for (MapRoomView room : rooms) {

            if (!room.visited() && !room.playerHere())
                continue;

            double x = layout.screenX(room.x());
            double y = layout.screenY(room.y());

            // Room circle
            Circle circle = roomRenderer.createRoomNode(room);
            circle.setCenterX(x);
            circle.setCenterY(y);
            pane.getChildren().add(circle);

            // Room name
            roomRenderer.renderRoomName(pane, room, x, y);

            // ----------------------------
            // PLAYER ICON
            // ----------------------------
            if (room.playerHere()) {

                var icon = playerRenderer.createPlayerIcon();
                icon.setX(x - 12);
                icon.setY(y - 12);
                pane.getChildren().add(icon);

                var highlight = playerRenderer.createHighlightCircle(x, y);
                pane.getChildren().add(0, highlight);

                playerRenderer.playHighlightAnimation(highlight);
                playerRenderer.playBounceAnimation(icon);
            }

            // ----------------------------
            // ITEMS
            // ----------------------------
            if (room.visited() && !room.items().isEmpty()) {

                int count = room.items().size();
                double offset = Math.max(25, count * 5);

                for (int i = 0; i < count; i++) {
                    MapItemView item = room.items().get(i);

                    double angle = 2 * Math.PI * i / count;
                    double ix = x + Math.cos(angle) * offset;
                    double iy = y + Math.sin(angle) * offset;

                    Circle itemNode = itemRenderer.createItemNode(item);
                    itemNode.setCenterX(ix);
                    itemNode.setCenterY(iy);

                    pane.getChildren().add(itemNode);
                }
            }

            Tooltip.install(circle, roomRenderer.createRoomTooltip(room));
        }

        // ----------------------------
        // FORCE PANE TO FIT MAP CONTENT
        // ----------------------------
        pane.applyCss();
        pane.layout();

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (var node : pane.getChildren()) {
            var b = node.getBoundsInParent();
            minX = Math.min(minX, b.getMinX());
            minY = Math.min(minY, b.getMinY());
            maxX = Math.max(maxX, b.getMaxX());
            maxY = Math.max(maxY, b.getMaxY());
        }

        double padding = 80;

        double contentWidth = (maxX - minX) + padding;
        double contentHeight = (maxY - minY) + padding;

        pane.setPrefSize(contentWidth, contentHeight);

        // ----------------------------
        // SCROLL CONTAINER
        // ----------------------------
        Pane wrapper = new Pane(pane);
        wrapper.getStyleClass().add(Css.MAP_PANE);

        pane.setLayoutX(-minX + padding / 2);
        pane.setLayoutY(-minY + padding / 2);

        ScrollPane scroll = new ScrollPane(wrapper);
        scroll.setPannable(true);
        scroll.getStyleClass().add(Css.MAP_ROOT);

        wrapper.setPrefSize(contentWidth, contentHeight);

        scroll.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            wrapper.setMinSize(newVal.getWidth(), newVal.getHeight());
        });

        scroll.getStylesheets().add(
                Objects.requireNonNull(
                        WorldMapDialog.class.getResource("/com/sus/questbound/style.css")
                ).toExternalForm()
        );

        dialog.setScene(new Scene(scroll, WIDTH, HEIGHT));

        double playerX = layout.screenX(playerRoom.x());
        double playerY = layout.screenY(playerRoom.y());

        double adjustedPlayerX = playerX + pane.getLayoutX();
        double adjustedPlayerY = playerY + pane.getLayoutY();

        // Center the ScrollPane view on the player's position when the map opens
        Platform.runLater(() -> {
            scroll.setHvalue(adjustedPlayerX / wrapper.getWidth());
            scroll.setVvalue(adjustedPlayerY / wrapper.getHeight());
        });

        dialog.showAndWait();
    }
}