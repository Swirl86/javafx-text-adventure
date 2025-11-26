package com.sus.questbound.model;

public class Player {
    private String name;
    private int health;

    public Player(String name) {
        this.name = name;
        this.health = 100; // start health
    }

    public String getName() { return name; }
    public int getHealth() { return health; }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
    }

    public boolean isAlive() {
        return health > 0;
    }
}
