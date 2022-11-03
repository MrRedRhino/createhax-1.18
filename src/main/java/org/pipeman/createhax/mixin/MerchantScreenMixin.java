package org.pipeman.createhax.mixin;

import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantScreen.class)
public class MerchantScreenMixin {

    @Inject(method = "postButtonClick", at = @At("TAIL"))
    private void postButtonClick(CallbackInfo ci) {
        System.out.println("thing");
    }
}
