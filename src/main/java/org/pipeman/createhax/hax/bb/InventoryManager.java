package org.pipeman.createhax.hax.bb;

import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class InventoryManager {
    public static boolean switchToItem(ItemLike item) {
        Minecraft minecraftClient = Minecraft.getInstance();
        Inventory playerInventory = minecraftClient.player.getInventory();

        int i = playerInventory.findSlotMatchingItem(new ItemStack(item));

        if ("diamond_pickaxe".equals(item.toString())) {
            i = getEfficientTool(playerInventory);
        }

        if (i != -1) {
            if (Inventory.isHotbarSlot(i)) {
                playerInventory.selected = i;
            } else {
                minecraftClient.gameMode.handlePickItem(i);
            }
            minecraftClient.getConnection().send(new ServerboundSetCarriedItemPacket(playerInventory.selected));
            return true;
        }
        return false;
    }

    private static int getEfficientTool(Inventory playerInventory) {
        for (int i = 0; i < playerInventory.items.size(); ++i) {
            if (getBlockBreakingSpeed(Blocks.PISTON.defaultBlockState(), i) > 45f) {
                return i;
            }
        }
        return -1;
    }

    public static boolean canInstantlyMinePiston() {
        Minecraft minecraftClient = Minecraft.getInstance();
        Inventory playerInventory = minecraftClient.player.getInventory();

        for (int i = 0; i < playerInventory.getContainerSize(); i++) {
            if (getBlockBreakingSpeed(Blocks.PISTON.defaultBlockState(), i) > 45f) {
                return true;
            }
        }
        return false;
    }

    private static float getBlockBreakingSpeed(BlockState block, int slot) {
        Minecraft minecraftClient = Minecraft.getInstance();
        Player player = minecraftClient.player;
        ItemStack stack = player.getInventory().getItem(slot);

        float f = stack.getDestroySpeed(block);
        if (f > 1.0F) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, stack);
            ItemStack itemStack = player.getInventory().getItem(slot);
            if (i > 0 && !itemStack.isEmpty()) {
                f += (float) (i * i + 1);
            }
        }

        if (MobEffectUtil.hasDigSpeed(player)) {
            f *= 1.0F + (float) (MobEffectUtil.getDigSpeedAmplification(player) + 1) * 0.2F;
        }

        if (player.hasEffect(MobEffects.DIG_SLOWDOWN)) {
            float k = switch (player.getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };

            f *= k;
        }

        if (player.isEyeInFluid(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(player)) {
            f /= 5.0F;
        }

        if (!player.isOnGround()) {
            f /= 5.0F;
        }

        return f;
    }

    public static int getInventoryItemCount(ItemLike item) {
        Minecraft minecraftClient = Minecraft.getInstance();
        Inventory playerInventory = minecraftClient.player.getInventory();
        return playerInventory.countItem(item.asItem());
    }

    public static String warningMessage() {
        Minecraft minecraftClient = Minecraft.getInstance();
        if (minecraftClient.gameMode == null) return null;
        if (!"survival".equals(minecraftClient.gameMode.getPlayerMode().getName())) {
            return "Only works in survival-mode";
        }

        if (InventoryManager.getInventoryItemCount(Blocks.PISTON) < 2) {
            return "You need at least 2 pistons";
        }

        if (InventoryManager.getInventoryItemCount(Blocks.REDSTONE_TORCH) < 1) {
            return "You need at least 1 redstone torch";
        }

        if (InventoryManager.getInventoryItemCount(Blocks.SLIME_BLOCK) < 1){
            return "You need at least 1 slime-block";
        }

        if (!InventoryManager.canInstantlyMinePiston()) {
            return "You have to be able to insta-mine stone";
        }
        return null;
    }
}
