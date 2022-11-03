package org.pipeman.createhax.hax;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.pipeman.createhax.CreateHax;

public class LightspeedTrader {
    private final KeyMapping tradeKey = new KeyMapping("Villager Trader key", 66, "Hax");

    public LightspeedTrader() {
        ClientRegistry.registerKeyBinding(tradeKey);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClick(ScreenEvent.MouseInputEvent event) {
//        System.out.println("something");
//        System.out.println(event);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (tradeKey.isDown()) {
            if (CreateHax.MC.screen instanceof MerchantScreen screen) {
                MerchantMenu menu = screen.getMenu();
//                for (MerchantScreen.)
            }
        }
    }

    @SubscribeEvent
    public void tick(TickEvent event) {
//        ScreenOpener.open();
    }
}
