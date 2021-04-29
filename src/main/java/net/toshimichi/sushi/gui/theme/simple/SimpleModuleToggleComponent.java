package net.toshimichi.sushi.gui.theme.simple;

import net.toshimichi.sushi.events.input.ClickType;
import net.toshimichi.sushi.gui.MouseStatus;
import net.toshimichi.sushi.gui.SmoothCollapseComponent;
import net.toshimichi.sushi.gui.theme.ThemeConstants;
import net.toshimichi.sushi.modules.Module;
import net.toshimichi.sushi.utils.GuiUtils;

import java.awt.Color;

public class SimpleModuleToggleComponent extends SimpleToggleComponent<Module> {

    private final ThemeConstants constants;
    private final Module module;
    private final SmoothCollapseComponent<?> component;

    public SimpleModuleToggleComponent(ThemeConstants constants, Module module, SmoothCollapseComponent<?> component) {
        super(constants, module.isEnabled());
        this.constants = constants;
        this.module = module;
        this.component = component;
        setHeight(16);
    }

    @Override
    protected void onChange(boolean newValue) {
        module.setEnabled(newValue);
    }

    @Override
    public void onRender() {
        GuiUtils.prepareArea(this);
        setToggled(module.isEnabled());
        super.onRender();
        GuiUtils.prepareText(module.getName(), constants.font.getValue(), constants.textColor.getValue(), 10, true)
                .draw(getWindowX() + 5, getWindowY() + 2);
        int x = getWindowX() + getWidth() - 10;
        int y = getWindowY() + getHeight() / 2;
        int midY = (int) (getWindowY() + getHeight() / 2 + (component.getProgress() - 0.5) * getHeight() * 1 / 3);
        if (component.getProgress() == 1) midY++;
        GuiUtils.drawLine(x, y, x + 3, midY, Color.WHITE, 2);
        GuiUtils.drawLine(x + 3, midY, x + 6, y, Color.WHITE, 2);
        GuiUtils.releaseArea();
    }

    @Override
    public void onClick(int x, int y, ClickType type) {
        super.onClick(x, y, type);
        if (type != ClickType.RIGHT) return;
        component.setCollapsed(!component.isCollapsed());
    }

    @Override
    public void onHold(int fromX, int fromY, int toX, int toY, ClickType type, MouseStatus status) {
        super.onHold(fromX, fromY, toX, toY, type, status);
        if (status != MouseStatus.END || type != ClickType.RIGHT) return;
        component.setCollapsed(!component.isCollapsed());
    }

    @Override
    public Module getValue() {
        return module;
    }
}
