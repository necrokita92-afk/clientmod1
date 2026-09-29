package com.example.clientmod.gui.components;

import com.example.clientmod.modules.Module;
import com.example.clientmod.modules.ModuleCategory;
import com.example.clientmod.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.util.List;

public class CategoryPanel {

    private final ModuleCategory category;
    private int x, y;
    private final int width = 120;
    private final int headerHeight = 16;
    private final int moduleHeight = 16;

    private boolean dragging;
    private int dragX, dragY;

    private final List<Module> modules;

    public CategoryPanel(ModuleCategory category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.modules = ModuleManager.getModulesByCategory(category);
    }

    public void draw(int mouseX, int mouseY) {
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        Gui.drawRect(x, y, x + width, y + headerHeight + modules.size() * moduleHeight, 0xCC1E1E1E);
        Gui.drawRect(x, y, x + width, y + headerHeight, 0xFF2D2D2D);
        fr.drawStringWithShadow(category.getDisplayName(), x + 4, y + 4, 0xFFFFFFFF);

        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int my = y + headerHeight + i * moduleHeight;
            int bg = module.isEnabled() ? 0xAA00AA00 : 0xAA333333;
            Gui.drawRect(x + 2, my + 1, x + width - 2, my + moduleHeight - 1, bg);
            int textColor = module.isEnabled() ? 0xFF00FF00 : 0xFFAAAAAA;
            fr.drawStringWithShadow(module.getName(), x + 6, my + 4, textColor);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + headerHeight) {
            if (button == 0) {
                dragging = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            }
            return;
        }

        for (int i = 0; i < modules.size(); i++) {
            int my = y + headerHeight + i * moduleHeight;
            if (mouseX >= x + 2 && mouseX <= x + width - 2 && mouseY >= my + 1 && mouseY <= my + moduleHeight - 1) {
                if (button == 0) {
                    modules.get(i).toggle();
                }
                return;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int button) {
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
    }
}