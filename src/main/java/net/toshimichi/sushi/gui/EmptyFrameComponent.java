package net.toshimichi.sushi.gui;

import net.toshimichi.sushi.events.input.ClickType;
import net.toshimichi.sushi.gui.base.BaseFrameComponent;

public class EmptyFrameComponent<T extends Component> extends BaseFrameComponent<T> {

    private final T component;

    public EmptyFrameComponent(T component) {
        this.component = component;
    }

    @Override
    public T getValue() {
        return component;
    }

    @Override
    public int getX() {
        return component.getX();
    }

    @Override
    public int getY() {
        return component.getY();
    }

    @Override
    public void setX(int x) {
        component.setX(x);
    }

    @Override
    public void setY(int y) {
        component.setY(y);
    }

    @Override
    public int getWidth() {
        return component.getWidth();
    }

    @Override
    public int getHeight() {
        return component.getHeight();
    }

    @Override
    public void setWidth(int width) {
        component.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        component.setHeight(height);
    }

    @Override
    public int getWindowX() {
        return component.getWindowX();
    }

    @Override
    public int getWindowY() {
        return component.getWindowY();
    }

    @Override
    public void setWindowX(int x) {
        component.setWindowX(x);
    }

    @Override
    public void setWindowY(int y) {
        component.setWindowY(y);
    }

    @Override
    public Anchor getAnchor() {
        return component.getAnchor();
    }

    @Override
    public void setAnchor(Anchor anchor) {
        component.setAnchor(anchor);
    }

    @Override
    public Component getParent() {
        return component.getParent();
    }

    @Override
    public void setParent(Component component) {
        this.component.setParent(component);
    }

    @Override
    public boolean isFocused() {
        return component.isFocused();
    }

    @Override
    public void setFocused(boolean focused) {
        component.setFocused(focused);
    }

    @Override
    public Insets getMargin() {
        return component.getMargin();
    }

    @Override
    public void setMargin(Insets margin) {
        super.setMargin(margin);
    }

    @Override
    public Origin getOrigin() {
        return super.getOrigin();
    }

    @Override
    public void setOrigin(Origin origin) {
        super.setOrigin(origin);
    }

    @Override
    public ComponentContext<?> getContext() {
        return super.getContext();
    }

    @Override
    public void setContext(ComponentContext<?> context) {
        super.setContext(context);
    }

    @Override
    public boolean isClosed() {
        return super.isClosed();
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public void onClick(int x, int y, ClickType type) {
        component.onClick(x, y, type);
    }

    @Override
    public void onHold(int fromX, int fromY, int toX, int toY, ClickType type, MouseStatus status) {
        component.onHold(fromX, fromY, toX, toY, type, status);
    }

    @Override
    public void onHover(int x, int y) {
        component.onHover(x, y);
    }

    @Override
    public void onScroll(int deltaX, int deltaY, ClickType type) {
        component.onScroll(deltaX, deltaY, type);
    }

    @Override
    public boolean onKeyPressed(int keyCode, char key) {
        return component.onKeyPressed(keyCode, key);
    }

    @Override
    public boolean onKeyReleased(int keyCode) {
        return component.onKeyReleased(keyCode);
    }

    @Override
    public void onRender() {
        component.onRender();
    }
}
