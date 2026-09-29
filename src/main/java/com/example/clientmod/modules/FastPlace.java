package com.example.clientmod.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;

public class FastPlace extends Module {

    public static Setting delay;
    public static Setting autoAngle;
    public static Setting scaffold;

    public FastPlace() {
        super("FastPlace", "Ускоренная установка блоков", ModuleCategory.PLAYER);
        delay = Setting.integer("Задержка (тики)", 0, 0, 3, this);
        autoAngle = Setting.bool("Авто-наклон", true, this);
        scaffold = Setting.bool("Scaffold", false, this);
    }

    public void onTick(Minecraft mc) {
        if (!isEnabled() || mc.player == null || mc.currentScreen != null) return;

        if (scaffold.boolValue && autoAngle.boolValue) {
            RayTraceResult result = mc.objectMouseOver;
            if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK) {
                mc.player.rotationPitch = 90f;
            }
        }
    }
}