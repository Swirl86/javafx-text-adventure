package com.sus.questbound.ui.map;

import com.sus.questbound.game.engine.GameLogic;
import com.sus.questbound.ui.view.MapExitView;
import com.sus.questbound.ui.view.MapRoomView;

import java.util.List;

/**
 * Provides map-related data and utilities for rendering.
 * Acts as a bridge between GameLogic and the UI layer.
 */
public record MapController(GameLogic gameLogic) {

    public List<MapRoomView> getRooms() {
        return gameLogic.getMapRooms();
    }

    public List<MapExitView> getExits() {
        return gameLogic.getMapExits();
    }

    public MapRoomView getPlayerRoom() {
        return gameLogic.getMapRooms().stream()
                .filter(MapRoomView::playerHere)
                .findFirst()
                .orElseThrow();
    }
}
