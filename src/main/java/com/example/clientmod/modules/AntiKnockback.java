package com.example.clientmod.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;

public class AntiKnockback extends Module {

    public static Setting percentage;

    public AntiKnockback() {
        super("AntiKnockback", "Снижение откидывания от ударов", ModuleCategory.COMBAT);
        percentage = Setting.integer("Снижение %", 100, 0, 100, this);
    }

    public void onKnockback(LivingKnockBackEvent event) {
        if (!isEnabled()) return;
        if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (player != net.minecraft.client.Minecraft.getMinecraft().player) return;

        float reduction = percentage.intValue / 100.0f;
        if (reduction >= 1.0f) {
            event.setCanceled(true);
        } else if (reduction > 0f) {
            // В Forge 1.12.2 нет метода setRatio() — гасим скорость вручную
            event.setCanceled(true);
            player.motionX *= (1.0f - reduction);
            player.motionZ *= (1.0f - reduction);
        }
    }
}