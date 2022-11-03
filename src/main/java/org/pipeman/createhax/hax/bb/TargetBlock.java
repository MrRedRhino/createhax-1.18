package org.pipeman.createhax.hax.bb;

import org.pipeman.createhax.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;

import java.util.ArrayList;


public class TargetBlock {
    private final BlockPos blockPos;
    private BlockPos redstoneTorchBlockPos;
    private final BlockPos pistonBlockPos;
    private final ClientLevel world;
    private Status status;
    private BlockPos slimeBlockPos;
    private int tickTimes;
    private boolean hasTried;
    private int stuckTicksCounter;

    public TargetBlock(BlockPos pos, ClientLevel world) {
        this.hasTried = false;
        this.stuckTicksCounter = 0;
        this.status = Status.UNINITIALIZED;
        this.blockPos = pos;
        this.world = world;
        this.pistonBlockPos = pos.above();
        this.redstoneTorchBlockPos = CheckingEnvironment.findNearbyFlatBlockToPlaceRedstoneTorch(this.world, this.blockPos);
        if (redstoneTorchBlockPos == null) {
            this.slimeBlockPos = CheckingEnvironment.findPossibleSlimeBlockPos(world, pos);
            if (slimeBlockPos != null) {
                BlockPlacer.simpleBlockPlacement(slimeBlockPos, Blocks.SLIME_BLOCK);
                redstoneTorchBlockPos = slimeBlockPos.above();
            } else {
                this.status = Status.FAILED;
            }
        }
    }

    public Status tick() {
        this.tickTimes++;
        updateStatus();
        System.out.println(this.status);
        switch (this.status) {
            case UNINITIALIZED:
                InventoryManager.switchToItem(Blocks.PISTON);
                BlockPlacer.pistonPlacement(this.pistonBlockPos, Direction.UP);
                InventoryManager.switchToItem(Blocks.REDSTONE_TORCH);
                BlockPlacer.simpleBlockPlacement(this.redstoneTorchBlockPos, Blocks.REDSTONE_TORCH);
                break;
            case UNEXTENDED_WITH_POWER_SOURCE:
                break;
            case EXTENDED:
                ArrayList<BlockPos> nearByRedstoneTorchPosList = CheckingEnvironment.findNearbyRedstoneTorch(world, pistonBlockPos);
                for (BlockPos pos : nearByRedstoneTorchPosList) {
                    BlockBreaker.breakBlock(pos);
                }
                BlockBreaker.breakBlock(this.pistonBlockPos);
                BlockPlacer.pistonPlacement(this.pistonBlockPos, Direction.DOWN);
                this.hasTried = true;
                break;
            case RETRACTED:
                BlockBreaker.breakBlock(pistonBlockPos);
                BlockBreaker.breakBlock(pistonBlockPos.above());
                if (this.slimeBlockPos != null) {
                    BlockBreaker.breakBlock(slimeBlockPos);
                }
                return Status.RETRACTED;
            case RETRACTING:
                return Status.RETRACTING;
            case UNEXTENDED_WITHOUT_POWER_SOURCE:
                InventoryManager.switchToItem(Blocks.REDSTONE_TORCH);
                BlockPlacer.simpleBlockPlacement(this.redstoneTorchBlockPos, Blocks.REDSTONE_TORCH);
                break;
            case FAILED:
                BlockBreaker.breakBlock(pistonBlockPos);
                BlockBreaker.breakBlock(pistonBlockPos.above());
                return Status.FAILED;
            case STUCK:
                BlockBreaker.breakBlock(pistonBlockPos);
                BlockBreaker.breakBlock(pistonBlockPos.above());
                break;
            case NEEDS_WAITING:
                break;
        }
        return null;
    }

    enum Status {
        FAILED,
        UNINITIALIZED,
        UNEXTENDED_WITH_POWER_SOURCE,
        UNEXTENDED_WITHOUT_POWER_SOURCE,
        EXTENDED,
        NEEDS_WAITING,
        RETRACTING,
        RETRACTED,
        STUCK
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public ClientLevel getWorld() {
        return world;
    }

