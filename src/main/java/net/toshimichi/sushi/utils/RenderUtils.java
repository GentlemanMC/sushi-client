package net.toshimichi.sushi.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.AxisAlignedBB;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {

    public static void prepare3D() {
        glPushMatrix();
        glPushAttrib(GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        double dx = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double dy = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double dz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        glTranslated(-dx, -dy, -dz);
    }

    public static void release3D() {
        glPopMatrix();
        glPopAttrib();
    }

    public static void drawOutline(AxisAlignedBB box, Color color, double width) {
        glLineWidth((float) width);
        GuiUtils.setColor(color);
        prepare3D();
        glBegin(GL_LINE_STRIP);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glEnd();
        release3D();
    }

    public static void drawFilled(AxisAlignedBB box, Color color) {
        GuiUtils.setColor(color);
        prepare3D();
        glBegin(GL_QUAD_STRIP);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glEnd();
        glBegin(GL_QUAD_STRIP);
        glVertex3d(box.minX, box.minY, box.minZ);
        glVertex3d(box.minX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.minZ);
        glVertex3d(box.maxX, box.maxY, box.minZ);
        glVertex3d(box.maxX, box.minY, box.maxZ);
        glVertex3d(box.maxX, box.maxY, box.maxZ);
        glVertex3d(box.minX, box.minY, box.maxZ);
        glVertex3d(box.minX, box.maxY, box.maxZ);
        glEnd();
        release3D();
    }
}
