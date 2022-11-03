package org.pipeman.createhax.hax;

import org.pipeman.createhax.Util;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public abstract class Toggleable implements IHack {
    boolean hotkeyDown = false;
    public boolean running = false;

    public abstract KeyMapping getToggleKey();

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (getToggleKey().isDown() && !hotkeyDown) {
            running = !running;
            onToggle();
        }
        hotkeyDown = getToggleKey().isDown();
    }

    public void onToggle() {
        Util.showToggleMessage(getName(), running);
    }

    @SubscribeEvent
    public void mouseScroll(InputEvent.MouseScrollEvent event) {
        if (hotkeyDown) {
            event.setCanceled(true);
            onModifyModifiable(event.getScrollDelta());
        }
    }

    public abstract void onModifyModifiable(double delta);
}
