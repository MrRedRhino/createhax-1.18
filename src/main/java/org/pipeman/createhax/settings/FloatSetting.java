package org.pipeman.createhax.settings;

import org.pipeman.createhax.Util;

import java.util.Optional;
import java.util.Properties;

public class FloatSetting implements Setting<Float> {
    private final String propertiesKey;
    private float value;
    private float min = Float.MIN_VALUE;
    private float max = Float.MAX_VALUE;

    public FloatSetting(String propertiesKey, float defaultValue) {
        this.propertiesKey = propertiesKey;
        this.value = defaultValue;
    }

    public FloatSetting setMin(float min) {
        this.min = min;
        return this;
    }

    public FloatSetting setMax(float max) {
        this.max = max;
        return this;
    }

    public FloatSetting save() {
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
            Optional<Float> aFloat = Util.parseFloat(val);
            if (aFloat.isEmpty()) p.put(propertiesKey, String.valueOf(value));
            else set(aFloat.get());
        }
    }

    @Override
    public void onScroll(double delta) {
        value = Math.min(Math.max(value + (float) delta, min), max);
    }

    @Override
    public Float get() {
        return value;
    }

    @Override
    public void set(Float value) {
        this.value = value;
    }
}
