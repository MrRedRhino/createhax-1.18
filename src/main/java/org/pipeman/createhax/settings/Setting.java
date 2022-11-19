package org.pipeman.createhax.settings;

import java.util.Properties;

public interface Setting<T> {
    void save(Properties p);
    void read(Properties p);

    void onScroll(double delta);

    T get();

    void set(T value);
}
