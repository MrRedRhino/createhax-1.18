package org.pipeman.createhax;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.pipeman.createhax.hax.*;
import org.pipeman.createhax.hax.bb.BedrockBreaker;

@Mod("createhax")
public class CreateHax {
    private static final String HAX = "Hax";

    public CreateHax() {
        if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;

        HackManager.INSTANCE.registerHack(new FlyHack(), key("Fly-Hack"));

        HackManager.INSTANCE.registerHack(new SuperSponge(), key("Super Sponge"));

        HackManager.INSTANCE.registerHack(new VillagerTradeFinder(), key("Villager trade finder"));

        HackManager.INSTANCE.registerHack(new BedrockBreaker(), key("Bedrock-Breaker"));

        HackManager.INSTANCE.registerHack(new ShowContraptionInv(), key("Show contraption inventory"));

        HackManager.INSTANCE.registerHack(new FullbrightHack(), key("Fullbright"));

        HackManager.INSTANCE.registerHack(new ConstantHonkHack(), key("Constant Honking"));

        HackManager.INSTANCE.registerHack(new FastRightClickHack(), key("Fast Right Click"));

        HackManager.INSTANCE.registerHack(AimLock.INSTANCE, key("Aim Lock"));

        HackManager.INSTANCE.registerHack(HoldUse.INSTANCE, key("Hold Use"));

        Util.ignoreException(HackManager.INSTANCE::read);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Util.ignoreException(HackManager.INSTANCE::save)));
    }

    private static KeyMapping key(String name, int code) {
        return new KeyMapping(name, code, HAX);
    }

    private static KeyMapping key(String name) {
        return key(name, 66);
    }
}
