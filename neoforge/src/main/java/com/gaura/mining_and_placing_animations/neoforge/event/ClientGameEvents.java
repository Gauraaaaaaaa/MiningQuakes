package com.gaura.mining_and_placing_animations.neoforge.event;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = MiningAndPlacingAnimations.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientGameEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {

        if (Minecraft.getInstance().level != null) {

            BlockAnimationManager.tick();
        }
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {

            MultiBufferSource.BufferSource buffers = minecraft.renderBuffers().bufferSource();
            BlockAnimationManager.render(event.getPoseStack(), buffers, minecraft.gameRenderer.getMainCamera(), minecraft.getTimer().getGameTimeDeltaPartialTick(false));
            buffers.endBatch();
        }
    }
}
