package com.sus.questbound.ui.map;

import com.sus.questbound.ui.GameEngine;
import com.sus.questbound.ui.view.MapExitView;
import com.sus.questbound.ui.view.MapRoomView;

import java.util.List;

/**
 * Provides map-related data and utilities for rendering.
 * Acts as a bridge between GameLogic and the UI layer.
 */
public record MapController(GameEngine engine) {

    public List<MapRoomView> getRooms() {
        return engine.getMapRooms();
    }

    public List<MapExitView> getExits() {
        return engine.getMapExits();
    }

    public MapRoomView getPlayerRoom() {
        return engine.getMapRooms().stream()
                .filter(MapRoomView::playerHere)
                .findFirst()
                .orElseThrow();
    }
}
