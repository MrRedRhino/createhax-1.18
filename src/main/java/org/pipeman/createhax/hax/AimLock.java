package org.pipeman.createhax.hax;

import java.util.Properties;

public class AimLock implements IHack {
    public static final AimLock INSTANCE = new AimLock();
    private boolean running = false;

    private AimLock() {
    }

    @Override

    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public String getName() {
        return "AimLock";
    }

    @Override
    public void saveState(Properties prop) {

    }

    @Override
    public void readState(Properties prop) {

    }
}
