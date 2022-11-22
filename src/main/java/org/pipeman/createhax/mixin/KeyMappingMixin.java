package org.pipeman.createhax.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.pipeman.createhax.hax.HoldUse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(KeyMapping.class)
public class KeyMappingMixin {

    @ModifyVariable(method = "setDown", at = @At("HEAD"), index = 1, argsOnly = true)
    private boolean down(boolean value) {
        return ((Object) this) == Minecraft.getInstance().options.keyUse && HoldUse.INSTANCE.isRunning() ? true : value;
    }
}
