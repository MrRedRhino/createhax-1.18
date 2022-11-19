package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;

public class ShowContraptionInv implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private String overlayContent;
    private boolean running = false;

    @Override
    public void saveTick() {
        if (MC.hitResult == null || MC.hitResult.getType() != HitResult.Type.ENTITY) return;

        EntityHitResult result = ((EntityHitResult) MC.hitResult);

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

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public String getName() {
        return "Contraption Inventory Renderer";
    }
}
