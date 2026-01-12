package com.sus.questbound.util;

import com.sus.questbound.model.Item;

import java.util.List;
import java.util.Random;

public class GMHelper {

    private static final List<String> ATTITUDES = List.of(
            "whispers",
            "chuckles",
            "says",
            "smirks",
            "remarks",
            "murmurs",
            "giggles",
            "grumbles",
            "exclaims",
            "notes",
            "comments",
            "observes",
            "snickers",
            "hints",
            "sigh"
    );

    private static final Random RANDOM = new Random();

    /**
     * Returns a GM message with a random attitude.
     * Example: "GM smirks: Oops, no door that way!"
     */
    public static String randomAttitudeMessage(String msg) {
        String attitude = ATTITUDES.get(RANDOM.nextInt(ATTITUDES.size()));
        return attitude + ": " + msg;
    }

    /**
     * Returns a random message from a set of options, prefixed with a random attitude.
     */
    public static String randomHint(String... messages) {
        if (messages.length == 0) return "";
        String msg = messages[RANDOM.nextInt(messages.length)];
        return randomAttitudeMessage(msg);
    }


    /**
     * Predefined random hints for unknown commands
     */
    public static String randomUnknownCommandHint() {
        return randomHint(
                "Hmm, that was a bit unusual.\nTry 'look' to see around or 'inventory' to check your items.",
                "That command seems odd!\nMaybe you meant to look around or check your inventory?",
                "Oops, I don't understand that.\nType 'look' to explore or 'inventory' to see what you have.",
                "Interesting choice... but try 'look' or 'inventory' instead.",
                "That doesn't quite work.\nPerhaps try looking around or checking your inventory."
        );
    }

    /**
     * Predefined random hints for hitting a dead end in a room
     */
    public static String randomDeadEndHint(String availableExits) {
        return randomHint(
                "Oops, no door that way! Available exits: " + availableExits,
                "That direction seems blocked. You could try: " + availableExits,
                "Hmm, nothing but walls that way. Your options: " + availableExits
        );
    }

    public static String deadEndMessage(String direction) {
        return "You try to go " + direction + " but hit a dead end. " +
                "It's quiet here—maybe look around or head back.";
    }

    public static String randomHintAttempt() {
        return randomHint(
                "Let me see... maybe the exits are nearby.",
                "Summoning the knowledge of the room...",
                "Hmm, I think there are ways out here.",
                "Consulting the mystical map...",
                "Let me whisper some guidance about the exits."
        );
    }

    // ---------- Messages for items ----------
    public static String randomPickupHint(Item item) {
        if (item == null) return randomHint("You try to pick something up, but nothing happens.");
        return randomHint(
                "Ah, I see you've found " + item.getName() + ". Picking it up seems wise.",
                "Carefully lifting the " + item.getName() + "... Excellent choice!",
                "You reach for the " + item.getName() + " and add it to your belongings.",
                "The " + item.getName() + " is now in your inventory. Well done!",
                "You pick up the " + item.getName() + ". It might come in handy."
        );
    }

    public static String randomDropHint(Item item) {
        if (item == null) return randomHint("You try to drop something, but you hold nothing.");
        return randomHint(
                "You gently set down the " + item.getName() + ". It rests where you leave it.",
                "Dropping the " + item.getName() + "... hope you won't need it soon!",
                "The " + item.getName() + " is no longer in your inventory. Handle with care!",
                "You let go of the " + item.getName() + ". Perhaps someone else will find it.",
                "You drop the " + item.getName() + ". It hits the ground with a soft thud."
        );
    }

    /* TODO add more categories if needed, e.g., randomItemHint(), randomEnemyEncounter(), etc. */
}

