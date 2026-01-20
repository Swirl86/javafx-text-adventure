package com.sus.questbound.game;

import com.sus.questbound.model.Room;

import java.util.List;

public record MoveResult(boolean success, Room newRoom, List<String> availableExits) {}
