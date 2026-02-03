package com.sus.questbound.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * All tags used for items in the game.
 * .
 * NOTE: These must match the "tags" field in items.json,
 * otherwise items may not be created or identified correctly.
 */
public enum ItemTags {

    LIGHT("light"),
    UTILITY("utility"),
    CONSUMABLE("consumable"),
    WEAPON("weapon"),
    MELEE("melee"),
    ARMOR("armor"),
    LORE("lore"),
    MAGIC("magic"),
    QUEST("quest"),
    ACCESSORY("accessory"),
    CURRENCY("currency"),
    NAVIGATION("navigation"),
    FOOD("food"),
    TOOL("tool"),
    CONTAINER("container"),
    VALUABLE("valuable"),
    MYSTERIOUS("mysterious"),
    BIG_DECORATION("big-decoration"),
    EXIT("exit"),
    FINAL_KEY("final-key");

    private final String id;

    ItemTags(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public static Set<String> tags(ItemTags... tags) {
        return Arrays.stream(tags)
                .map(ItemTags::id)
                .collect(Collectors.toSet());
    }

    public static boolean matches(String tag, ItemTags itemTag) {
        return itemTag.id.equalsIgnoreCase(tag);
    }
}