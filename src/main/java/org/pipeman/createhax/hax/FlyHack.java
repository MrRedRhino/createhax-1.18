package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.sync.ClientMotionPacket;
import com.simibubi.create.foundation.networking.AllPackets;
import org.pipeman.createhax.Util;

import java.text.DecimalFormat;

import static org.pipeman.createhax.CreateHax.MC;

public class FlyHack implements IHack {
    private float speed = 1;
    private boolean running = false;

    @Override
    public void onModify(double delta) {
        speed = (float) Math.max(0.1f, speed + delta / 10);

        DecimalFormat df = new DecimalFormat("#.##");
        Util.sendActionbarMessage("Fly speed set to: §2" + df.format(speed));
        MC.player.getAbilities().setFlyingSpeed(speed);
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
    }

    @Override
    public String getName() {
        return "FlyHack";
    }

}
