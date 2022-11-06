package org.pipeman.createhax.hax;

public interface IHack {
    boolean isRunning();

    void setRunning(boolean running);

    String getName();

    default void saveTick() {

    }

    default void onModify(double delta) {

    }

    default boolean showToggleMessage() {
        return true;
    }
}
