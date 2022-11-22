package org.pipeman.createhax.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.pipeman.createhax.hax.AimLock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Shadow
    private float yRot;
    @Shadow
    private float xRot;
    @Shadow
    public float yRotO;
    @Shadow
    public float xRotO;

    private float forcedYaw = 0;
    private float forcedPitch = 0;

    @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
    private void overrideYaw(double yawChange, double pitchChange, CallbackInfo ci) {
        if ((Object) this instanceof LocalPlayer) {
            if (AimLock.INSTANCE.isRunning()) {
                this.yRot = this.forcedYaw;
                this.xRot = this.forcedPitch;
                this.yRotO = this.yRot;
                this.xRotO = this.xRot;
                ci.cancel();

                return;
            }

            forcedYaw = xRot;
            forcedPitch = yRot;
        }
    }
}
