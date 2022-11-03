package org.pipeman.createhax.hax.bb;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;

public class BlockBreaker {
    public static void breakBlock(BlockPos pos) {
        InventoryManager.switchToItem(Items.DIAMOND_PICKAXE);
        Minecraft.getInstance().gameMode.startDestroyBlock(pos, Direction.UP);
    }
}