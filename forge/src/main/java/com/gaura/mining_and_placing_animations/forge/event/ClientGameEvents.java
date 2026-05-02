package com.gaura.mining_and_placing_animations.forge.event;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MiningAndPlacingAnimations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientGameEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {

        if (Minecraft.getInstance().level != null && event.phase == TickEvent.Phase.END) {

            BlockAnimationManager.tick();
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {

            MultiBufferSource.BufferSource buffers = minecraft.renderBuffers().bufferSource();
            BlockAnimationManager.render(event.getPoseStack(), buffers, minecraft.gameRenderer.getMainCamera(), event.getPartialTick());
            buffers.endBatch();
        }
    }
}
