package com.sus.questbound.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private String description;
    private List<String> items = new ArrayList<>();

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<String> getItems() { return items; }

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }
}