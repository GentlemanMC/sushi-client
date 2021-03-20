package net.toshimichi.sushi.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

public class GuiUtils {

    private static boolean locked;

    public static void lockGame() {
        if (isGameLocked()) return;
        Minecraft.getMinecraft().displayGuiScreen(new LockGuiScreen(Minecraft.getMinecraft().currentScreen));
        locked = true;
        Minecraft.getMinecraft().setIngameNotInFocus();
    }

    public static void unlockGame() {
        if (!isGameLocked()) return;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof LockGuiScreen)
            ((LockGuiScreen) screen).close();
        if (Minecraft.getMinecraft().currentScreen instanceof LockGuiScreen) return; // still locked!
        locked = false;
        Minecraft.getMinecraft().setIngameFocus();
    }

    public static boolean isGameLocked() {
        return locked;
    }

    private static void prepare2D() {
        glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT);

        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
    }

    private static void release2D() {
        glPopAttrib();
    }

    private static void setColor(Color color) {
        glColor4d((double) color.getRed() / 255, (double) color.getGreen() / 255, (double) color.getBlue() / 255, (double) color.getAlpha() / 255);
    }

    public static void drawText(int x, int y, String text, String font, Color color, int pts, boolean shadow) {
        setColor(color);
        if (font == null) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            int oldHeight = renderer.FONT_HEIGHT;
            renderer.FONT_HEIGHT = pts;
            Minecraft.getMinecraft().fontRenderer.drawString(text, x, y, color.getRGB(), shadow);
            renderer.FONT_HEIGHT = oldHeight;
            return;
        }

        // TODO: support other fonts
    }

    public static void drawLine(int x1, int y1, int x2, int y2, Color color, double width) {
        prepare2D();

        glLineWidth((float) width);
        setColor(color);
        glBegin(GL_LINES);
        glVertex2i(x1, y1);
        glVertex2i(x2, y2);
        glEnd();

        release2D();
    }

    public static void drawRect(int x, int y, int width, int height, Color color) {
        prepare2D();

        setColor(color);
        glRectd(x, y, x + width, y + height);

        release2D();
    }

    public static void drawOutline(int x, int y, int width, int height, Color color, int pts) {
        prepare2D();

        glLineWidth(pts);
        setColor(color);
        glBegin(GL_LINE_STRIP);
        glVertex2i(x, y);
        glVertex2d(x + width, y);
        glVertex2d(x + width, y + height);
        glEnd();

        release2D();
    }

}
