package com.sus.questbound.game.library;

import java.util.List;

public class PlayerMessageDefinition {

    public Directions directions;
    public List<String> look;
    public List<String> inventory;
    public List<String> go;
    public List<String> hint;
    public List<String> pickup;
    public List<String> drop;
    public List<String> defaultMessages;

    public static class Directions {
        public List<String> north;
        public List<String> south;
        public List<String> east;
        public List<String> west;
        public List<String> up;
        public List<String> down;
    }
}

