package org.pipeman.createhax.hax;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.fluids.IFluidBlock;
import org.pipeman.createhax.CreateHax;
import org.pipeman.createhax.Util;

public class SuperSponge implements IHack {
    int blocksPerTick = 4;
    int blocksThisTick = 0;
    private boolean running = false;

    @Override
    public void onModify(double delta) {
        blocksPerTick = (int) Math.max(0, blocksPerTick + delta);

        Util.sendActionbarMessage("Blocks per tick set to: §2" + blocksPerTick +
                                  (blocksPerTick == 0 ? " " + "(unlimited)" : ""));
    }

    @Override
    public void saveTick() {
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

    private void placeAndBreakCasing(BlockPos pos) {
        if (CreateHax.MC.getConnection() == null || CreateHax.MC.player == null) return;

        Util.switchToItemInHotbar(AllBlocks.ANDESITE_CASING.asStack());
        Util.sendItemUsePacket(pos, Direction.UP);

        Util.switchToItemInHotbar(AllItems.WRENCH.asStack());
        Util.sendSneakPacket(true);
        Util.sendItemUsePacket(pos, Direction.UP);
        Util.sendSneakPacket(false);
    }

    private boolean isFluidSource(BlockPos pos, Level world) {
        Block block = world.getBlockState(pos).getBlock();
        return block instanceof LiquidBlock || block instanceof IFluidBlock;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
        if (running) {
            if (Util.hasNecessaryBlocks(getName(), AllItems.WRENCH.asStack(), AllBlocks.ANDESITE_CASING.asStack())) {
                Util.showToggleMessage(getName(), true);
            } else {
                this.running = false;
            }
        } else {
            Util.showToggleMessage(getName(), false);
        }
    }

    @Override
    public boolean showToggleMessage() {
        return false;
    }

    @Override
    public String getName() {
        return "SuperSponge";
    }
}
