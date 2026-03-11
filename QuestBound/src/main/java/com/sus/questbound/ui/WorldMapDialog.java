package com.sus.questbound.ui;

import com.sus.questbound.game.engine.GameLogic;
import com.sus.questbound.ui.view.MapExitView;
import com.sus.questbound.ui.view.MapItemView;
import com.sus.questbound.ui.view.MapRoomView;
import com.sus.questbound.util.Css;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class WorldMapDialog {

    public static void show(GameLogic gameLogic) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Map");

        Pane pane = new Pane();
        pane.getStyleClass().add(Css.MAP_PANE);

        double paneWidth = 600;
        double paneHeight = 400;
        pane.setPrefSize(paneWidth, paneHeight);

        // Fetch DTO data
        var rooms = gameLogic.getMapRooms();
        var exits = gameLogic.getMapExits();

        // Calculate world bounds
        int minX = rooms.stream().mapToInt(MapRoomView::x).min().orElse(0);
        int maxX = rooms.stream().mapToInt(MapRoomView::x).max().orElse(0);
        int minY = rooms.stream().mapToInt(MapRoomView::y).min().orElse(0);
        int maxY = rooms.stream().mapToInt(MapRoomView::y).max().orElse(0);

        // Dynamic scaling so the map fits inside the window
        double scaleX = paneWidth / (maxX - minX + 3);
        double scaleY = paneHeight / (maxY - minY + 3);
        double scale = Math.min(scaleX, scaleY);

        // Center the map around the player's current room
        MapRoomView playerRoom = rooms.stream()
                .filter(MapRoomView::playerHere)
                .findFirst()
                .orElseThrow();

        double offsetX = paneWidth / 2 - (playerRoom.x() - minX + 0.5) * scale;
        double offsetY = paneHeight / 2 - (playerRoom.y() - minY + 0.5) * scale;

        // --- DRAW EXITS ---
        for (MapExitView exit : exits) {

            if (!exit.visible())
                continue;

            double fromX = (exit.fromX() - minX + 0.5) * scale + offsetX;
            double fromY = paneHeight - ((exit.fromY() - minY + 0.5) * scale + offsetY);

            double toX = (exit.toX() - minX + 0.5) * scale + offsetX;
            double toY = paneHeight - ((exit.toY() - minY + 0.5) * scale + offsetY);

            Line line = new Line(fromX, fromY, toX, toY);
            line.setStroke(Color.web("#3A2A1A"));
            line.setStrokeWidth(3);
            line.setOpacity(0.35);
            line.setSmooth(true);
            pane.getChildren().add(line);
        }

        // --- DRAW ROOMS ---
        for (MapRoomView room : rooms) {

            if (!room.visited() && !room.playerHere())
                continue;

            double roomX = (room.x() - minX + 0.5) * scale + offsetX;
            double roomY = paneHeight - ((room.y() - minY + 0.5) * scale + offsetY);

            // Room color (brown for normal rooms, blue for dungeon exit)
            LinearGradient grad = room.dungeonExit()
                    ? new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#6BB5FF")),
                    new Stop(1, Color.web("#3A7CC4")))
                    : new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#C49A6C")),
                    new Stop(1, Color.web("#8B5E3C")));

            Circle roomCircle = new Circle(roomX, roomY, 20);
            roomCircle.setFill(grad);
            roomCircle.setEffect(new DropShadow(12, Color.rgb(0,0,0,0.4)));
            pane.getChildren().add(roomCircle);

            // Draw room name above the circle
            Text name = new Text(roomX - room.name().length() * 3, roomY - 28, room.name());
            name.setFill(Color.web("#3A2A1A"));
            name.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            pane.getChildren().add(name);

            // Draw player icon if this is the player's current room
            if (room.playerHere()) {
                ImageView playerIcon = new ImageView(
                        new Image(Objects.requireNonNull(WorldMapDialog.class.getResource(
                                "/com/sus/questbound/icons/player.png")).toExternalForm())
                );
                playerIcon.setEffect(new DropShadow(20, Color.GOLD));
                playerIcon.setFitWidth(24);
                playerIcon.setFitHeight(24);
                playerIcon.setX(roomX - 12);
                playerIcon.setY(roomY - 12);
                pane.getChildren().add(playerIcon);

                // Animated highlight circle behind the player
                Circle highlight = new Circle(roomX, roomY, 25);
                highlight.setStroke(Color.web("#FFB300"));
                highlight.setStrokeWidth(3);
                highlight.setOpacity(0.8);
                pane.getChildren().add(0, highlight);

                FadeTransition fade = new FadeTransition(Duration.seconds(1), highlight);
                fade.setFromValue(1.0);
                fade.setToValue(0.2);
                fade.setCycleCount(Timeline.INDEFINITE);
                fade.setAutoReverse(true);
                fade.play();

                ScaleTransition bounce = new ScaleTransition(Duration.seconds(0.5), playerIcon);
                bounce.setFromX(1.0);
                bounce.setFromY(1.0);
                bounce.setToX(1.2);
                bounce.setToY(1.2);
                bounce.setCycleCount(Timeline.INDEFINITE);
                bounce.setAutoReverse(true);
                bounce.play();
            }

            // Draw items only in visited rooms
            if (room.visited() && !room.items().isEmpty()) {
                int itemCount = room.items().size();
                double itemOffset = Math.max(25, itemCount * 5);

                for (int i = 0; i < itemCount; i++) {
                    MapItemView item = room.items().get(i);

                    double angle = 2 * Math.PI * i / itemCount;
                    double ix = roomX + Math.cos(angle) * itemOffset;
                    double iy = roomY + Math.sin(angle) * itemOffset;

                    Circle itemCircle = new Circle(ix, iy, 7);
                    itemCircle.setFill(Color.web(item.colorHex()));
                    itemCircle.setStroke(Color.web("#3A2A1A"));
                    itemCircle.setStrokeWidth(1.5);
                    itemCircle.setEffect(new DropShadow(5, Color.rgb(0,0,0,0.3)));

                    Tooltip tooltip = new Tooltip(item.name());
                    Tooltip.install(itemCircle, tooltip);

                    pane.getChildren().add(itemCircle);
                }
            }

            // Tooltip for visited rooms
            String tooltipText = room.name() + "\n" + room.description();

            if (!room.items().isEmpty()) {
                String itemList = room.items().stream()
                        .map(MapItemView::name)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");

                tooltipText += "\nItems: " + itemList;
            }

            Tooltip tooltip = new Tooltip(tooltipText);
            Tooltip.install(roomCircle, tooltip);
        }

        // Scrollable map container
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setPannable(true);
        scrollPane.getStyleClass().add(Css.MAP_ROOT);

        scrollPane.getStylesheets().add(
                Objects.requireNonNull(
                        WorldMapDialog.class.getResource("/com/sus/questbound/style.css")
                ).toExternalForm()
        );

        dialog.setScene(new Scene(scrollPane, paneWidth, paneHeight));
        dialog.showAndWait();
    }
}