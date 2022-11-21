package org.pipeman.createhax.hax;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import org.pipeman.createhax.HackManager;
import org.pipeman.createhax.Util;
import org.pipeman.createhax.invokers.IMinecraftInvoker;
import org.pipeman.createhax.settings.IntSetting;

import java.util.Properties;

public class FastRightClickHack implements IHack {
    private final Minecraft MC = Minecraft.getInstance();
    private boolean running = false;
    private final String propertiesKey = "fast-right-click-";
    private final IntSetting bpt = new IntSetting(propertiesKey + "blocks-per-tick", 4).setMin(1).setMax(64);

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public String getName() {
        return "FastRightClick";
    }

    @Override
    public void saveState(Properties prop) {
        HackManager.put(prop, propertiesKey + "running", String.valueOf(isRunning()));
        bpt.save(prop);
    }

    @Override
    public void readState(Properties prop) {
        setRunning(HackManager.get(prop, propertiesKey + "running", false));
        bpt.read(prop);
    }

    @Override
    public void saveTick() {
        if (isVanillaKeybindHeld(MC.options.keyUse) && canUseFastRightClick(MC.player)) {
            final int count = bpt.get();
            for (int i = 0; i < count; ++i) {
                ((IMinecraftInvoker) MC).invokeItemUse();
            }
        }
    }

    private static boolean canUseFastRightClick(Player player) {
        HitResult trace = player.pick(6, 0f, false);
        return trace.getType() == HitResult.Type.BLOCK; // FAST_RIGHT_CLICK_BLOCK_RESTRICTION.isAllowed(Blocks.AIR); if its not a block
    }


    private static boolean isVanillaKeybindHeld(KeyMapping key) {
//        return Keys.isKeyDown(key.getKeyCode());
        return key.isDown();
    }

    @Override
    public void onModify(double delta) {
        bpt.onScroll(delta);
        Util.sendActionbarMessage("FastRightClick blocks per tick set to: §2" + bpt.get());
    }
}
