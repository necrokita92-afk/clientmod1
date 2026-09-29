package com.example.clientmod.modules;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Scaffold — автоматическая установка блоков под игроком.
 * Каждый тик проверяет блок под ногами. Если он воздушный — ставит
 * блок из текущего слота, если там есть блок (ItemBlock).
 */
public class Scaffold extends Module {

    public static Setting autoDisableOnGround;
    public static Setting rotate;

    public Scaffold() {
        super("Scaffold", "Автоматическая установка блоков под ногами", ModuleCategory.PLAYER);
        autoDisableOnGround = Setting.bool("Выключать на земле", false, this);
        rotate             = Setting.bool("Поворачивать взгляд", true, this);
    }

    /** Вызывается каждый тик клиента */
    public void onTick(Minecraft mc) {
        if (!isEnabled() || mc.player == null || mc.world == null) return;
        if (mc.currentScreen != null) return;

        EntityPlayer player = mc.player;

        // Опционально: автоматически выключить, если игрок на земле
        if (autoDisableOnGround.boolValue && player.onGround) {
            setEnabled(false);
            return;
        }

        // Проверяем, что в руке держим блок
        ItemStack held = player.getHeldItem(EnumHand.MAIN_HAND);
        if (held.isEmpty() || !(held.getItem() instanceof ItemBlock)) return;

        // Позиция блока под ногами
        BlockPos below = new BlockPos(player.posX, player.posY - 1, player.posZ);
        Block belowBlock = mc.world.getBlockState(below).getBlock();

        // Если под ногами не воздух — ничего не делаем
        if (belowBlock != Blocks.AIR) return;

        // Убеждаемся, что блок можно поставить (не в листве/жидкости)
        if (!mc.world.isAirBlock(below)) return;

        // Наводим "прицел" на этот блок (имитация)
        if (rotate.boolValue) {
            // Простой расчёт углов, чтобы клиент отправил нужный пакет
            Vec3d eye = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            double dx = (below.getX() + 0.5) - eye.x;
            double dy = (below.getY() + 1.0) - eye.y;
            double dz = (below.getZ() + 0.5) - eye.z;

            double dist = Math.sqrt(dx * dx + dz * dz);
            float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
            float pitch = (float) -Math.toDegrees(Math.atan2(dy, dist));

            player.rotationYaw = yaw;
            player.rotationPitch = pitch;
        }

        // Пытаемся поставить блок через vanilla-логику
        // (через PlayerControllerMP.processRightClickBlock)
        BlockPos neighbor = below.up();  // блок над пустотой — наша опора
        EnumFacing facing = EnumFacing.DOWN;

        mc.playerController.processRightClickBlock(
                player,
                mc.world,
                neighbor,
                facing,
                new Vec3d(
                        below.getX() + 0.5,
                        below.getY() + 0.5,
                        below.getZ() + 0.5
                ),
                EnumHand.MAIN_HAND
        );
        // Размах рукой
        player.swingArm(EnumHand.MAIN_HAND);
    }
}