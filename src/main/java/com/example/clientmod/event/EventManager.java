package com.example.clientmod.event;

import com.example.clientmod.gui.ClickGui;
import com.example.clientmod.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class EventManager {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            if (mc.currentScreen == null) {
                mc.displayGuiScreen(new ClickGui());
            }
        }

        if (Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            for (var module : ModuleManager.getModules()) {
                if (module.getKeybind() == key) {
                    module.toggle();
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        ModuleManager.playerESP.onRenderWorld(event);
    }

    @SubscribeEvent
    public void onKnockback(LivingKnockBackEvent event) {
        ModuleManager.antiKnockback.onKnockback(event);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;
        ModuleManager.fastPlace.onTick(mc);
    }
}