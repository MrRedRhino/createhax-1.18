package org.pipeman.createhax;

import mod.pipeman.createhax.hax.*;
import org.pipeman.createhax.hax.ShowContraptionInv;
import org.pipeman.createhax.hax.SuperSponge;
import org.pipeman.createhax.hax.VillagerTradeFinder;
import org.pipeman.createhax.hax.bb.BedrockBreaker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.pipeman.createhax.hax.FlyHack;

@Mod("createhax")
public class CreateHax {
    public static final Minecraft MC = Minecraft.getInstance();

    public CreateHax() {
        MinecraftForge.EVENT_BUS.register(new ActiveHaxRenderer());
        new FlyHack();
//        MinecraftForge.EVENT_BUS.register(flyHackInst);

        new SuperSponge();
//        MinecraftForge.EVENT_BUS.register(spongeInst);
//        ActiveHaxRenderer.registerHack(f);

        new BedrockBreaker();

        new ShowContraptionInv();

//        new LightspeedTrader();

        new VillagerTradeFinder();
    } // TODO fix mixins
}
