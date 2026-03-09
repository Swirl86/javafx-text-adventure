package com.sus.questbound.ui;

import com.sus.questbound.logic.GameLogicController;
import com.sus.questbound.model.Item;
import com.sus.questbound.model.ItemTags;
import com.sus.questbound.model.Room;
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

public class MapDialog {

    public static void show(GameLogicController gameLogic, Room playerRoom) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Map");

        Pane pane = new Pane();
        pane.getStyleClass().add("map-pane");

        double paneWidth = 600;
        double paneHeight = 400;
        pane.setPrefSize(paneWidth, paneHeight);

        // Calculate world boundaries to scale the map correctly
        int minX = gameLogic.getAllRooms().stream().mapToInt(Room::getX).min().orElse(0);
        int maxX = gameLogic.getAllRooms().stream().mapToInt(Room::getX).max().orElse(0);
        int minY = gameLogic.getAllRooms().stream().mapToInt(Room::getY).min().orElse(0);
        int maxY = gameLogic.getAllRooms().stream().mapToInt(Room::getY).max().orElse(0);

        // Dynamic scaling so the map fits inside the window
        double scaleX = paneWidth / (maxX - minX + 3);
        double scaleY = paneHeight / (maxY - minY + 3);
        double scale = Math.min(scaleX, scaleY);

        // Center the map around the player's current room
        double offsetX = paneWidth / 2 - (playerRoom.getX() - minX + 0.5) * scale;
        double offsetY = paneHeight / 2 - (playerRoom.getY() - minY + 0.5) * scale;

        for (Room room : gameLogic.getAllRooms()) {

            // Only draw rooms that have been visited (or the player's current room)
            if (!room.isVisited() && room != playerRoom)
                continue;

            double roomX = (room.getX() - minX + 0.5) * scale + offsetX;
            double roomY = paneHeight - ((room.getY() - minY + 0.5) * scale + offsetY);

            // Draw exits only if the connected room is also visited
            room.getExits().forEach((dir, exitRoom) -> {

                // Skip exits leading to unvisited rooms
                if (!exitRoom.isVisited() && exitRoom != playerRoom)
                    return;

                // Avoid drawing duplicate lines between rooms
                if (System.identityHashCode(room) < System.identityHashCode(exitRoom)) {
                    double exitX = (exitRoom.getX() - minX + 0.5) * scale + offsetX;
                    double exitY = paneHeight - ((exitRoom.getY() - minY + 0.5) * scale + offsetY);

                    Line line = new Line(roomX, roomY, exitX, exitY);
                    line.setStroke(Color.web("#4B3621"));
                    line.setStrokeWidth(2);
                    line.setOpacity(0.5);
                    pane.getChildren().add(line);
                }
            });

            // Room color (brown for normal rooms, blue for dungeon exit)
            LinearGradient grad = room.isDungeonExit()
                    ? new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#4C90C0")), new Stop(1, Color.web("#2A6CA9")))
                    : new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#8B5E3C")), new Stop(1, Color.web("#5A3E21")));

            Circle roomCircle = new Circle(roomX, roomY, 20);
            roomCircle.setFill(grad);
            roomCircle.setEffect(new DropShadow(5, Color.BLACK));
            pane.getChildren().add(roomCircle);

            // Draw room name above the circle
            Text name = new Text(roomX - room.getName().length() * 3, roomY - 30, room.getName());
            pane.getChildren().add(name);

            // Draw player icon if this is the player's current room
            if (room.equals(playerRoom)) {
                ImageView playerIcon = new ImageView(
                        new Image(Objects.requireNonNull(MapDialog.class.getResource(
                                "/com/sus/questbound/icons/player.png")).toExternalForm())
                );
                playerIcon.setFitWidth(24);
                playerIcon.setFitHeight(24);
                playerIcon.setX(roomX - 12);
                playerIcon.setY(roomY - 12);
                pane.getChildren().add(playerIcon);

                // Animated highlight circle behind the player
                Circle highlight = new Circle(roomX, roomY, 25);
                highlight.setStroke(Color.ORANGE);
                highlight.setFill(Color.TRANSPARENT);
                highlight.setStrokeWidth(2);
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
            if (room.isVisited()) {
                int itemCount = room.getItems().size();
                if (itemCount > 0) {
                    double itemOffset = Math.max(25, itemCount * 5);

                    for (int i = 0; i < itemCount; i++) {
                        Item item = room.getItems().get(i);

                        // Position items in a circle around the room
                        double angle = 2 * Math.PI * i / itemCount;
                        double ix = roomX + Math.cos(angle) * itemOffset;
                        double iy = roomY + Math.sin(angle) * itemOffset;

                        Circle itemCircle = getCircle(ix, iy, item);

                        Tooltip tooltip = new Tooltip(item.name());
                        Tooltip.install(itemCircle, tooltip);

                        pane.getChildren().add(itemCircle);
                    }
                }
            }

            // Tooltip for visited rooms
            if (room.isVisited()) {
                String itemsText = room.getItems().stream()
                        .map(Item::name)
                        .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);

                String tooltipText = room.getName() + "\n" + room.getDescription();
                if (!itemsText.isEmpty())
                    tooltipText += "\nItems: " + itemsText;

                Tooltip tooltip = new Tooltip(tooltipText);
                Tooltip.install(roomCircle, tooltip);
            }
        }

        // Scrollable map container
        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setPannable(true);
        scrollPane.getStyleClass().add("map-root");

        // Apply CSS stylesheet
        scrollPane.getStylesheets().add(
                Objects.requireNonNull(
                        MapDialog.class.getResource("/com/sus/questbound/style.css")
                ).toExternalForm()
        );

        dialog.setScene(new Scene(scrollPane, paneWidth, paneHeight));
        dialog.showAndWait();
    }

    private static Circle getCircle(double ix, double iy, Item item) {
        Circle itemCircle = new Circle(ix, iy, 6);

        // Color items based on type
        if (item.hasTag(ItemTags.FINAL_KEY.id()))
            itemCircle.setFill(Color.BLUE);
        else if (item.isPickupable())
            itemCircle.setFill(Color.GREEN);
        else
            itemCircle.setFill(Color.RED);

        itemCircle.setStroke(Color.BLACK);
        itemCircle.setStrokeWidth(1);
        return itemCircle;
    }
}
