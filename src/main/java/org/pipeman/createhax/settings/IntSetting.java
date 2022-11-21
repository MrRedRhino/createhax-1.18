package org.pipeman.createhax.settings;

import org.pipeman.createhax.HackManager;

import java.util.Properties;

public class IntSetting implements Setting<Integer> {
    private final String propertiesKey;
    private int value;
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public IntSetting(String propertiesKey, int defaultValue) {
        this.propertiesKey = propertiesKey;
        this.value = defaultValue;
    }

    public IntSetting setMin(int min) {
        this.min = min;
        return this;
    }

    public IntSetting setMax(int max) {
        this.max = max;
        return this;
    }

    @Override
    public void save(Properties p) {
        HackManager.put(p, propertiesKey, String.valueOf(value));
    }

    @Override
    public void read(Properties p) {
        set(HackManager.get(p, propertiesKey, value));
    }

    @Override
    public void onScroll(double delta) {
        value = Math.min(Math.max(value + (int) delta, min), max);
    }

    @Override
    public Integer get() {
        return value;
    }

    @Override
    public void set(Integer value) {
        this.value = value;
    }

}
