package com.sus.questbound.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Room {

    private final String name;
    private final String description;
    private final List<String> items;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(String item) {
        items.add(item);
    }

    public boolean removeItem(String item) {
        return items.remove(item);
    }

    public boolean hasItem(String item) {
        return items.contains(item);
    }
}
