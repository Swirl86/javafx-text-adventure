package com.sus.questbound.ui;

import com.sus.questbound.logic.GameLogicController;
import com.sus.questbound.model.Action;
import com.sus.questbound.model.Direction;
import com.sus.questbound.model.Item;
import com.sus.questbound.model.MsgType;
import com.sus.questbound.util.GMMsgHelper;
import com.sus.questbound.util.PlayerMsgHelper;
import com.sus.questbound.util.SystemMsgHelper;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record ActionController(
        GameLogicController gameLogic,
        BiConsumer<Action, Direction> executeAction,
        OutputController output
) {

    public void north() { executeAction.accept(Action.GO, Direction.NORTH); }
    public void south() { executeAction.accept(Action.GO, Direction.SOUTH); }
    public void east()  { executeAction.accept(Action.GO, Direction.EAST); }
    public void west()  { executeAction.accept(Action.GO, Direction.WEST); }

    public void pickup() {
        output.println(PlayerMsgHelper.getPlayerMsg(Action.PICKUP, null), MsgType.PLAYER);

        List<Item> pickupableItems = gameLogic.getPickupableItemsInCurrentRoom();

        if (pickupableItems.isEmpty()) {
            output.println(SystemMsgHelper.nothingToPickup(), MsgType.SYSTEM);
            output.println(GMMsgHelper.pickup(null), MsgType.GM);
            return;
        }

        output.println(SystemMsgHelper.askWhichItemToPickup(), MsgType.SYSTEM);

        List<String> itemNames = pickupableItems.stream()
                .map(Item::name)
                .collect(Collectors.toList());

        showDialog(
                SystemMsgHelper.pickupDialogTitle(),
                SystemMsgHelper.pickupDialogHeader(),
                SystemMsgHelper.pickupDialogContent(),
                itemNames,
                itemName -> {
                    Item it = gameLogic.pickupItem(itemName);
                    if (it != null) {
                        output.println(GMMsgHelper.pickup(it), MsgType.GM);
                    } else {
                        output.println(SystemMsgHelper.itemNotHere(itemName), MsgType.SYSTEM);
                    }
                }
        );
    }

    public void drop() {
        output.println(PlayerMsgHelper.getPlayerMsg(Action.DROP, null), MsgType.PLAYER);

        List<Item> inventory = gameLogic.getPlayerInventory();

        if (inventory.isEmpty()) {
            output.println(SystemMsgHelper.inventoryEmpty(), MsgType.SYSTEM);
            output.println(GMMsgHelper.drop(null), MsgType.GM);
            return;
        }

        output.println(SystemMsgHelper.askWhichItemToDrop(), MsgType.SYSTEM);

        List<String> itemNames = inventory.stream()
                .map(Item::name)
                .collect(Collectors.toList());

        showDialog(
                SystemMsgHelper.dropDialogTitle(),
                SystemMsgHelper.dropDialogHeader(),
                SystemMsgHelper.dropDialogContent(),
                itemNames,
                itemName -> {
                    Item it = gameLogic.dropItem(itemName);
                    if (it != null) {
                        output.println(GMMsgHelper.drop(it), MsgType.GM);
                    } else {
                        output.println(SystemMsgHelper.itemNotInInventory(itemName), MsgType.SYSTEM);
                    }
                }
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
            pane.lookupAll(".combo-box").forEach(box -> {
                box.setDisable(true);
                box.setOpacity(1.0);

                box.lookupAll(".arrow-button").forEach(arrow -> {
                    arrow.setVisible(false);
                    arrow.setManaged(false);
                });
            });
        }

        dialog.showAndWait().ifPresent(onSelect);
    }
}