    private void updateStatus() {
        if (this.tickTimes > 40) {
            this.status = Status.FAILED;
            return;
        }
        this.redstoneTorchBlockPos = CheckingEnvironment.findNearbyFlatBlockToPlaceRedstoneTorch(this.world, this.blockPos);
        if (this.redstoneTorchBlockPos == null) {
            this.slimeBlockPos = CheckingEnvironment.findPossibleSlimeBlockPos(world, blockPos);
            if (slimeBlockPos != null) {
                BlockPlacer.simpleBlockPlacement(slimeBlockPos, Blocks.SLIME_BLOCK);
                redstoneTorchBlockPos = slimeBlockPos.above();
            } else {
                this.status = Status.FAILED;
                Util.sendActionbarMessage("Failed to place redstone-torch");
            }
        } else if (!this.world.getBlockState(this.blockPos).is(Blocks.BEDROCK) && this.world.getBlockState(this.pistonBlockPos).is(Blocks.PISTON)) {
            this.status = Status.RETRACTED;
        } else if (this.world.getBlockState(this.pistonBlockPos).is(Blocks.PISTON) && this.world.getBlockState(this.pistonBlockPos).getValue(PistonBaseBlock.EXTENDED)) {
            this.status = Status.EXTENDED;
        } else if (this.world.getBlockState(this.pistonBlockPos).is(Blocks.MOVING_PISTON)) {
            this.status = Status.RETRACTING;
        }  else if (this.world.getBlockState(this.pistonBlockPos).is(Blocks.PISTON) && !this.world.getBlockState(this.pistonBlockPos).getValue(PistonBaseBlock.EXTENDED) && CheckingEnvironment.findNearbyRedstoneTorch(this.world, this.pistonBlockPos).size() != 0 && this.world.getBlockState(this.blockPos).is(Blocks.BEDROCK)) {
            this.status = Status.UNEXTENDED_WITH_POWER_SOURCE;
        } else if (this.hasTried && this.world.getBlockState(this.pistonBlockPos).is(Blocks.PISTON) && this.stuckTicksCounter < 15) {
            this.status = Status.NEEDS_WAITING;
            this.stuckTicksCounter++;
        } else if (this.world.getBlockState(this.pistonBlockPos).is(Blocks.PISTON) && this.world.getBlockState(this.pistonBlockPos).getValue(PistonBaseBlock.FACING) == Direction.DOWN && !this.world.getBlockState(this.pistonBlockPos).getValue(PistonBaseBlock.EXTENDED) && CheckingEnvironment.findNearbyRedstoneTorch(this.world, this.pistonBlockPos).size() != 0 && this.world.getBlockState(this.blockPos).is(Blocks.BEDROCK)) {
            this.status = Status.STUCK;
            this.hasTried = false;
            this.stuckTicksCounter = 0;
        }else if (this.world.getBlockState(this.pistonBlockPos).is(Blocks.PISTON) && !this.world.getBlockState(this.pistonBlockPos).getValue(PistonBaseBlock.EXTENDED) && this.world.getBlockState(this.pistonBlockPos).getValue(PistonBaseBlock.FACING) == Direction.UP && CheckingEnvironment.findNearbyRedstoneTorch(this.world, this.pistonBlockPos).size() == 0 && this.world.getBlockState(this.blockPos).is(Blocks.BEDROCK)) {
            this.status = Status.UNEXTENDED_WITHOUT_POWER_SOURCE;
        } else if (CheckingEnvironment.has2BlocksOfPlaceToPlacePiston(world, this.blockPos)) {
            this.status = Status.UNINITIALIZED;
        } else if (!CheckingEnvironment.has2BlocksOfPlaceToPlacePiston(world, this.blockPos)) {
            this.status = Status.FAILED;
            Util.sendActionbarMessage("Failed to place piston");
        } else {
            this.status = Status.FAILED;
        }
    }
}
