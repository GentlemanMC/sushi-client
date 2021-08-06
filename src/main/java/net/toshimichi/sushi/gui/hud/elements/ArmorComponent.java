package net.toshimichi.sushi.gui.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.toshimichi.sushi.config.Configuration;
import net.toshimichi.sushi.config.Configurations;
import net.toshimichi.sushi.config.data.DoubleRange;
import net.toshimichi.sushi.gui.hud.BaseHudElementComponent;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class ArmorComponent extends BaseHudElementComponent {

    private static final int MARGIN = 16;

    private final Configuration<Boolean> vertical;
    private final Configuration<DoubleRange> scale;

    public ArmorComponent(Configurations configurations, String id, String name) {
        super(configurations, id, name);
        vertical = getConfiguration("vertical", "vertical", null, Boolean.class, true);
        scale = getConfiguration("scale", "Scale", null, DoubleRange.class, new DoubleRange(3, 10, 1, 0.1, 1));
    }

    @Override
    public void onRender() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) return;
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(player.inventory.armorInventory.get(3));
        items.add(player.inventory.armorInventory.get(2));
        items.add(player.inventory.armorInventory.get(1));
        items.add(player.inventory.armorInventory.get(0));

        RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
        double s = scale.getValue().getCurrent();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glPushMatrix();
        glTranslated(getWindowX(), getWindowY(), 0);
        glScaled(s, s, 0);
        double x = 0;
        double y = 0;
        for (ItemStack item : items) {
            RenderHelper.enableGUIStandardItemLighting();
            renderer.renderItemAndEffectIntoGUI(item, (int) x, (int) y);
            renderer.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, item, (int) x, (int) y);
            RenderHelper.disableStandardItemLighting();
            if (vertical.getValue()) y += MARGIN;
            else x += MARGIN;
        }
        glPopMatrix();
    }

    @Override
    public void onRelocate() {
        double base = scale.getValue().getCurrent() * MARGIN;
        if (vertical.getValue()) {
            setWidth(base);
            setHeight(base * 4);
        } else {
            setWidth(base * 4);
            setHeight(base);
        }
    }
}