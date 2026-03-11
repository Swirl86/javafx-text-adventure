package com.sus.questbound.game.engine;

import com.sus.questbound.game.model.Direction;
import com.sus.questbound.game.model.Room;

import java.util.List;

public record MoveResult(boolean success, Room newRoom, List<Direction> availableExits) {}
