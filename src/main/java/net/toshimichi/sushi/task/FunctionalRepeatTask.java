package net.toshimichi.sushi.task;

public class FunctionalRepeatTask extends TaskAdapter<Void, Void> {

    private final RepeatTask delegate;

    public FunctionalRepeatTask(RepeatTask delegate) {
        this.delegate = delegate;
    }

    @Override
    public void tick() throws Exception {
        if (delegate.tick()) stop(null);
    }
}
