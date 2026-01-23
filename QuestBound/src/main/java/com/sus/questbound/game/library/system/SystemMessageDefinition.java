package com.sus.questbound.game.library.system;

import java.util.List;

public class SystemMessageDefinition {

    public Welcome welcome;
    public Movement movement;
    public Room room;
    public Inventory inventory;
    public Pickup pickup;
    public Drop drop;
    public EnemyHints enemyHints;
    public HpHints hpHints;
    public DurabilityHints durabilityHints;

    public static class Welcome {
        public List<String> welcome;
        public List<String> ambient;
    }

    public static class Movement {
        public String move;
        public String enterRoom;
    }

    public static class Room {
        public String nothingToSee;
        public String singleItem;
        public String multipleItems;
        public String noExits;
        public String singleExit;
        public String multipleExits;
        public String creepyFeeling;
        public String mysteriousShadows;
    }

    public static class Inventory {
        public String empty;
        public String singleItem;
        public String multipleItems;
        public String itemNotInInventory;
        public String inventoryFull;
    }

    public static class Pickup {
        public String askWhich;
        public String nothing;
        public String itemNotHere;
        public String pickupSuccess;
        public String pickupFailed;
        public Dialog dialog;
    }

    public static class Drop {
        public String askWhich;
        public String dropSuccess;
        public String dropFailed;
        public Dialog dialog;
    }

    public static class Dialog {
        public String title;
        public String header;
        public String content;
    }

    public static class EnemyHints {
        public String feelingWatched;
        public String shadowsInDistance;
        public String uneasySilence;
        public String footstepsNearby;
        public String strangeRustle;
    }

    public static class HpHints {
        public String fullEnergy;
        public String slightlyTired;
        public String tired;
        public String veryTired;
        public String critical;
    }

    public static class DurabilityHints {
        public String excellent;
        public String good;
        public String worn;
        public String damaged;
        public String broken;
    }
}