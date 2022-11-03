package org.pipeman.createhax.hax.bb;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockPlacer {
    public static void simpleBlockPlacement(BlockPos pos, ItemLike item) {
        Minecraft minecraftClient = Minecraft.getInstance();

        InventoryManager.switchToItem(item);
        BlockHitResult hitResult = new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), Direction.UP, pos,
                false);
//        minecraftClient.interactionManager.interactBlock(minecraftClient.player, minecraftClient.world, Hand.MAIN_HAND, hitResult);
        placeBlockWithoutInteractingBlock(minecraftClient, hitResult);
    }

    public static void pistonPlacement(BlockPos pos, Direction direction) {
        Minecraft minecraftClient = Minecraft.getInstance();
        double x = pos.getX();

        Player player = minecraftClient.player;
        float pitch = switch (direction) {
            case UP -> 90f;
            case DOWN -> -90f;
            default -> 90f;
        };

        minecraftClient.getConnection().send(new ServerboundMovePlayerPacket.Rot(player.getViewYRot(1.0f), pitch,
                player.isOnGround()));

        Vec3 vec3d = new Vec3(x, pos.getY(), pos.getZ());

        InventoryManager.switchToItem(Blocks.PISTON);
        BlockHitResult hitResult = new BlockHitResult(vec3d, Direction.UP, pos, false);
//        minecraftClient.interactionManager.interactBlock(minecraftClient.player, minecraftClient.world, Hand.MAIN_HAND, hitResult);
        placeBlockWithoutInteractingBlock(minecraftClient, hitResult);
    }

    private static void placeBlockWithoutInteractingBlock(Minecraft minecraftClient, BlockHitResult hitResult) {
        Player player = minecraftClient.player;
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);

        minecraftClient.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, hitResult));

        if (!itemStack.isEmpty() && !player.getCooldowns().isOnCooldown(itemStack.getItem())) {
            UseOnContext itemUsageContext = new UseOnContext(player, InteractionHand.MAIN_HAND, hitResult);
            itemStack.useOn(itemUsageContext);
        }
    }
}
