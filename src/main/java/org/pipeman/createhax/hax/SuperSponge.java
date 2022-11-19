package org.pipeman.createhax.hax;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.fluids.IFluidBlock;
import org.pipeman.createhax.Util;
import org.pipeman.createhax.settings.IntSetting;

public class SuperSponge implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private final IntSetting bpt = new IntSetting("supersponge-blocks-per-tick", 4).setMin(0).save();
    int blocksThisTick = 0;
    private boolean running = false;

    @Override
    public void onModify(double delta) {
        bpt.onScroll(delta);
        int bpt = this.bpt.get();
        Util.sendActionbarMessage("Blocks per tick set to: §2" + bpt + (bpt == 0 ? " " + "(unlimited)" : ""));
    }

    @Override
    public void saveTick() {
        if (MC.player == null || MC.level == null || !running) return;
        int bpt = this.bpt.get();
        BlockPos playerPos = MC.player.blockPosition();

        for (int y = 4; y > -4; y--) {
            for (int x = -4; x < 4; x++) {
                for (int z = -4; z < 4; z++) {
                    if (isFluidSource(playerPos.offset(x, y, z), MC.player.level)) {
                        if (blocksThisTick <= bpt || blocksThisTick == 0) {
                            placeAndBreakCasing(playerPos.offset(x, y, z));
                            blocksThisTick++;
                        }
                    }
                    if (blocksThisTick >= bpt && bpt != 0) {
                        blocksThisTick = 0;
                        return;
                    }
                }
            }
        }
    }

    private void placeAndBreakCasing(BlockPos pos) {
        if (MC.getConnection() == null || MC.player == null) return;

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
