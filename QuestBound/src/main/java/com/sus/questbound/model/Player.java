package com.sus.questbound.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    private final String name;
    private int health;
    private final List<String> inventory;

    public Player(String name) {
        this.name = name;
        this.health = 100;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Returns an unmodifiable view of the player's inventory.
     * Prevents external modification.
     */
    public List<String> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public void addItem(String item) {
        inventory.add(item);
    }

    public boolean removeItem(String item) {
        return inventory.remove(item);
    }

    public boolean hasItem(String item) {
        return inventory.contains(item);
    }
}
