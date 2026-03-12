package com.sus.questbound.ui.map;

import com.sus.questbound.ui.view.MapRoomView;

import java.util.List;

/**
 * Calculates scaling, centering and coordinate transforms for the world map.
 * Converts room grid coordinates into screen coordinates.
 */
public class MapLayoutCalculator {

    private static final double SPACING = 1.8;

    private final double paneWidth;
    private final double paneHeight;

    private final int minX, minY;
    private final double scale;
    private final double offsetX, offsetY;

    public MapLayoutCalculator(double paneWidth, double paneHeight, List<MapRoomView> rooms, MapRoomView playerRoom) {

        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;

        this.minX = rooms.stream().mapToInt(MapRoomView::x).min().orElse(0);
        int maxX = rooms.stream().mapToInt(MapRoomView::x).max().orElse(0);

        this.minY = rooms.stream().mapToInt(MapRoomView::y).min().orElse(0);
        int maxY = rooms.stream().mapToInt(MapRoomView::y).max().orElse(0);

        double scaleX = paneWidth / ((maxX - minX) * SPACING + 3);
        double scaleY = paneHeight / ((maxY - minY) * SPACING + 3);
        this.scale = Math.min(scaleX, scaleY);

        this.offsetX = paneWidth / 2 - ((playerRoom.x() - minX) * SPACING + 0.5) * scale;
        this.offsetY = paneHeight / 2 - ((playerRoom.y() - minY) * SPACING + 0.5) * scale;
    }

    public double screenX(int gridX) {
        return ((gridX - minX) * SPACING + 0.5) * scale + offsetX;
    }

    public double screenY(int gridY) {
        return paneHeight - (((gridY - minY) * SPACING + 0.5) * scale + offsetY);
    }

    public double getScale() {
        return scale;
    }
}