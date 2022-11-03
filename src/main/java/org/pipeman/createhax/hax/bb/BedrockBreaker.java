package org.pipeman.createhax.hax.bb;

import org.pipeman.createhax.ActiveHaxRenderer;
import org.pipeman.createhax.hax.Toggleable;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BedrockBreaker extends Toggleable {
    private final KeyMapping toggleKey = new KeyMapping("Bedrock-Breaker", 66, "Hax");

    public BedrockBreaker() {
        ClientRegistry.registerKeyBinding(toggleKey);
        ActiveHaxRenderer.registerHack(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public boolean isOn() {
        return running;
    }

    @Override
    public String getName() {
        return "BedrockBreaker";
    }

    @Override
    public KeyMapping getToggleKey() {
        return toggleKey;
    }

    @Override
    public void onModifyModifiable(double delta) {

    }

    public static boolean breakEverything = false;
    boolean lmbIsPressed = false;


    @SubscribeEvent
    public void Tick(TickEvent event) {
        if (running) {
            BreakingFlowController.tick();
        }
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
