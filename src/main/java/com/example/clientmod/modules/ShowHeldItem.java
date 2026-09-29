package com.example.clientmod.modules;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * ShowHeldItem — утилитарный модуль.
 * Активирует отображение предмета в руке у цели в ESP.
 */
public class ShowHeldItem extends Module {

    public static Setting showMainHand;
    public static Setting showOffHand;

    public ShowHeldItem() {
        super("ShowHeldItem", "Показывать предмет в руке цели (в ESP)", ModuleCategory.RENDER);
        showMainHand = Setting.bool("Правая рука", true, this);
        showOffHand  = Setting.bool("Левая рука", false, this);
    }

    /** Возвращает строку с предметами цели, если модуль включён */
    public String getHeldItemString(EntityPlayer target) {
        if (!isEnabled()) return "";

        StringBuilder sb = new StringBuilder();

        if (showMainHand.boolValue) {
            ItemStack main = target.getHeldItem(EnumHand.MAIN_HAND);
            if (!main.isEmpty()) {
                sb.append(main.getDisplayName());
                // Показываем количество, если больше 1
                if (main.getCount() > 1) sb.append(" x").append(main.getCount());
            }
        }

        if (showOffHand.boolValue) {
            ItemStack off = target.getHeldItem(EnumHand.OFF_HAND);
            if (!off.isEmpty()) {
                if (sb.length() > 0) sb.append(" | ");
                sb.append(off.getDisplayName());
            }
        }

        return sb.toString();
    }
}