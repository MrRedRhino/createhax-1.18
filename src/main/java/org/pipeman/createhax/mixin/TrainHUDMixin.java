package org.pipeman.createhax.mixin;

import com.simibubi.create.content.contraptions.components.structureMovement.interaction.controls.TrainHUD;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = TrainHUD.class, remap = false)
public class TrainHUDMixin {
    @ModifyArg(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(DDD)D"), index = 1)
    private static double infiniteNegativeScroll(double p_14009_) {
        return -Double.MAX_VALUE;
    }
}
