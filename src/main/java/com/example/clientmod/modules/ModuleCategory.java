package com.example.clientmod.modules;

public enum ModuleCategory {
    COMBAT("Бой"),
    MOVEMENT("Движение"),
    RENDER("Отображение"),
    PLAYER("Игрок");

    private final String displayName;

    ModuleCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}