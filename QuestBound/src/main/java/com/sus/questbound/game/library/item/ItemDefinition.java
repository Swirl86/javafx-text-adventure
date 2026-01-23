package com.sus.questbound.game.library.item;

import java.util.List;
import java.util.Set;

public class ItemDefinition {

    private String id;
    private List<String> names;
    private List<String> descriptions;
    private Set<String> tags;
    private Integer durability;
    private Integer damage;
    private Boolean pickupable = false;

    public ItemDefinition() {
        // Default constructor needed for Jackson
    }

    // ---------- Getters och setters ----------
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public List<String> getNames() { return names; }
    public void setNames(List<String> names) { this.names = names; }

    public List<String> getDescriptions() { return descriptions; }
    public void setDescriptions(List<String> descriptions) { this.descriptions = descriptions; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

    public Integer getDurability() { return durability; }
    public void setDurability(Integer durability) { this.durability = durability; }

    public Integer getDamage() { return damage; }
    public void setDamage(Integer damage) { this.damage = damage; }

    public Boolean isPickupable() { return pickupable; }
    public void setPickupable(Boolean pickupable) { this.pickupable = pickupable; }
}