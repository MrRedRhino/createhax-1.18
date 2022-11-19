package org.pipeman.createhax;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.pipeman.createhax.hax.*;
import org.pipeman.createhax.hax.bb.BedrockBreaker;
import org.pipeman.createhax.settings.SettingSaver;

import java.io.IOException;

@Mod("createhax")
public class CreateHax {
//    public static final Minecraft MC = Minecraft.getInstance();

    public CreateHax() throws IOException {
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;

        HackManager.INSTANCE.registerHack(new FlyHack(), new KeyMapping("Fly-Hack", 66, "Hax"));

        HackManager.INSTANCE.registerHack(new SuperSponge(), new KeyMapping("Super Sponge", 66, "Hax"));

        HackManager.INSTANCE.registerHack(new VillagerTradeFinder(), new KeyMapping("Villager trade finder", 66, "Hax"));

        HackManager.INSTANCE.registerHack(new BedrockBreaker(), new KeyMapping("Bedrock-Breaker", 66, "Hax"));

        HackManager.INSTANCE.registerHack(new ShowContraptionInv(), new KeyMapping("Show contraption inventory", 66, "Hax"));

        HackManager.INSTANCE.registerHack(new FullbrightHack(), new KeyMapping("Fullbright", 66, "Hax"));

        HackManager.INSTANCE.registerHack(new ConstantHonkHack(), new KeyMapping("Constant Honking", 66, "Hax"));

        SettingSaver.read();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                SettingSaver.save();
            } catch (Exception ignored) {
            }
        }));
    }
}
