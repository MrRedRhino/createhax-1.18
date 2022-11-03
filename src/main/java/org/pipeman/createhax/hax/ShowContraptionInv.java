package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.pipeman.createhax.CreateHax;

import java.util.HashMap;

public class ShowContraptionInv {
    private final KeyMapping toggleKey = new KeyMapping(
            "Show contraption inventory hotkey", 66, "Hax");

    private String overlayContent;
    public ShowContraptionInv() {
        ClientRegistry.registerKeyBinding(toggleKey);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tick(TickEvent event) {
        if (CreateHax.MC.hitResult == null || CreateHax.MC.hitResult.getType() != HitResult.Type.ENTITY) return;

        EntityHitResult result = ((EntityHitResult) CreateHax.MC.hitResult);

        Entity entity = result.getEntity();
        if (entity.getPassengers().size() == 0) return;

        if (entity.getPassengers().get(0) instanceof AbstractContraptionEntity contraption) {
            HashMap<Item, Integer> items = new HashMap<>();
            int totalSlots = 0;
            int usedSlots = 0;
            for (BlockEntity block : contraption.getContraption().presentTileEntities.values()) {
                if (block instanceof Container c) {
                    for (int i = 0; i < c.getContainerSize(); i++) {
                        totalSlots++;
                        ItemStack is = c.getItem(i);
                        Item item = is.getItem();

                        if (!is.isEmpty()) {
                            usedSlots++;

                            if (!items.containsKey(item)) items.put(item, is.getCount());
                            else items.replace(item, items.get(item) + is.getCount());
                        }
                    }
                }
            }

            overlayContent = (totalSlots - usedSlots) + " of " + totalSlots + " used.\n";

            System.out.println(items);
            System.out.println(usedSlots + " used of " + totalSlots);
        }
    }
}
