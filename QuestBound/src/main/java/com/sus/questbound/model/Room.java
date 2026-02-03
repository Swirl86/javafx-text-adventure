package com.sus.questbound.model;

import java.util.*;

public class Room {

    private final String name;
    private final String description;
    private final int x;
    private final int y;

    private final List<Item> items;
    private final Map<Direction, Room> exits = new EnumMap<>(Direction.class);

    private boolean dungeonExit;

    public Room(String name, String description, int x, int y) {
        this.name = name;
        this.description = description;
        this.x = x;
        this.y = y;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDungeonExit() {
        return dungeonExit;
    }

    public void setDungeonExit(boolean dungeonExit) {
        this.dungeonExit = dungeonExit;
    }

    public void setExit(Direction direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(Direction direction) {
        return exits.get(direction);
    }

    public Set<Direction> getAvailableExits() {
        return exits.keySet();
    }

    public boolean hasItem(Item item) {
        return items.contains(item);
    }

    public boolean containsItemWithTag(String tag) {
        return items.stream()
                .anyMatch(item -> item.hasTag(tag));
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public Item getItemByName(String name) {
        return items.stream()
                .filter(item -> item.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public boolean addItem(Item item) {
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }
}