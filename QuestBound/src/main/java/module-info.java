module com.sus.questbound {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    // --- FXML controllers ---
    opens com.sus.questbound.ui to javafx.fxml;

    // --- JSON data loading ---
    opens com.sus.questbound.game.data.gm to com.fasterxml.jackson.databind;
    opens com.sus.questbound.game.data.item to com.fasterxml.jackson.databind;
    opens com.sus.questbound.game.data.player to com.fasterxml.jackson.databind;
    opens com.sus.questbound.game.data.room to com.fasterxml.jackson.databind;
    opens com.sus.questbound.game.data.system to com.fasterxml.jackson.databind;

    // --- Model classes used by Jackson ---
    opens com.sus.questbound.game.model to com.fasterxml.jackson.databind;

    // --- Public API for other modules (UI uses these) ---
    exports com.sus.questbound;
    exports com.sus.questbound.ui;
    exports com.sus.questbound.ui.view;
    exports com.sus.questbound.game;
    exports com.sus.questbound.game.engine;
    exports com.sus.questbound.game.world;
    exports com.sus.questbound.game.world.generator;
    exports com.sus.questbound.game.world.config;
    exports com.sus.questbound.game.model;
}