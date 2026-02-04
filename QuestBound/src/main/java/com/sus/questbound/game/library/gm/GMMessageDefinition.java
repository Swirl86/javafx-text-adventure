package com.sus.questbound.game.library.gm;

import java.util.List;

public class GMMessageDefinition {

    public List<String> attitudes;
    public List<String> unknownCommand;
    public DeadEnd deadEnd;
    public List<String> hintAttempt;
    public ItemAction pickup;
    public ItemAction drop;
    public DungeonExit dungeonExit;

    public static class DeadEnd {
        public List<String> generic;
        public String directional;
    }

    public static class ItemAction {
        public List<String> success;
        public List<String> nothing;
    }

    public static class DungeonExit {
        public List<String> withKey;
        public List<String> withoutKey;
        public List<String> keyPresenceHint;
    }
}
