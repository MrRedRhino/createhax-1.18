package org.pipeman.createhax;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import static org.pipeman.createhax.CreateHax.MC;

public class Util {

    public static void sendItemUsePacket(BlockPos pos, Direction dir) {
        if (MC.getConnection() == null) return;

        BlockHitResult result = new BlockHitResult(
                new Vec3(0, 0, 0), dir, pos, false);
        MC.getConnection().send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, result));
    }

    public static void showToggleMessage(String module, boolean on) {
        String msg = "Toggled " + module + (on ? " §2ON" : " §cOFF");
        MC.gui.setOverlayMessage(Component.nullToEmpty(msg), false);
    }

    public static boolean switchToItemInHotbar(ItemStack item) {
        if (MC.getConnection() == null || MC.gameMode == null || MC.player == null) return false;

        Inventory inv = MC.player.getInventory();
        int slotBefore = inv.selected;

        int i = inv.findSlotMatchingItem(item);
        if (i != -1) {
            if (Inventory.isHotbarSlot(i)) {
                MC.getConnection().send(new ServerboundSetCarriedItemPacket(i));
            } else {
                MC.getConnection().send(new ServerboundPickItemPacket(i));
            }
            inv.selected = slotBefore;
            return true;
        }
        return false;
    }

    public static void sendSneakPacket(boolean sneaking) {
        if (MC.getConnection() == null || MC.player == null) return;

        ServerboundPlayerCommandPacket.Action packet = sneaking ?
        ServerboundPlayerCommandPacket.Action.PRESS_SHIFT_KEY : ServerboundPlayerCommandPacket.Action.RELEASE_SHIFT_KEY;

        MC.getConnection().send(new ServerboundPlayerCommandPacket(MC.player, packet));
    }

    public static void sendActionbarMessage(String message){
        MC.gui.setOverlayMessage(Component.nullToEmpty(message),false);
    }


    public static void sendChatMessage(String message){
        MC.gui.getChat().addMessage(Component.nullToEmpty(message));
    }

    public static boolean hasNecessaryBlocks(String hackName, ItemStack... items) {
        if (MC.player == null) return false;
        boolean hasBlocks = true;

        for (ItemStack stack : items) {
            if (!MC.player.getInventory().contains(stack)) {
                if (hasBlocks) {
                    sendChatMessage(hackName + " needs the following items to work:");
                }
                hasBlocks = false;
                sendChatMessage(" - §a\"" + stack.getDescriptionId());
            }
        }
        return hasBlocks;
    }
}
