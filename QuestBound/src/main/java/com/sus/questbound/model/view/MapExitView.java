package com.sus.questbound.model.view;

public record MapExitView(
        int fromX,
        int fromY,
        int toX,
        int toY,
        boolean visible
) {}
