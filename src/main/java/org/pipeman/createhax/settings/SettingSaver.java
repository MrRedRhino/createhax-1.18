package org.pipeman.createhax.settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

public class SettingSaver {
    private static final List<Setting<?>> settings = new ArrayList<>();
    private static final Path CONFIG_PATH = Path.of("config", "createhax.properties");

    public static <T extends Setting<?>> T registerSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public static void read() throws IOException {
        doWithProperties(p -> settings.forEach(s -> s.read(p)));
    }

    public static void save() throws IOException {
        doWithProperties(p -> settings.forEach(s -> s.save(p)));
    }

    private static void doWithProperties(Consumer<Properties> action) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        CONFIG_PATH.toFile().createNewFile();

        Properties properties = new Properties();
        properties.load(Files.newInputStream(CONFIG_PATH));
        action.accept(properties);
        properties.store(Files.newOutputStream(CONFIG_PATH), "");
    }
}
