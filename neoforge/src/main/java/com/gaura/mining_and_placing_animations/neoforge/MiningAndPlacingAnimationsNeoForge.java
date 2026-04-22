package com.gaura.mining_and_placing_animations.neoforge;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.gaura.mining_and_placing_animations.animation.data.AnimationResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@Mod(value = MiningAndPlacingAnimations.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MiningAndPlacingAnimations.MOD_ID, value = Dist.CLIENT)
public final class MiningAndPlacingAnimationsNeoForge {

    public MiningAndPlacingAnimationsNeoForge() {

        MiningAndPlacingAnimations.init();
    }

    @SubscribeEvent
    public static void onRegisterReloadListeners(AddClientReloadListenersEvent event) {

        event.addListener(ResourceLocation.fromNamespaceAndPath(MiningAndPlacingAnimations.MOD_ID, "animation_loader"), new AnimationResourceManager());
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {

        if (event.getPackType() == PackType.CLIENT_RESOURCES) {

            event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath(MiningAndPlacingAnimations.MOD_ID, "resourcepacks/default_animations"),
                    PackType.CLIENT_RESOURCES,
                    Component.literal("Default Mining & Placing Animations"),
                    PackSource.BUILT_IN,
                    false,
                    Pack.Position.TOP
            );
        }
    }

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
            BlockAnimationManager.render(event.getPoseStack(), buffers, minecraft.gameRenderer.getMainCamera(), minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false));
            buffers.endBatch();
        }
    }
}
