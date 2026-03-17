package com.sus.questbound.game.engine;

import com.sus.questbound.game.model.ItemTags;
import com.sus.questbound.game.model.Player;
import com.sus.questbound.game.model.Room;
import com.sus.questbound.util.GMMsgHelper;

import java.util.Optional;

/**
 * Handles dynamic game events triggered by player actions,
 * such as entering rooms with special conditions or items.
 * Returns optional GM messages that the UI may display.
 */
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
