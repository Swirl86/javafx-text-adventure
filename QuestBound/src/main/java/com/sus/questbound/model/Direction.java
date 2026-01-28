package com.sus.questbound.model;

public enum Direction {
    NORTH(0, 1),
    SOUTH(0, -1),
    EAST(1, 0),
    WEST(-1, 0),
    UP(0, 0),
    DOWN(0, 0);

    public final int dx;
    public final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public Direction opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
            case UP -> DOWN;
            case DOWN -> UP;
        };
    }

    public boolean isPlanar() {
        return this == NORTH || this == SOUTH || this == EAST || this == WEST;
    }

    public String key() {
        return label();
    }

    /** Text representation for UI */
    public String label() {
        return name().toLowerCase();
    }
}

