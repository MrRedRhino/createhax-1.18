package org.pipeman.createhax.ejector_hack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.block.depot.EjectorBlock;
import com.simibubi.create.content.logistics.block.depot.EjectorPlacementPacket;
import com.simibubi.create.foundation.networking.AllPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class EjectorScreen extends Screen {
    private static final Minecraft MC = Minecraft.getInstance();
    private EditBox v;
    private EditBox h;

    protected EjectorScreen(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    protected void init() {
        super.init();
        v = new EditBox(MC.font, 20, 20, 20, 80, Component.nullToEmpty(""));
        h = new EditBox(MC.font, 20, 20, 20, 80, Component.nullToEmpty(""));

        h.setResponder(s -> valuesChanged());
        v.setResponder(s -> valuesChanged());
    }

    private void valuesChanged() {
        int vInt;
        int hInt;
        try {
            vInt = Integer.parseInt(v.getValue());
            hInt = Integer.parseInt(h.getValue());
        } catch (NumberFormatException ignored) {
            return;
        }

        if (MC.hitResult instanceof BlockHitResult r) {
            BlockPos pos = r.getBlockPos();
            BlockState bs = MC.player.level.getBlockState(pos);

            if (bs.is(AllBlocks.WEIGHTED_EJECTOR.get())) {
                AllPackets.channel.sendToServer(
                        new EjectorPlacementPacket(hInt, vInt, pos, bs.getValue(EjectorBlock.HORIZONTAL_FACING))
                );
            }
        }
    }

    @Override
    public void render(PoseStack ps, int x, int y, float thing) {
        super.render(ps, x, y, thing);
    }
}
