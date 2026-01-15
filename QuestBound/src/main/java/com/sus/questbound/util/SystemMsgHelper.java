package com.sus.questbound.util;

import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SystemMsgHelper {

    // Randomized welcome + ambient message
    public static String randomWelcome(Player player) {
        List<String> welcomes = List.of(
                "Welcome, " + player.getName() + "!",
                "Greetings, " + player.getName() + "!",
                "Ah, " + player.getName() + ", you have arrived!",
                "Hello, " + player.getName() + "! Ready for an adventure?"
        );

        List<String> ambients = List.of(
                "You hear distant sounds echoing around you.\nMaybe look around to explore your surroundings.",
                "The air is still, but you sense something nearby.",
                "Shadows flicker across the walls as you stand ready.",
                "You feel the weight of the silence before your next move."
        );

        String welcome = welcomes.get(ThreadLocalRandom.current().nextInt(welcomes.size()));
        String ambient = ambients.get(ThreadLocalRandom.current().nextInt(ambients.size()));

        return welcome + "\n" + ambient;
    }

    public static String moveMessage(String direction) {
        return "You move " + direction + "...";
    }

    public static String enterRoomMessage(Room room) {
        return "You are in: " + room.getName() + "\n" + room.getDescription();
    }

    public static String nothingToSee() {
        return "You see nothing of interest.";
    }

    public static String askWhichItemToPickup() {
        return "Which item do you want to pick up?";
    }

    public static String askWhichItemToDrop() {
        return "Which item do you want to drop?";
    }

    public static String inventoryEmpty() {
        return "Your inventory is empty.";
    }

    public static String singleItemInRoom(String itemName, String description) {
        return "You see one item: " + itemName + " — " + description;
    }

    public static String multipleItemsInRoom(String itemList) {
        return "You see some items: " + itemList;
    }

    public static String singleItemInInventory(String itemName, String description) {
        return "You have one item: " + itemName + " — " + description;
    }

    public static String multipleItemsInInventory(String itemList) {
        return "You have some items: " + itemList;
    }

    public static String itemNotHere(String itemName) {
        return "There is no '" + itemName + "' here to pick up.";
    }

    public static String itemNotInInventory(String itemName) {
        return "You don't have '" + itemName + "' in your inventory.";
    }

    public static String noVisibleExits() {
        return "There are no visible exits here.";
    }

    public static String singleVisibleExit(String exit) {
        return "There is one visible exit: " + exit;
    }

    public static String multipleVisibleExits(String exitList) {
        return "There are several visible exits: " + exitList;
    }

    public static String nothingToPickup() {
        return "You reach out, but grab nothing.";
    }

    public static String pickupDialogTitle() {
        return "Pick up item";
    }

    public static String pickupDialogHeader() {
        return "Choose an item to pick up:";
    }

    public static String pickupDialogContent() {
        return "Available items:";
    }

    public static String dropDialogTitle() {
        return "Drop item";
    }

    public static String dropDialogHeader() {
        return "Choose an item to drop:";
    }

    public static String dropDialogContent() {
        return "Your inventory:";
    }
}