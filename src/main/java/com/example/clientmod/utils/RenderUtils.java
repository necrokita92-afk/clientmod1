package com.example.clientmod.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    public static void drawTextInWorld(String text, double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager rm = mc.getRenderManager();
        FontRenderer fr = mc.fontRenderer;

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(-rm.playerViewY, 0f, 1f, 0f);
        GL11.glRotatef(rm.playerViewX, 1f, 0f, 0f);
        GL11.glScalef(-0.025f, -0.025f, 0.025f);

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();

        int width = fr.getStringWidth(text) / 2;
        Gui.drawRect(-width - 1, -1, width + 1, fr.FONT_HEIGHT, 0x88000000);
        fr.drawString(text, -width, 0, 0xFFFFFFFF);

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }
}