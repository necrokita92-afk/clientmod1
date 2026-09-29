package com.example.clientmod.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

/**
 * Tracers — линии от прицела игрока до других игроков.
 * Цвет меняется в зависимости от расстояния.
 */
public class Tracers extends Module {

    public static Setting maxDistance;
    public static Setting thickness;

    public Tracers() {
        super("Tracers", "Линии до ближайших игроков", ModuleCategory.RENDER);
        maxDistance = Setting.integer("Макс. дистанция", 64, 8, 256, this);
        thickness   = Setting.integer("Толщина линии x10", 15, 5, 50, this);
    }

    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        float partialTicks = event.getPartialTicks();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(thickness.intValue / 10.0f);

        GL11.glBegin(GL11.GL_LINES);

        double ex = mc.getRenderManager().viewerPosX;
        double ey = mc.getRenderManager().viewerPosY;
        double ez = mc.getRenderManager().viewerPosZ;

        for (EntityPlayer target : mc.world.playerEntities) {
            if (target == mc.player) continue;

            double dist = mc.player.getDistance(target);
            if (dist > maxDistance.intValue) continue;

            double tx = target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks - ex;
            double ty = target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks - ey;
            double tz = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks - ez;

            // Цвет зависит от дистанции: близко — красный, далеко — зелёный
            float ratio = (float) Math.min(1.0, dist / maxDistance.intValue);
            float r = ratio;             // 0..1
            float g = 1.0f - ratio;      // 1..0
            float b = 0.1f;
            GL11.glColor4f(r, g, b, 1.0f);

            GL11.glVertex3d(0, 0, 0);
            GL11.glVertex3d(tx, ty + target.height / 2, tz);
        }

        GL11.glEnd();

        GL11.glLineWidth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}