package org.pipeman.createhax.hax;

import com.simibubi.create.content.contraptions.components.structureMovement.sync.ClientMotionPacket;
import com.simibubi.create.foundation.networking.AllPackets;
import org.pipeman.createhax.ActiveHaxRenderer;
import org.pipeman.createhax.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraft.util.Mth;

import java.text.DecimalFormat;

import static org.pipeman.createhax.CreateHax.MC;

public class FlyHack extends Toggleable {
    private final KeyMapping toggleKey = new KeyMapping("Fly-Hack", 66, "Hax");
    private float speed = 1;

    @Override
    public KeyMapping getToggleKey() {
        return toggleKey;
    }
    @Override
    public void onModifyModifiable(double delta) {
        speed = (float) Math.max(0.1f, speed + delta / 10);

        DecimalFormat df = new DecimalFormat("#.##");
        Util.sendActionbarMessage("Fly speed set to: §2" + df.format(speed));
    }

    public FlyHack() {
        ClientRegistry.registerKeyBinding(toggleKey);
        ActiveHaxRenderer.registerHack(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void performFly(TickEvent.ClientTickEvent clientTickEvent) {
        if (MC.player == null || !running) return;

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
    public boolean isOn() {
        return running;
    }

    @Override
    public String getName() {
        return "FlyHack";
    }
}
