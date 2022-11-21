package org.pipeman.createhax.hax;

import java.util.Properties;

public interface IHack {
    boolean isRunning();

    void setRunning(boolean running);

    String getName();

    void saveState(Properties prop);

    void readState(Properties prop);

    default void saveTick() {

    }

    default void onModify(double delta) {

    }

    default boolean showToggleMessage() {
        return true;
    }
}
