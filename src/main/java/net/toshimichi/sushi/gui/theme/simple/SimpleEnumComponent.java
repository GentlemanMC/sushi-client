package net.toshimichi.sushi.gui.theme.simple;

import net.toshimichi.sushi.config.data.Named;
import net.toshimichi.sushi.events.input.ClickType;
import net.toshimichi.sushi.gui.MouseStatus;
import net.toshimichi.sushi.gui.base.BaseComponent;
import net.toshimichi.sushi.gui.theme.ThemeConstants;
import net.toshimichi.sushi.utils.GuiUtils;
import net.toshimichi.sushi.utils.TextPreview;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class SimpleEnumComponent<T extends Named> extends BaseComponent {

    private final ThemeConstants constants;
    private final String text;
    private final T[] values;
    private int counter;

    public SimpleEnumComponent(ThemeConstants constants, String text, T init, Class<T> tClass) {
        this.constants = constants;
        this.text = text;
        this.values = values(tClass);
        counter = Arrays.asList(values).indexOf(init);
        setHeight(14);
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] values(Class<?> c) {
        try {
            return (T[]) c.getMethod("values").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();

            return (T[]) new Named[0];
        }
    }

    @Override
    public void onRender() {
        GuiUtils.drawRect(getWindowX(), getWindowY(), getWidth(), getHeight(), constants.backgroundColor.getValue());
        GuiUtils.prepareText(text, constants.font.getValue(), constants.textColor.getValue(), 9, true)
                .draw(getWindowX() + 1, getWindowY() + 2);
        TextPreview preview = GuiUtils.prepareText(getNamed(counter).getName(), constants.font.getValue(), constants.textColor.getValue(), 9, true);
        preview.draw(getWindowX() + getWidth() - preview.getWidth() - 1, getWindowY() + 2);
    }

    @Override
    public void onClick(int x, int y, ClickType type) {
        onChange(getNamed(counter++));
    }

    @Override
    public void onHold(int fromX, int fromY, int toX, int toY, ClickType type, MouseStatus status) {
        if (status != MouseStatus.END) return;
        onChange(getNamed(counter++));
    }

    private T getNamed(int counter) {
        return values[counter % values.length];
    }

    protected void onChange(T newValue) {
    }
}
