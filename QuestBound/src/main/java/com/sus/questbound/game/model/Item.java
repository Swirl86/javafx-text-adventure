package com.sus.questbound.game.model;

import java.util.Set;

/**
 * @param durability null if not applicable
 * @param damage     null if not weapon
 */
public record Item(String id, String name, String description, boolean isPickupable, Integer durability, Integer damage,
                   Set<String> tags) {

    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }

    @Override
    public String toString() {
        return name;
    }
}