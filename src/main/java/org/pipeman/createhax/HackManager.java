package org.pipeman.createhax;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.pipeman.createhax.hax.IHack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class HackManager {
    private static final Minecraft MC = Minecraft.getInstance();
    private final Map<ToggleState, IHack> hacks = new HashMap<>();
    public static final HackManager INSTANCE = new HackManager();
    private static final Path CONFIG_PATH = Path.of("config", "createhax.properties");

    public HackManager() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        Font font = MC.gui.getFont();
        PoseStack ps = event.getMatrixStack();
        int line = 0;
        for (IHack hack : hacks.values()) {
            if (hack.isRunning()) {
                font.draw(ps, hack.getName(), 10, 10 + 10 * line++, 0x6875DF);
            }
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent clientTickEvent) {
        if (MC.player == null || MC.getConnection() == null) return;

        for (IHack hack : hacks.values()) if (hack.isRunning()) hack.saveTick();
    }

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        for (Map.Entry<ToggleState, IHack> e : hacks.entrySet()) {
            ToggleState ts = e.getKey();
            IHack hack = e.getValue();

            if (ts.toggleKey.isDown() && !ts.hotkeyDown) {
                hack.setRunning(!hack.isRunning());
                if (hack.showToggleMessage()) Util.showToggleMessage(hack.getName(), hack.isRunning());
            }
            ts.hotkeyDown = ts.toggleKey.isDown();
        }
    }

    @SubscribeEvent
    public void mouseScroll(InputEvent.MouseScrollEvent event) {
        for (Map.Entry<ToggleState, IHack> e : hacks.entrySet()) {
            ToggleState ts = e.getKey();
            IHack hack = e.getValue();

            if (ts.hotkeyDown) {
                event.setCanceled(true);
                hack.onModify(event.getScrollDelta());
            }
        }
    }

    public void registerHack(IHack hack, KeyMapping toggleKey) {
        ClientRegistry.registerKeyBinding(toggleKey);
        hacks.put(new ToggleState(toggleKey), hack);
    }

    public void read() throws IOException {
        doWithProperties(p -> hacks.values().forEach(s -> s.readState(p)));
    }

    public void save() throws IOException {
        doWithProperties(p -> hacks.values().forEach(s -> s.saveState(p)));
    }

    private static void doWithProperties(Consumer<Properties> action) throws IOException {
        //noinspection ResultOfMethodCallIgnored
        CONFIG_PATH.toFile().createNewFile();

        Properties properties = new Properties();
        properties.load(Files.newInputStream(CONFIG_PATH));
        action.accept(properties);
        properties.store(Files.newOutputStream(CONFIG_PATH), "");
    }

    public static int get(Properties p, String key, int fallback) {
        return get(p, key, fallback, s -> optTry(() -> Integer.parseInt(s)).orElse(fallback));
    }

    public static float get(Properties p, String key, float fallback) {
        return get(p, key, fallback, s -> optTry(() -> Float.parseFloat(s)).orElse(fallback));
    }

    public static boolean get(Properties p, String key, boolean fallback) {
        return get(p, key, fallback, s -> optTry(() -> parseBoolean(s)).orElse(fallback));
    }

    private static boolean parseBoolean(String s) {
        if (s.equalsIgnoreCase("true")) return true;
        if (s.equalsIgnoreCase("false")) return false;
        throw new IllegalArgumentException("Not a boolean");
    }

    public static <T> T get(Properties p, String key, T fallback, Function<String, T> mappingFunction) {
        return mappingFunction.apply(get(p, key, String.valueOf(fallback)));
    }

    public static String get(Properties p, String key, String fallback) {
        String value = (String) p.get(key);
        return value == null ? put(p, key, fallback) : value;
    }

    public static <T> Optional<T> optTry(Supplier<T> mappingFunction) {
        try {
            return Optional.ofNullable(mappingFunction.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static String put(Properties p, String key, String value) {
        p.put(key, value);
        return value;
    }

    private static class ToggleState {
        private boolean hotkeyDown = false;
        private final KeyMapping toggleKey;

        private ToggleState(KeyMapping toggleKey) {
            this.toggleKey = toggleKey;
        }
    }
}
