package com.example.clientmod.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Anti-Knockback (клиентская реализация):
 * Поскольку LivingKnockBackEvent срабатывает только на сервере,
 * на клиенте мы гасим горизонтальную скорость игрока, если она резко выросла
 * (что обычно происходит при откидывании).
 */
public class AntiKnockback extends Module {

    public static Setting percentage;

    /** Порог, при котором считаем, что игрока откинули */
    private static final double KNOCKBACK_THRESHOLD = 0.15;

    public AntiKnockback() {
        super("AntiKnockback", "Снижение откидывания от ударов", ModuleCategory.COMBAT);
        percentage = Setting.integer("Снижение %", 100, 0, 100, this);
    }

    /** Вызывается каждый тик клиента из EventManager */
    public void onTick(Minecraft mc) {
        if (!isEnabled() || mc.player == null) return;

        EntityPlayer player = mc.player;

        // Если игрок сам двигается (нажимает WASD) — не трогаем скорость
        if (player.moveForward != 0 || player.moveStrafing != 0) return;

        // Проверяем, не придали ли нам горизонтальную скорость извне
        double horizontalSpeed = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ);

        if (horizontalSpeed > KNOCKBACK_THRESHOLD) {
            float reduction = percentage.intValue / 100.0f;
            if (reduction >= 1.0f) {
                // Полное гашение горизонтальной скорости
                player.motionX = 0;
                player.motionZ = 0;
            } else if (reduction > 0f) {
                // Частичное гашение
                player.motionX *= (1.0f - reduction);
                player.motionZ *= (1.0f - reduction);
            }
        }
    }
}
