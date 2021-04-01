package net.toshimichi.sushi.gui.hud;

import net.toshimichi.sushi.events.input.ClickType;
import net.toshimichi.sushi.gui.Component;
import net.toshimichi.sushi.gui.MouseStatus;
import net.toshimichi.sushi.gui.Origin;
import net.toshimichi.sushi.gui.base.BasePanelComponent;
import net.toshimichi.sushi.utils.GuiUtils;

import java.awt.Color;
import java.util.ArrayList;

public class HudEditComponent extends BasePanelComponent<CornerComponent> {

    private static final Color LINE_COLOR = new Color(100, 160, 60);
    private final HudComponent hud;
    private int holdX;
    private int holdY;
    private int currentX;
    private int currentY;
    private CornerComponent corner;

    public HudEditComponent(HudComponent hud) {
        this.hud = hud;
    }

    private void addCornerComponents(Component parent) {
        ArrayList<CornerComponent> corners = new ArrayList<>();
        for (Origin origin : Origin.values()) {
            CornerComponent component = new CornerComponent();
            component.setOrigin(origin);
            corners.add(component);
        }
        corners.forEach(c -> c.setAnchor(c.getOrigin().toAnchor().getOpposite()));
        corners.forEach(c -> c.setParent(parent));
        corners.forEach(c -> add(c, true));
    }

    @Override
    public void onRender() {
        super.onRender();
        setWidth(GuiUtils.getWidth());
        setHeight(GuiUtils.getHeight());
        for (Component component : hud) {
            if (component instanceof VirtualHudElementComponent) continue;
            drawBox(component);
            CornerComponent connecting = getConnectingCorner(component);
            CornerComponent connected = getConnectedCorner(component);
            if (connecting != null && connected != null) {
                GuiUtils.drawLine(getCenterWindowX(connecting), getCenterWindowY(connecting),
                        getCenterWindowX(connected), getCenterWindowY(connected), LINE_COLOR, 1);
            }
        }
        for (Component component : this) {
            drawBox(component);
        }
        if (corner != null) {
            GuiUtils.drawLine(holdX, holdY,
                    currentX, currentY, LINE_COLOR, 1);
        }
    }

    private void drawBox(Component component) {
        GuiUtils.drawRect(component.getWindowX(), component.getWindowY(), component.getWidth(), component.getHeight(), new Color(60, 60, 60, 100));
        GuiUtils.drawOutline(component.getWindowX(), component.getWindowY(), component.getWidth(), component.getHeight(), Color.WHITE, 1);
    }

    private int getCenterWindowX(Component component) {
        return (component.getWindowX() + component.getWindowX() + component.getWidth()) / 2;
    }

    private int getCenterWindowY(Component component) {
        return (component.getWindowY() + component.getWindowY() + component.getHeight()) / 2;
    }

    private CornerComponent getConnectingCorner(Component target) {
        CornerComponent connected = getConnectedCorner(target);
        if (connected == null) return null;
        for (CornerComponent component : this) {
            if (component.getParent().equals(target) && target.getOrigin().getOpposite().equals(component.getOrigin()))
                return component;
        }
        return null;
    }

    private CornerComponent getConnectedCorner(Component target) {
        for (CornerComponent component : this) {
            if (component.getParent().equals(target.getParent()) && target.getAnchor().equals(component.getAnchor()))
                return component;
        }
        return null;
    }

    private void calculateWindowComponent(Component target) {
        int oldWindowX = target.getWindowX();
        int oldWindowY = target.getWindowY();
        boolean left = getCenterWindowX(target) < GuiUtils.getWidth() / 2;
        boolean top = getCenterWindowY(target) < GuiUtils.getHeight() / 2;
        Origin origin;
        if (left && top) origin = Origin.TOP_LEFT;
        else if (left) origin = Origin.BOTTOM_LEFT;
        else if (top) origin = Origin.TOP_RIGHT;
        else origin = Origin.BOTTOM_RIGHT;

        target.setOrigin(origin);
        target.setAnchor(origin.toAnchor());
        target.setParent(null);
        target.setWindowX(oldWindowX);
        target.setWindowY(oldWindowY);
    }

    @Override
    public void onHold(int fromX, int fromY, int toX, int toY, ClickType type, MouseStatus status) {

        // component
        Component component = hud.getTopComponent(fromX, fromY);
        if (component != null) {
            if (status == MouseStatus.START) {
                this.holdX = fromX - component.getWindowX();
                this.holdY = fromY - component.getWindowY();
            }
            if (this.corner == null) {
                component.setWindowX(toX - holdX);
                component.setWindowY(toY - holdY);
                if (component.getParent() == null)
                    calculateWindowComponent(component);
            }
        }

        // corner box
        CornerComponent corner = getTopComponent(fromX, fromY);
        if (status == MouseStatus.START && corner != null && !(corner.getParent() instanceof VirtualHudElementComponent)) {
            this.holdX = getCenterWindowX(corner);
            this.holdY = getCenterWindowY(corner);
            this.corner = corner;
        } else if (this.corner != null && status == MouseStatus.END || status == MouseStatus.CANCEL) {
            CornerComponent connected = getTopComponent(toX, toY);
            Component target = this.corner.getParent();
            if (connected == null) {
                calculateWindowComponent(target);
            } else if (!connected.equals(this.corner)) {
                int oldWindowX = target.getWindowX();
                int oldWindowY = target.getWindowY();
                target.setOrigin(this.corner.getOrigin().getOpposite());
                target.setAnchor(connected.getAnchor());
                target.setParent(connected.getParent());
                target.setWindowX(oldWindowX);
                target.setWindowY(oldWindowY);
            }
            this.corner = null;
        }
        if (this.corner != null) {
            currentX = toX;
            currentY = toY;
        }
    }

    @Override
    public void onShow() {
        GuiUtils.lockGame();
        for (HudElementComponent element : hud)
            addCornerComponents(element);
    }

    @Override
    public void onClose() {
        GuiUtils.unlockGame();
        clear();
    }
}
