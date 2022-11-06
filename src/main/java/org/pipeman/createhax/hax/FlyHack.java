package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.sync.ClientMotionPacket;
import com.simibubi.create.foundation.networking.AllPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
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
    }

    @Override
    public void saveTick() {
        MC.player.getAbilities().mayfly = true;

        Vec3 movement = getJumpVec().scale(speed);
        AllPackets.channel.sendToServer(new ClientMotionPacket(movement, true, 100));
        MC.player.setDeltaMovement(movement);
    }

    private static Vec3 getJumpVec() {
        Vec3 movement = asVec3();
        if (MC.options.keyJump.isDown()) {
            movement = movement.add(0, 0.1f, 0);
        }
        if (MC.options.keyShift.isDown()) {
            MC.player.input.shiftKeyDown = false;
            movement = movement.scale(1.6f).add(0, -0.1f, 0);
        }
        if (MC.options.keySprint.isDown()) {
            movement = movement.scale(1.5f);
        }
        return movement;
    }

    private static Vec3 asVec3() {
        if (MC.player == null)
            return Vec3.ZERO;
        Vec2 vector2 = MC.player.input.getMoveVector();
        float f = MC.player.getSpeed();

        float f2 = f * vector2.x;
        float f3 = f * vector2.y;
        float f4 = Mth.sin(MC.player.getYRot() * ((float) Math.PI / 180f));
        float f5 = Mth.cos(MC.player.getYRot() * ((float) Math.PI / 180f));
        return new Vec3(f2 * f5 - f3 * f4, 0, f3 * f5 + f2 * f4);
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
        return "FlyHack";
    }

}
