package net.toshimichi.sushi.utils;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class TtfTextPreview implements TextPreview {

    private static final File FONT_DIR = new File(System.getenv("SystemDrive") + "\\Windows\\Fonts");
    private static final double FONT_SCALE = 2;
    private static final ArrayList<FontData> cachedFonts = new ArrayList<>();

    private final String text;
    private final UnicodeFont font;
    private final boolean shadow;
    private Color color;

    public TtfTextPreview(String text, UnicodeFont font, Color color, boolean shadow) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.shadow = shadow;
    }

    @Override
    public double getWidth() {
        return 1D / GuiUtils.getScaleFactor() * font.getWidth(text);
    }

    @Override
    public double getHeight() {
        return 1D / GuiUtils.getScaleFactor() * font.getHeight(text);
    }

    @Override
    public void draw(double x, double y) {
        glPushAttrib(GL_ENABLE_BIT | GL_TEXTURE_BIT);

        glMatrixMode(GL_MODELVIEW);
        glPushMatrix();
        glScaled(1 / FONT_SCALE, 1 / FONT_SCALE, 1 / FONT_SCALE);
        glTranslated(FONT_SCALE * x, FONT_SCALE * y, 0);

        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        if (color == null) {
            double h = System.currentTimeMillis() / 10000D - System.currentTimeMillis() / 10000;
            color = Color.getHSBColor((float) (y / 10000D + h), 1, 1);
        }
        org.newdawn.slick.Color c = new org.newdawn.slick.Color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) color.getAlpha() / 255);
        if (shadow) {
            org.newdawn.slick.Color shadowColor = new org.newdawn.slick.Color(0.39F, 0.39F, 0.39F, (float) color.getAlpha() / 255);
            font.drawString(1, 1, text, shadowColor);
        }
        font.drawString(0, 0, text, c);

        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glPopAttrib();
    }

    @SuppressWarnings("unchecked")
    public static TtfTextPreview newTextPreview(String text, String font, Color color, int pts, boolean shadow) {
        // load from cached fonts
        for (FontData ttf : cachedFonts) {
            if (ttf.fontName.equals(font) && ttf.size == pts) {
                return new TtfTextPreview(text, ttf.font, color, shadow);
            }
        }

        // load TTF
        File ttfFile = new File(FONT_DIR, font + ".ttf");
        if (!ttfFile.exists()) return null;
        try {
            UnicodeFont unicode = new UnicodeFont(Font.createFont(Font.TRUETYPE_FONT, ttfFile).deriveFont((float) (pts * FONT_SCALE)));
            FontData fontData = new FontData(font, unicode, pts);
            unicode.getEffects().add(new ColorEffect(Color.WHITE));
            unicode.addAsciiGlyphs();
            unicode.loadGlyphs();
            cachedFonts.add(fontData);
            return new TtfTextPreview(text, unicode, color, shadow);
        } catch (FontFormatException | IOException | SlickException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class FontData {
        String fontName;
        UnicodeFont font;
        int size;

        FontData(String fontName, UnicodeFont font, int size) {
            this.fontName = fontName;
            this.font = font;
            this.size = size;
        }
    }
}
