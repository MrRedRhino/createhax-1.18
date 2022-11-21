package org.pipeman.createhax.hax.bb;

import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.pipeman.createhax.HackManager;
import org.pipeman.createhax.hax.IHack;

import java.util.Properties;

public class BedrockBreaker implements IHack {
    private boolean running = false;
    public static boolean breakEverything = false;
    boolean lmbIsPressed = false;
    private final String propertiesKey = "bedrock-breaker-";

    public BedrockBreaker() {
        MinecraftForge.EVENT_BUS.register(this);
    }

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
        return "BedrockBreaker";
    }

    @Override
    public void saveState(Properties prop) {
        HackManager.put(prop, propertiesKey + "running", String.valueOf(isRunning()));
    }

    @Override
    public void readState(Properties prop) {
        setRunning(HackManager.get(prop, propertiesKey + "running", false));
    }

    @Override
    public void saveTick() {
        BreakingFlowController.tick();
    }

    @SubscribeEvent
    public void lcbEvent(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).is(Blocks.BEDROCK) && running && lmbIsPressed) {
            BreakingFlowController.addBlockPosToList(event.getPos());
        }
    }

    @SubscribeEvent
    public void mouseInput(InputEvent.MouseInputEvent event) {
        lmbIsPressed = event.getAction() == 1;
    }
}
