package com.example.clientmod.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

/**
 * FastPlace / Scaffold:
 * - Сбрасывает задержку установки блоков (rightClickDelayTimer)
 * - Автоматический наклон взгляда вниз при строительстве
 * - Настраиваемая задержка между установками
 */
public class FastPlace extends Module {

    public static Setting delay;
    public static Setting autoAngle;
    public static Setting scaffold;

    /** Счётчик тиков для реализации пользовательской задержки */
    private int tickCounter = 0;

    public FastPlace() {
        super("FastPlace", "Ускоренная установка блоков", ModuleCategory.PLAYER);
        delay = Setting.integer("Задержка (тики)", 0, 0, 3, this);
        autoAngle = Setting.bool("Авто-наклон", true, this);
        scaffold = Setting.bool("Scaffold", false, this);
    }

    /** Вызывается каждый тик клиента из EventManager */
    public void onTick(Minecraft mc) {
        if (!isEnabled() || mc.player == null || mc.currentScreen != null) return;

        // 1) Автонаклон вниз (Scaffold)
        if (scaffold.boolValue && autoAngle.boolValue) {
            RayTraceResult result = mc.objectMouseOver;
            if (result == null || result.typeOfHit != RayTraceResult.Type.BLOCK) {
                mc.player.rotationPitch = 90f;
            }
        }

        // 2) Ускорение установки: если игрок зажал ПКМ — сбрасываем задержку
        if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
            tickCounter++;
            if (tickCounter >= delay.intValue) {
                tickCounter = 0;
                try {
                    // rightClickDelayTimer — ванильное поле, отвечающее за паузу между кликами.
                    // Через reflection получаем доступ и ставим 0, чтобы игра ставила блок сразу.
                    Field field = ReflectionHelper.findField(Minecraft.class, "rightClickDelayTimer", "field_71467_ac");
                    field.setAccessible(true);
                    field.setInt(mc, 0);
                } catch (Exception e) {
                    // Если поле не найдено — просто игнорируем, чтобы не крашить игру
                    e.printStackTrace();
                }
            }
        } else {
            tickCounter = 0;
        }
    }
}
