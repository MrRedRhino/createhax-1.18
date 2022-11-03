package org.pipeman.createhax.mixin;

import com.simibubi.create.content.schematics.client.tools.Tools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = Tools.class, remap = false)
public class ToolsMixin {
    @Inject(method = "getTools", at = @At(value = "RETURN"), cancellable = true)
    private static void getTools(boolean creative, CallbackInfoReturnable<List<Tools>> cir) {
        cir.setReturnValue(List.of(Tools.Move, Tools.MoveY, Tools.Deploy, Tools.Rotate, Tools.Flip, Tools.Print));
    }
}
