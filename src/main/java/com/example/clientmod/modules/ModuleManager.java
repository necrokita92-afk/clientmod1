package com.example.clientmod.modules;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static final List<Module> modules = new ArrayList<>();

    public static FastPlace fastPlace;
    public static AntiKnockback antiKnockback;
    public static PlayerESP playerESP;

    public static void init() {
        modules.clear();

        fastPlace = new FastPlace();
        antiKnockback = new AntiKnockback();
        playerESP = new PlayerESP();

        modules.add(fastPlace);
        modules.add(antiKnockback);
        modules.add(playerESP);
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static List<Module> getModulesByCategory(ModuleCategory category) {
        List<Module> result = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == category) result.add(m);
        }
        return result;
    }
}