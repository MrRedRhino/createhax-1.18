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

import java.util.HashMap;
import java.util.Map;

public class HackManager {
    private static final Minecraft MC = Minecraft.getInstance();
    private final Map<ToggleState, IHack> hacks = new HashMap<>();
    public static final HackManager INSTANCE = new HackManager();

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

    private static class ToggleState {
        private boolean hotkeyDown = false;
        private final KeyMapping toggleKey;

        private ToggleState(KeyMapping toggleKey) {
            this.toggleKey = toggleKey;
        }
    }
}
