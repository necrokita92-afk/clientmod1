package com.example.clientmod.config;

import com.example.clientmod.modules.Module;
import com.example.clientmod.modules.ModuleManager;

import java.io.*;
import java.util.Properties;

public class ConfigManager {

    private static final String FILE_PATH = "config/clientmod.properties";
    private static final Properties props = new Properties();

    public static void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file)) {
            props.load(fis);

            for (Module module : ModuleManager.getModules()) {
                String key = "module." + module.getName();
                boolean enabled = Boolean.parseBoolean(props.getProperty(key + ".enabled", "false"));
                module.setEnabled(enabled);

                int keybind = Integer.parseInt(props.getProperty(key + ".keybind", "-1"));
                module.setKeybind(keybind);

                for (Module.Setting setting : module.getSettings()) {
                    String sk = key + ".setting." + setting.name;
                    switch (setting.type) {
                        case BOOLEAN:
                            setting.boolValue = Boolean.parseBoolean(props.getProperty(sk, String.valueOf(setting.boolValue)));
                            break;
                        case INTEGER:
                            setting.intValue = Integer.parseInt(props.getProperty(sk, String.valueOf(setting.intValue)));
                            break;
                        case ENUM:
                            setting.enumIndex = Integer.parseInt(props.getProperty(sk, String.valueOf(setting.enumIndex)));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        for (Module module : ModuleManager.getModules()) {
            String key = "module." + module.getName();
            props.setProperty(key + ".enabled", String.valueOf(module.isEnabled()));
            props.setProperty(key + ".keybind", String.valueOf(module.getKeybind()));

            for (Module.Setting setting : module.getSettings()) {
                String sk = key + ".setting." + setting.name;
                switch (setting.type) {
                    case BOOLEAN:
                        props.setProperty(sk, String.valueOf(setting.boolValue));
                        break;
                    case INTEGER:
                        props.setProperty(sk, String.valueOf(setting.intValue));
                        break;
                    case ENUM:
                        props.setProperty(sk, String.valueOf(setting.enumIndex));
                        break;
                }
            }
        }

        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            props.store(fos, "ClientMod Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}