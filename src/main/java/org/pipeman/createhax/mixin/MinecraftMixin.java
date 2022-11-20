package org.pipeman.createhax.mixin;

import net.minecraft.client.Minecraft;
import org.pipeman.createhax.invokers.IMinecraftInvoker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements IMinecraftInvoker {
    @Shadow
    private void startUseItem() {
    }

    @Override
    public void invokeItemUse() {
        this.startUseItem();
    }
}
