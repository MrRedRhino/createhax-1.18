package org.pipeman.createhax.hax;

import net.minecraft.client.Minecraft;
import org.pipeman.createhax.Util;

import java.text.DecimalFormat;

public class FullbrightHack implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private boolean running = false;
    private double originalGamma = 0;
    private float gamma = 1;

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        if (running) {
            originalGamma = MC.options.gamma;
            MC.options.gamma = gamma;
        } else {
            MC.options.gamma = originalGamma;
        }
    }

    @Override
    public String getName() {
        return "Fullbright";
    }

    @Override
    public void onModify(double delta) {
        gamma = (float) Math.max(0, gamma + delta / 10);

        DecimalFormat df = new DecimalFormat("#.##");
        Util.sendActionbarMessage("Fullbright gamma set to: §2" + df.format(gamma));
        if (running) MC.options.gamma = gamma;
    }
}
