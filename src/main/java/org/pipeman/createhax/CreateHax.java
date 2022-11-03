package org.pipeman.createhax;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.pipeman.createhax.hax.*;
import org.pipeman.createhax.hax.bb.BedrockBreaker;

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

        new VillagerTradeFinder();
    } // TODO fix mixins
}
