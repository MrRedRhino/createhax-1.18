package org.pipeman.createhax.hax.bb;

import net.minecraft.core.Vec3i;
import org.pipeman.createhax.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;

public class BreakingFlowController {
    private static ArrayList<TargetBlock> cachedTargetBlockList = new ArrayList<>();

    public static void addBlockPosToList(BlockPos pos) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) return;

        if (world.getBlockState(pos).is(Blocks.BEDROCK) || BedrockBreaker.breakEverything) {
            String hasEnoughItems = InventoryManager.warningMessage();
            if (hasEnoughItems != null) {
                Util.sendActionbarMessage(hasEnoughItems);
                return;
            }

            if (shouldAddNewTargetBlock(pos)){
                TargetBlock targetBlock = new TargetBlock(pos, world);
                cachedTargetBlockList.add(targetBlock);
            }
        } else {
            Util.sendActionbarMessage("Block has to be bedrock or breakEverything has to be turned on");
        }
    }

    public static void tick() {
        if (InventoryManager.warningMessage() != null) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (!"survival".equals(minecraftClient.gameMode.getPlayerMode().getName())) {
            return;
        }

        for (int i = 0; i < cachedTargetBlockList.size(); i++) {
            TargetBlock selectedBlock = cachedTargetBlockList.get(i);

            //玩家切换世界，或离目标方块太远时，删除所有缓存的任务
            if (selectedBlock.getWorld() != Minecraft.getInstance().level) {
                cachedTargetBlockList = new ArrayList<>();
                break;
            }

            if (blockInPlayerRange(selectedBlock.getBlockPos(), player)) {
                TargetBlock.Status status = cachedTargetBlockList.get(i).tick();
                if (status == TargetBlock.Status.RETRACTING) {
                    continue;
                } else if (status == TargetBlock.Status.FAILED || status == TargetBlock.Status.RETRACTED) {
                    if (cachedTargetBlockList.size() > i) cachedTargetBlockList.remove(i);
                } else {
                    break;
                }

            }
        }
    }

    private static boolean blockInPlayerRange(BlockPos blockPos, Player player) {
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        return (blockPos.distSqr(new Vec3i(x, y, z)) <= (float) 3.4 * (float) 3.4);
    }

    private static boolean shouldAddNewTargetBlock(BlockPos pos){
        for (TargetBlock targetBlock : cachedTargetBlockList) {
            if (targetBlock.getBlockPos().distSqr(new Vec3i(pos.getX(), pos.getY(), pos.getZ())) == 0) {
                return false;
            }
        }
        return true;
    }
}
