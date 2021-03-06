package net.toshimichi.sushi.events;

public class KeyPressEvent extends CancellableEvent {

    private final int keyCode;

    public KeyPressEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
