package org.pipeman.createhax.settings;

import org.pipeman.createhax.Util;

import java.util.Optional;
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

    public IntSetting save() {
        return SettingSaver.registerSetting(this);
    }

    @Override
    public void save(Properties p) {
        p.put(propertiesKey, String.valueOf(value));
    }

    @Override
    public void read(Properties p) {
        String val = (String) p.get(propertiesKey);
        if (val == null) p.put(propertiesKey, String.valueOf(value));
        else {
            Optional<Integer> anInt = Util.parseInt(val);
            if (anInt.isEmpty()) p.put(propertiesKey, String.valueOf(value));
            else set(anInt.get());
        }
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
