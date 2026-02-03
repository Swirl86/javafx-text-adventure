package com.sus.questbound.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    private final String name;
    private int health;
    private final List<Item> inventory;

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
    public List<Item > getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public boolean addItem(Item  item) {
        return inventory.add(item);
    }

    public boolean removeItem(Item  item) {
        return inventory.remove(item);
    }

    public boolean hasItem(Item  item) {
        return inventory.contains(item);
    }

    public boolean hasItemWithTag(String tag) {
        return inventory.stream()
                .anyMatch(item -> item.hasTag(tag));
    }

    public Item getItemByName(String name) {
        return inventory.stream()
                .filter(item -> item.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
