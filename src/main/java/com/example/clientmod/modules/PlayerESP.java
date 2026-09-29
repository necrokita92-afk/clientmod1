package com.example.clientmod.modules;

import com.example.clientmod.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class PlayerESP extends Module {

    public static Setting showBox;
    public static Setting showName;
    public static Setting showHealth;
    public static Setting showDistance;
    public static Setting tracer;
    public static Setting colorMode;

    private static final int COLOR_SELF    = 0x00FF00;
    private static final int COLOR_ENEMY   = 0xFF0000;
    private static final int COLOR_NEUTRAL = 0xFFFF00;

    public PlayerESP() {
        super("PlayerESP", "Отображение игроков сквозь стены", ModuleCategory.RENDER);
        showBox      = Setting.bool("Показывать рамку", true, this);
        showName     = Setting.bool("Показывать ник", true, this);
        showHealth   = Setting.bool("Показывать здоровье", true, this);
        showDistance = Setting.bool("Показывать дистанцию", true, this);
        tracer       = Setting.bool("Линия до игрока", false, this);
        colorMode    = Setting.options("Режим цвета", new String[]{"Единый", "По здоровью"}, 0, this);
    }

    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!isEnabled()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        float partialTicks = event.getPartialTicks();

        for (EntityPlayer target : mc.world.playerEntities) {
            if (target == mc.player) continue;
            if (target.isInvisible()) continue;

            double dx = target.lastTickPosX + (target.posX - target.lastTickPosX) * partialTicks;
            double dy = target.lastTickPosY + (target.posY - target.lastTickPosY) * partialTicks;
            double dz = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * partialTicks;

            double ex = mc.getRenderManager().viewerPosX;
            double ey = mc.getRenderManager().viewerPosY;
            double ez = mc.getRenderManager().viewerPosZ;

            double rx = dx - ex;
            double ry = dy - ey;
            double rz = dz - ez;

            AxisAlignedBB bb = target.getEntityBoundingBox()
                    .offset(-target.posX, -target.posY, -target.posZ)
                    .offset(rx, ry, rz)
                    .grow(0.1);

            int color = getColor(target);

            if (showBox.boolValue) drawBox(bb, color);
            if (tracer.boolValue) drawTracer(rx, ry + target.height / 2, rz, color);
            if (showName.boolValue || showHealth.boolValue || showDistance.boolValue) {
                drawLabel(target, rx, ry + target.height + 0.3, rz, mc);
            }
        }
    }

    private int getColor(EntityPlayer player) {
        if (colorMode.enumIndex == 0) return COLOR_NEUTRAL;
        return player.getHealth() < 10f ? COLOR_ENEMY : COLOR_SELF;
    }

    private void drawBox(AxisAlignedBB bb, int color) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(2.0f);
        GL11.glColor4f(r, g, b, 1.0f);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glEnd();

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ); GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ); GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ); GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ); GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glEnd();

        GL11.glLineWidth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private void drawTracer(double tx, double ty, double tz, int color) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(1.5f);
        GL11.glColor4f(r, g, b, 1.0f);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(tx, ty, tz);
        GL11.glEnd();

        GL11.glLineWidth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private void drawLabel(EntityPlayer target, double x, double y, double z, Minecraft mc) {
        StringBuilder sb = new StringBuilder();
        if (showName.boolValue) sb.append(target.getDisplayNameString());
        if (showHealth.boolValue) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(String.format("%.0f hp", target.getHealth()));
        }
        if (showDistance.boolValue) {
            if (sb.length() > 0) sb.append(" ");
            double dist = mc.player.getDistance(target);
            sb.append(String.format("%.1f m", dist));
        }
        if (sb.length() == 0) return;
        RenderUtils.drawTextInWorld(sb.toString(), x, y, z);
    }
}