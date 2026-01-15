package com.sus.questbound.ui;

import com.sus.questbound.logic.GameLogicController;
import com.sus.questbound.model.Item;
import com.sus.questbound.util.GMHelper;
import com.sus.questbound.util.SystemMsgHelper;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DialogPane;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public record ActionController(GameLogicController gameLogic, Consumer<String> executeCommand, Consumer<String> printlnSystem) {

    // ---------- simple commands ----------
    public void north() { executeCommand.accept("north"); }
    public void south() { executeCommand.accept("south"); }
    public void east() { executeCommand.accept("east"); }
    public void west() { executeCommand.accept("west"); }
    public void look() { executeCommand.accept("look"); }
    public void inventory() { executeCommand.accept("inventory"); }
    public void hint() { executeCommand.accept("hint"); }

    // ---------- pickup ----------
    public void pickup() {
        List<Item> items = gameLogic.getCurrentRoom().getItems();

        if (items.isEmpty()) {
            printlnSystem.accept(SystemMsgHelper.nothingToPickup());
            return;
        }

        List<String> itemNames = items.stream()
                .map(Item::getName)
                .collect(Collectors.toList());

        showDialog(
                SystemMsgHelper.pickupDialogTitle(),
                SystemMsgHelper.pickupDialogHeader(),
                SystemMsgHelper.pickupDialogContent(),
                itemNames,
                itemName -> {
                    Item it = gameLogic.pickupItem(itemName);
                    if (it != null) {
                        printlnSystem.accept(GMHelper.randomPickupHint(it));
                    } else {
                        printlnSystem.accept(SystemMsgHelper.itemNotHere(itemName));
                    }
                }
        );
    }


    // ---------- drop ----------
    public void drop() {
        List<Item> inventory = gameLogic.getPlayerInventory();

        if (inventory.isEmpty()) {
            printlnSystem.accept(SystemMsgHelper.inventoryEmpty());
            return;
        }

        List<String> itemNames = inventory.stream()
                .map(Item::getName)
                .collect(Collectors.toList());

        showDialog(
                SystemMsgHelper.dropDialogTitle(),
                SystemMsgHelper.dropDialogHeader(),
                SystemMsgHelper.dropDialogContent(),
                itemNames,
                itemName -> {
                    Item it = gameLogic.dropItem(itemName);
                    if (it != null) {
                        printlnSystem.accept(GMHelper.randomDropHint(it));
                    } else {
                        printlnSystem.accept(SystemMsgHelper.itemNotInInventory(itemName));
                    }
                }
        );
    }

    // ---------- Shared dialog helper ----------
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
                Objects.requireNonNull(getClass().getResource("/com/sus/questbound/style.css"))
                        .toExternalForm()
        );

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(onSelect);
    }
}
