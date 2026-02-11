package com.sus.questbound.logic;

import com.sus.questbound.model.ItemTags;
import com.sus.questbound.model.Player;
import com.sus.questbound.model.Room;
import com.sus.questbound.util.GMMsgHelper;

import java.util.Optional;

public class GameEventService {

    public Optional<String> onEnterRoom(Room room, Player player) {

        if (room.isDungeonExit()) {
            if (player.hasItemWithTag(ItemTags.FINAL_KEY.id())) {
                // TODO: expand endgame
                return Optional.of(GMMsgHelper.dungeonExitWithKey());
            }
            return Optional.of(GMMsgHelper.dungeonExitWithoutKey());
        }

        if (room.containsItemWithTag(ItemTags.FINAL_KEY.id())) {
            return Optional.of(GMMsgHelper.finalKeyPresenceHint());
        }

        return Optional.empty();
    }
}
