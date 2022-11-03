package org.pipeman.createhax.hax;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import org.pipeman.createhax.ActiveHaxRenderer;
import org.pipeman.createhax.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.IFluidBlock;
import org.pipeman.createhax.CreateHax;

public class SuperSponge extends Toggleable {
    private final KeyMapping toggleKey = new KeyMapping("Super Sponge", 66, "Hax");
    int blocksPerTick = 4;
    int blocksThisTick = 0;

    public SuperSponge() {
        ClientRegistry.registerKeyBinding(toggleKey);
        MinecraftForge.EVENT_BUS.register(this);
        ActiveHaxRenderer.registerHack(this);
    }

    @Override
    public KeyMapping getToggleKey() {
        return toggleKey;
    }

    @Override
    public void onToggle() {
        if (running) {
            if (Util.hasNecessaryBlocks(getName(), AllItems.WRENCH.asStack(),
                    AllBlocks.ANDESITE_CASING.asStack())) {
                Util.showToggleMessage(getName(), running);
            } else {
                running = false;
            }
        }
    }

    @Override
    public void onModifyModifiable(double delta) {
        blocksPerTick = (int) Math.max(0, blocksPerTick + delta);

        Util.sendActionbarMessage("Blocks per tick set to: §2" + blocksPerTick +
                (blocksPerTick == 0 ? " " + "(unlimited)" : ""));
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (CreateHax.MC.player == null || CreateHax.MC.level == null || !running) return;
        BlockPos playerPos = CreateHax.MC.player.blockPosition();

        for (int y = 4; y > -4; y--) {
            for (int x = -4; x < 4; x++) {
                for (int z = -4; z < 4; z++) {
                    if (isFluidSource(playerPos.offset(x, y, z), CreateHax.MC.player.level)) {
                        if (blocksThisTick <= blocksPerTick || blocksThisTick == 0) {
                            placeAndBreakCasing(playerPos.offset(x, y, z));
                            blocksThisTick++;
                        }
                    }
                    if (blocksThisTick >= blocksPerTick && blocksPerTick != 0) {
                        blocksThisTick = 0;
                        return;
                    }
                }
            }
        }
    }

    void placeAndBreakCasing(BlockPos pos) {
        if (CreateHax.MC.getConnection() == null || CreateHax.MC.player == null) return;

        Util.switchToItemInHotbar(AllBlocks.ANDESITE_CASING.asStack());
        Util.sendItemUsePacket(pos, Direction.UP);

        Util.switchToItemInHotbar(AllItems.WRENCH.asStack());
        Util.sendSneakPacket(true);
        Util.sendItemUsePacket(pos, Direction.UP);
        Util.sendSneakPacket(false);
    }

    boolean isFluidSource(BlockPos pos, Level world) {
        Block block = world.getBlockState(pos).getBlock();
        return block instanceof LiquidBlock || block instanceof IFluidBlock;
    }

    @Override
    public boolean isOn() {
        return running;
    }

    @Override
    public String getName() {
        return "SuperSponge";
    }
}
