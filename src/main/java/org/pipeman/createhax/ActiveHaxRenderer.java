package org.pipeman.createhax;

import com.mojang.blaze3d.vertex.PoseStack;
import org.pipeman.createhax.hax.IHack;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;

import static org.pipeman.createhax.CreateHax.MC;

public class ActiveHaxRenderer {
    private static final ArrayList<IHack> hacks = new ArrayList<>();

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event) {
        Font font = MC.gui.getFont();
        PoseStack ps = event.getMatrixStack();
        int line = 0;
        for (IHack hack : hacks) {
            if (hack.isOn()) {
                font.draw(ps, hack.getName(), 10, 10 + 10 * line, 0x6875DF);
                line++;
            }
        }
    }

    public static void registerHack(IHack hack) {
        hacks.add(hack);
    }
}
