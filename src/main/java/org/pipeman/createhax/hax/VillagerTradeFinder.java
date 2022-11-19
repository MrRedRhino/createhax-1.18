package org.pipeman.createhax.hax;

import com.simibubi.create.AllBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.pipeman.createhax.Util;

import java.util.Map;

public class VillagerTradeFinder implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private BlockPos bearingPos;
    private Villager villager;
    private String enchantmentName;
    private BlockPos crankPos;
    int stage = 0; // 1=press crank, 2=press bearing, 3=wait for villager to have no job, 4=if villager has
    // job -> press him, 5=check gui
    boolean bearingTurning = false;
    private boolean running = false;

    public VillagerTradeFinder() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void saveTick() {
        if (crankPos == null || enchantmentName == null || bearingPos == null || villager == null) return;

        switch (stage) {
            case 1 -> {
                Util.sendItemUsePacket(crankPos, Direction.UP);
                bearingTurning = true;
                stage++;
            }
            case 2 -> {
                if (villager.getVillagerData().getProfession() == VillagerProfession.NONE) {
                    if (bearingTurning) {
                        Util.sendItemUsePacket(bearingPos, Direction.UP);
                        bearingTurning = false;
                    }
                }
                stage++;
            }
            case 3 -> {
                if (villager.getVillagerData().getProfession() == VillagerProfession.LIBRARIAN) {
                    stage++;
                    ServerboundInteractPacket packet = ServerboundInteractPacket.createInteractionPacket(
                            villager, false, InteractionHand.MAIN_HAND);
                    MC.getConnection().send(packet);
                } else {
                    stage = 2;
                }
            }
            case 4 -> {
                if (MC.screen instanceof MerchantScreen screen) {
                    for (MerchantOffer offer : screen.getMenu().getOffers()) {
                        for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(offer.getResult()).entrySet()) {
                            if (entry.getKey().getMaxLevel() == entry.getValue()) {
                                if (enchantmentName != null && enchantmentName.equals(entry.getKey().getRegistryName().toString())) {
                                    Util.sendActionbarMessage("BOOK FOUND!");
                                    MC.screen.onClose();
                                    running = false;
                                    bearingPos = null;
                                    villager = null;
                                    enchantmentName = null;
                                    return;
                                }
                            }
                        }
                    }
                    stage = 1;
                    MC.screen.onClose();
                } else {
                    stage = 3;
                }
            }
        }
        System.out.print(stage + "\r");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;

        stage = 1;
        if (running) {
            Util.sendChatMessage("Left click a bearing, left click a hand crank, right click a villager and send the " +
                                 "enchantment name in chat to start the TradeFinder.");
        } else {
            bearingPos = null;
            villager = null;
            enchantmentName = null;
        }
    }

    @Override
    public String getName() {
        return "TradeFinder";
    }

    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (!running || bearingPos != null) return;
        Level w = event.getWorld();
        BlockState b = w.getBlockState(event.getPos());
        if (b.is(AllBlocks.MECHANICAL_BEARING.get())) {
            bearingPos = event.getPos();
            event.setCanceled(true);
            Util.sendActionbarMessage("Set bearing position to " + event.getPos() + ".");
        }
        if (b.is(AllBlocks.HAND_CRANK.get())) {
            crankPos = event.getPos();
            event.setCanceled(true);
            Util.sendActionbarMessage("Set crank position to " + event.getPos() + ".");
        }
    }

    @SubscribeEvent
    public void onInteractEntity(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!running || villager != null) return;
        event.setCanceled(true);
        if (event.getTarget() instanceof Villager v) {
            villager = v;
            villager.addEffect(new MobEffectInstance(MobEffects.GLOWING));
            Util.sendActionbarMessage("Set villager.");
        }
    }

    @SubscribeEvent
    public void onChatMessage(ClientChatEvent event) {
        if (!running || enchantmentName != null) return;
        event.setCanceled(true);
        for (ResourceLocation key : ForgeRegistries.ENCHANTMENTS.getKeys()) {
            if (event.getMessage().equalsIgnoreCase(key.toString())) {
                enchantmentName = event.getMessage();
                Util.sendActionbarMessage("Configured enchantment to search: " + enchantmentName);
                return;
            }
        }
        Util.sendActionbarMessage("Enchantment not found: " + event.getMessage());
    }
}
