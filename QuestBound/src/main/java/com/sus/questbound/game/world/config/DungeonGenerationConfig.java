package com.sus.questbound.game.world.config;

/**
 * Configuration object used by dungeon generators to control the size
 * and structural limits of the generated dungeon.
 *
 * @param minRooms the minimum number of rooms the generator must create
 * @param maxRooms the maximum number of rooms the generator is allowed to create
 */
public record DungeonGenerationConfig(int minRooms, int maxRooms) {}
