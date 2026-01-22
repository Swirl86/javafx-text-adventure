package com.sus.questbound.game.library;

import java.util.List;

public class GMMessageDefinition {

    public List<String> attitudes;
    public List<String> unknownCommand;
    public DeadEnd deadEnd;
    public List<String> hintAttempt;
    public ItemAction pickup;
    public ItemAction drop;

    public static class DeadEnd {
        public List<String> generic;
        public String directional;
    }

    public static class ItemAction {
        public List<String> success;
        public List<String> nothing;
    }
}
