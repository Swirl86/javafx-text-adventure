package com.sus.questbound.ui.renderer;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Renders the player marker on the world map and manages movement animations.
 * Creates the player icon and animates transitions between rooms.
 */
public class PlayerMarkerRenderer {

    public ImageView createPlayerIcon() {
        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(PlayerMarkerRenderer.class.getResource(
                        "/com/sus/questbound/icons/player.png"
                )).toExternalForm())
        );

        icon.setFitWidth(24);
        icon.setFitHeight(24);
        icon.setEffect(new DropShadow(20, Color.GOLD));

        return icon;
    }

    public Circle createHighlightCircle(double x, double y) {
        Circle highlight = new Circle(x, y, 25);
        highlight.setStroke(Color.web("#FFB300"));
        highlight.setStrokeWidth(3);
        highlight.setOpacity(0.8);
        return highlight;
    }

    public void playHighlightAnimation(Circle highlight) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), highlight);
        fade.setFromValue(1.0);
        fade.setToValue(0.2);
        fade.setCycleCount(Timeline.INDEFINITE);
        fade.setAutoReverse(true);
        fade.play();
    }

    public void playBounceAnimation(ImageView icon) {
        ScaleTransition bounce = new ScaleTransition(Duration.seconds(0.5), icon);
        bounce.setFromX(1.0);
        bounce.setFromY(1.0);
        bounce.setToX(1.2);
        bounce.setToY(1.2);
        bounce.setCycleCount(Timeline.INDEFINITE);
        bounce.setAutoReverse(true);
        bounce.play();
    }
}
