package com.gaura.mining_and_placing_animations.fabric;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.gaura.mining_and_placing_animations.animation.data.AnimationResourceManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class MiningAndPlacingAnimationsFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        MiningAndPlacingAnimations.init();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
            new IdentifiableResourceReloadListener() {

                private final AnimationResourceManager animationResourceManager = new AnimationResourceManager();

                @Override
                public ResourceLocation getFabricId() {

                    return new ResourceLocation(MiningAndPlacingAnimations.MOD_ID, "animation_loader");
                }

                @Override
                public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller2, Executor executor, Executor executor2) {

                    return this.animationResourceManager.reload(preparationBarrier, resourceManager, profilerFiller, profilerFiller2, executor, executor2);
                }
            }
        );

        FabricLoader.getInstance().getModContainer(MiningAndPlacingAnimations.MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    new ResourceLocation(MiningAndPlacingAnimations.MOD_ID, "default_animations"),
                    container,
                    Component.literal("Default Mining & Placing Animations"),
                    ResourcePackActivationType.DEFAULT_ENABLED
            );
        });

        ClientTickEvents.END_WORLD_TICK.register(world -> BlockAnimationManager.tick());

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {

            Minecraft minecraft = Minecraft.getInstance();

            MultiBufferSource.BufferSource buffers = minecraft.renderBuffers().bufferSource();

            BlockAnimationManager.render(
                    context.matrixStack(),
                    buffers,
                    minecraft.gameRenderer.getMainCamera(),
                    context.tickDelta()
            );

            buffers.endBatch();
        });
    }
}
