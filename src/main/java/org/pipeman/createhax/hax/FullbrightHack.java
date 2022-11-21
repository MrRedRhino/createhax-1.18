package org.pipeman.createhax.hax;

import net.minecraft.client.Minecraft;
import org.pipeman.createhax.HackManager;
import org.pipeman.createhax.Util;
import org.pipeman.createhax.settings.FloatSetting;

import java.text.DecimalFormat;
import java.util.Properties;

public class FullbrightHack implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private boolean running = false;
    private double originalGamma = 0;
    private final String propertiesKey = "fullbright-";
    private final FloatSetting gamma = new FloatSetting(propertiesKey + "gamma", 1).setMin(0);

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        if (running) {
            originalGamma = MC.options.gamma;
            MC.options.gamma = gamma.get();
        } else {
            MC.options.gamma = originalGamma;
        }
    }

    @Override
    public String getName() {
        return "Fullbright";
    }

    @Override
    public void saveState(Properties prop) {
        HackManager.put(prop, propertiesKey + "running", String.valueOf(running));
        gamma.save(prop);
    }

    @Override
    public void readState(Properties prop) {
        setRunning(HackManager.get(prop, propertiesKey + "running", false));
        gamma.read(prop);
    }

    @Override
    public void onModify(double delta) {
        gamma.onScroll(delta / 10);

        DecimalFormat df = new DecimalFormat("#.##");
        Util.sendActionbarMessage("Fullbright gamma set to: §2" + df.format(gamma.get()));
        if (running) MC.options.gamma = gamma.get();
    }
}
