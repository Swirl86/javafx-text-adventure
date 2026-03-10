package com.sus.questbound.model.view;

import java.util.List;

public record MapRoomView(
        int x,
        int y,
        String name,
        String description,
        boolean visited,
        boolean dungeonExit,
        boolean playerHere,
        List<MapItemView> items
) {}
