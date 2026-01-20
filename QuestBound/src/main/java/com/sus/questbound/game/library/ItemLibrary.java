package com.sus.questbound.game.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sus.questbound.model.Item;

import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ItemLibrary {

    private static final Random RANDOM = new Random();
    private static final List<ItemDefinition> DEFINITIONS = load();

    private static List<ItemDefinition> load() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = ItemLibrary.class
                    .getResourceAsStream("/world/items.json");

            if (is == null) {
                throw new IllegalStateException("items.json not found");
            }

            return mapper.readValue(is, new TypeReference<List<ItemDefinition>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load items.json", e);
        }
    }

    // ---------- public API ----------
    public static Item createRandomItem() {
        ItemDefinition def = DEFINITIONS.get(RANDOM.nextInt(DEFINITIONS.size()));
        return createItem(def);
    }

    public static Item createItemWithTag(String tag) {
        List<ItemDefinition> filtered = DEFINITIONS.stream()
                .filter(d -> d.getTags() != null && d.getTags().contains(tag))
                .toList();

        if (filtered.isEmpty()) {
            throw new IllegalArgumentException("No items with tag: " + tag);
        }

        return createItem(filtered.get(RANDOM.nextInt(filtered.size())));
    }

    // ---------- internal ----------
    private static boolean determinePickupable(ItemDefinition def) {
        if (Boolean.FALSE.equals(def.isPickupable())) {
            return false;
        }

        Set<String> tags = def.getTags();
        if (tags == null) return false;

        List<String> pickupTags = List.of(
                "weapon", "consumable", "tool", "quest", "accessory", "food", "light"
        );

        return tags.stream().anyMatch(pickupTags::contains);
    }

    private static Item createItem(ItemDefinition def) {
        return new Item(

                def.getId(),
                random(def.getNames()),
                random(def.getDescriptions()),
                determinePickupable(def),
                def.getDurability(),
                def.getDamage(),
                def.getTags()
        );
    }

    private static String random(List<String> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }
}