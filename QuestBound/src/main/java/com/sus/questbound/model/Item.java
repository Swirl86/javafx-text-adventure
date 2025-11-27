package com.sus.questbound.model;

public class Item {
    private final String name;
    private final String description;
    private final boolean pickupable;

    public Item(String name, String description, boolean pickupable) {
        this.name = name;
        this.description = description;
        this.pickupable = pickupable;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    @Override
    public String toString() {
        return name;
    }

    // TODO Add more predefined items
    public static final Item KEY = new Item("Key", "A small rusty key", true);
    public static final Item LANTERN = new Item("Lantern", "A lantern to light your way", true);
    public static final Item OLD_BOOK = new Item("Old Book", "A dusty old book with unreadable text", true);
    public static final Item MAP = new Item("Map", "A map showing the surrounding area", true);
    public static final Item TORCH = new Item("Torch", "A wooden torch to light dark areas", true);
    public static final Item GOLD_COIN = new Item("Gold Coin", "A shiny gold coin", true);
    public static final Item CHEST = new Item("Chest", "A locked chest that might contain something valuable", false);
    public static final Item STATUE = new Item("Statue", "An old stone statue, too heavy to move", false);
    public static final Item DOOR = new Item("Door", "A sturdy wooden door leading somewhere", false);
    public static final Item POTION = new Item("Potion", "A mysterious healing potion", true);
    public static final Item SWORD = new Item("Sword", "A sharp sword, good for combat", true);
    public static final Item SHIELD = new Item("Shield", "A sturdy shield for protection", true);
    public static final Item NOTE = new Item("Note", "A piece of paper with scribbled handwriting", true);
    public static final Item RING = new Item("Ring", "A small golden ring with an engraving", true);
    public static final Item TORCH_HOLDER = new Item("Torch Holder", "A fixture on the wall to hold a torch", false);
}
