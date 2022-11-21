package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.HonkPacket;
import com.simibubi.create.content.logistics.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.foundation.networking.AllPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.AABB;
import org.pipeman.createhax.HackManager;

import java.util.List;
import java.util.Properties;

public class ConstantHonkHack implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private int cooldown = 0;
    private boolean running = false;
    private final String propertiesKey = "constant-honk-";

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void saveTick() {
        if (cooldown++ == 10) sendHonks(true);
        else if (cooldown == 20) {
            sendHonks(false);
            cooldown = 0;
        }
    }

    private void sendHonks(boolean honk) {
        AABB bb = AABB.ofSize(MC.player.position(), 1000, 1000, 1000);
        List<CarriageContraptionEntity> carriages = MC.player.level.getEntitiesOfClass(CarriageContraptionEntity.class, bb);

        for (CarriageContraptionEntity carriage : carriages) {
            AllPackets.channel.sendToServer(new HonkPacket.Serverbound(carriage.getCarriage().train, honk));
        }
    }

    @Override
    public String getName() {
        return "ConstantHonk";
    }

    @Override
    public void saveState(Properties prop) {
        HackManager.put(prop, propertiesKey + "running", String.valueOf(isRunning()));
    }

    @Override
    public void readState(Properties prop) {
        setRunning(HackManager.get(prop, propertiesKey + "running", false));
    }
}
