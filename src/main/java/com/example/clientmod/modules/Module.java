package com.example.clientmod.modules;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;
    private int keybind;

    private final List<Setting> settings = new ArrayList<>();

    public Module(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
        this.keybind = -1;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public ModuleCategory getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public int getKeybind() { return keybind; }
    public void setKeybind(int keybind) { this.keybind = keybind; }
    public List<Setting> getSettings() { return settings; }

    public void toggle() {
        enabled = !enabled;
        onToggle();
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;
            onToggle();
        }
    }

    protected void onToggle() {}

    protected void addSetting(Setting setting) {
        settings.add(setting);
    }

    public static class Setting {
        public final String name;
        public final SettingType type;
        public boolean boolValue;
        public int intValue;
        public int min;
        public int max;
        public final String[] options;
        public int enumIndex;
        public final Module parent;

        private Setting(String name, SettingType type, Module parent) {
            this.name = name;
            this.type = type;
            this.parent = parent;
        }

        public static Setting bool(String name, boolean defaultValue, Module module) {
            Setting s = new Setting(name, SettingType.BOOLEAN, module);
            s.boolValue = defaultValue;
            module.addSetting(s);
            return s;
        }

        public static Setting integer(String name, int defaultValue, int min, int max, Module module) {
            Setting s = new Setting(name, SettingType.INTEGER, module);
            s.intValue = defaultValue;
            s.min = min;
            s.max = max;
            module.addSetting(s);
            return s;
        }

        public static Setting options(String name, String[] options, int defaultIndex, Module module) {
            Setting s = new Setting(name, SettingType.ENUM, module);
            s.options = options;
            s.enumIndex = defaultIndex;
            module.addSetting(s);
            return s;
        }
    }

    public enum SettingType {
        BOOLEAN, INTEGER, ENUM
    }
}