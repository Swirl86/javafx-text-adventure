package com.sus.questbound.util;

import com.sus.questbound.game.library.SystemMessageDefinition;
import com.sus.questbound.game.library.SystemMessageLibrary;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SystemMsgHelper {

    private static final SystemMessageDefinition MSG = SystemMessageLibrary.get();

    // ---------- Welcome ----------
    public static String randomWelcome(Player player) {
        String welcome = random(MSG.welcome.welcome).replace("{player}", player.getName());
        String ambient = random(MSG.welcome.ambient);
        return welcome + " ✦ " + ambient + " ✦";
    }

    // ---------- Movement ----------
    public static String moveMessage(String direction) {
        return MSG.movement.move.replace("{direction}", direction);
    }

    public static String enterRoom(Room room) {
        return MSG.movement.enterRoom
                .replace("{room}", room.getName())
                .replace("{description}", room.getDescription());
    }

    // ---------- Room ----------
    public static String nothingToSee() {
        return MSG.room.nothingToSee;
    }

    public static String singleItemInRoom(String itemName, String description) {
        return MSG.room.singleItem
                .replace("{item}", itemName)
                .replace("{description}", description);
    }

    public static String multipleItemsInRoom(String itemList) {
        return MSG.room.multipleItems.replace("{items}", itemList);
    }

    public static String noVisibleExits() {
        return MSG.room.noExits;
    }

    public static String singleVisibleExit(String exit) {
        return MSG.room.singleExit.replace("{exit}", exit);
    }

    public static String multipleVisibleExits(String exitList) {
        return MSG.room.multipleExits.replace("{exits}", exitList);
    }

    // ---------- Inventory ----------
    public static String inventoryEmpty() {
        return MSG.inventory.empty;
    }

    public static String singleItemInInventory(String itemName, String description) {
        return MSG.inventory.singleItem
                .replace("{item}", itemName)
                .replace("{description}", description);
    }

    public static String multipleItemsInInventory(String itemList) {
        return MSG.inventory.multipleItems.replace("{items}", itemList);
    }

    public static String itemNotInInventory(String itemName) {
        return MSG.inventory.itemNotInInventory.replace("{item}", itemName);
    }

    // ---------- Pickup ----------
    public static String askWhichItemToPickup() {
        return MSG.pickup.askWhich;
    }

    public static String nothingToPickup() {
        return MSG.pickup.nothing;
    }

    public static String itemNotHere(String itemName) {
        return MSG.pickup.itemNotHere.replace("{item}", itemName);
    }

    public static String pickupDialogTitle() {
        return MSG.pickup.dialog.title;
    }

    public static String pickupDialogHeader() {
        return MSG.pickup.dialog.header;
    }

    public static String pickupDialogContent() {
        return MSG.pickup.dialog.content;
    }

    // ---------- Drop ----------
    public static String askWhichItemToDrop() {
        return MSG.drop.askWhich;
    }

    public static String dropDialogTitle() {
        return MSG.drop.dialog.title;
    }

    public static String dropDialogHeader() {
        return MSG.drop.dialog.header;
    }

    public static String dropDialogContent() {
        return MSG.drop.dialog.content;
    }

    /** // ---------- TODO implement ----------
    public static String randomEnemyHint() {
        return random(MSG.enemyHints.values());
    }

    public static String hpHintMessage(int hp, int maxHp) {
        double ratio = (double) hp / maxHp;
        if (ratio > 0.8) return MSG.hpHints.fullEnergy;
        if (ratio > 0.6) return MSG.hpHints.slightlyTired;
        if (ratio > 0.4) return MSG.hpHints.tired;
        if (ratio > 0.2) return MSG.hpHints.veryTired;
        return MSG.hpHints.critical;
    }

    public static String durabilityHintMessage(int durability, int maxDurability) {
        double ratio = (double) durability / maxDurability;
        if (ratio > 0.8) return MSG.durabilityHints.excellent;
        if (ratio > 0.6) return MSG.durabilityHints.good;
        if (ratio > 0.4) return MSG.durabilityHints.worn;
        if (ratio > 0.2) return MSG.durabilityHints.damaged;
        return MSG.durabilityHints.broken;
    }*/

    // ---------- Helper ----------
    private static String random(List<String> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
}