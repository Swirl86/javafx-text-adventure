package com.sus.questbound.ui;

import com.sus.questbound.game.model.Action;
import com.sus.questbound.game.model.Direction;
import com.sus.questbound.game.model.Item;
import com.sus.questbound.game.model.MsgType;
import com.sus.questbound.util.*;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record PlayerActionController(
        GameEngine engine,
        BiConsumer<Action, Direction> executeAction,
        GameOutputController output
) {

    public void north() { executeAction.accept(Action.GO, Direction.NORTH); }
    public void south() { executeAction.accept(Action.GO, Direction.SOUTH); }
    public void east()  { executeAction.accept(Action.GO, Direction.EAST); }
    public void west()  { executeAction.accept(Action.GO, Direction.WEST); }

    public void pickup() {
        output.println(PlayerMsgHelper.getPlayerMsg(Action.PICKUP, null), MsgType.PLAYER);

        List<Item> pickupableItems = engine.getPickupableItems();

        if (pickupableItems.isEmpty()) {
            output.println(SystemMsgHelper.nothingToPickup(), MsgType.SYSTEM);
            output.println(GMMsgHelper.pickup(null), MsgType.GM);
            return;
        }

        output.println(SystemMsgHelper.askWhichItemToPickup(), MsgType.SYSTEM);

        List<String> itemNames = CollectionUtil.mapToList(pickupableItems, Item::name);

        showDialog(
                SystemMsgHelper.pickupDialogTitle(),
                SystemMsgHelper.pickupDialogHeader(),
                SystemMsgHelper.pickupDialogContent(),
                itemNames,
                itemName -> engine.pickupItem(itemName)
                        .ifPresentOrElse(
                                it -> output.println(GMMsgHelper.pickup(it), MsgType.GM),
                                () -> output.println(SystemMsgHelper.itemNotHere(itemName), MsgType.SYSTEM)
                        )

        );
    }

    public void drop() {
        output.println(PlayerMsgHelper.getPlayerMsg(Action.DROP, null), MsgType.PLAYER);

        List<Item> inventory = engine.getPlayerInventory();

        if (inventory.isEmpty()) {
            output.println(SystemMsgHelper.inventoryEmpty(), MsgType.SYSTEM);
            output.println(GMMsgHelper.drop(null), MsgType.GM);
            return;
        }

        output.println(SystemMsgHelper.askWhichItemToDrop(), MsgType.SYSTEM);

        List<String> itemNames = CollectionUtil.mapToList(inventory, Item::name);

        showDialog(
                SystemMsgHelper.dropDialogTitle(),
                SystemMsgHelper.dropDialogHeader(),
                SystemMsgHelper.dropDialogContent(),
                itemNames,
                itemName -> engine.dropItem(itemName)
                        .ifPresentOrElse(
                                it -> output.println(GMMsgHelper.drop(it), MsgType.GM),
                                () -> output.println(SystemMsgHelper.itemNotInInventory(itemName), MsgType.SYSTEM)
                        )
        );
    }

    private void showDialog(
            String title,
            String header,
            String content,
            List<String> options,
            Consumer<String> onSelect
    ) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        DialogPane pane = dialog.getDialogPane();
        pane.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource("/com/sus/questbound/style.css")
                ).toExternalForm()
        );

        if (options.size() == 1) {
            pane.lookupAll("." + Css.COMBO).forEach(box -> {
                box.setDisable(true);
                box.setOpacity(1.0);

                box.lookupAll("." + Css.ARROW_BUTTON).forEach(arrow -> {
                    arrow.setVisible(false);
                    arrow.setManaged(false);
                });
            });
        }

        dialog.showAndWait().ifPresent(onSelect);
    }
}