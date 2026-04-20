package com.gaura.mining_and_placing_animations.fabric;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.gaura.mining_and_placing_animations.animation.data.AnimationResourceManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;

public final class MiningAndPlacingAnimationsFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        MiningAndPlacingAnimations.init();

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(ResourceLocation.fromNamespaceAndPath(MiningAndPlacingAnimations.MOD_ID, "animation_loader"), new AnimationResourceManager());

        FabricLoader.getInstance().getModContainer(MiningAndPlacingAnimations.MOD_ID).ifPresent(container -> {
            ResourceManagerHelper.registerBuiltinResourcePack(
                    ResourceLocation.fromNamespaceAndPath(MiningAndPlacingAnimations.MOD_ID, "default_animations"),
                    container,
                    Component.literal("Default Mining & Placing Animations"),
                    ResourcePackActivationType.DEFAULT_ENABLED
            );
        });

        ClientTickEvents.END_WORLD_TICK.register(world -> BlockAnimationManager.tick());
    }
}
