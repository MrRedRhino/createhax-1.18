package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.sync.ClientMotionPacket;
import com.simibubi.create.foundation.networking.AllPackets;
import net.minecraft.client.Minecraft;
import org.pipeman.createhax.Util;
import org.pipeman.createhax.settings.FloatSetting;

import java.text.DecimalFormat;

public class FlyHack implements IHack {
    private static final Minecraft MC = Minecraft.getInstance();
    private final FloatSetting speed = new FloatSetting("flyhack-speed", 0.05f).setMin(0);
    private boolean running = false;

    @Override
    public void onModify(double delta) {
        speed.onScroll(delta / 100);

        DecimalFormat df = new DecimalFormat("#.##");
        Util.sendActionbarMessage("Fly speed set to: §2" + df.format(speed.get()));
        MC.player.getAbilities().setFlyingSpeed(speed.get());
    }

    @Override
    public void saveTick() {
        AllPackets.channel.sendToServer(new ClientMotionPacket(MC.player.getDeltaMovement(), true, 0));
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setRunning(boolean running) {
        this.running = running;

        if (MC.player == null) return;
        MC.player.getAbilities().mayfly = running;
        MC.player.getAbilities().flying = running;
    }

    @Override
    public String getName() {
        return "FlyHack";
    }

}
