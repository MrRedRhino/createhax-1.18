package org.pipeman.createhax.hax;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.Properties;

public class HoldUse implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private boolean running = false;
    public static final HoldUse INSTANCE = new HoldUse();

    private HoldUse() {
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        KeyMapping.set(MC.options.keyUse.getKey(), running);
    }

    @Override
    public String getName() {
        return "HoldUse";
    }

    @Override
    public void saveState(Properties prop) {

    }

    @Override
    public void readState(Properties prop) {

    }
}
