package com.gaura.mining_and_placing_animations.neoforge.event;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.gaura.mining_and_placing_animations.animation.data.AnimationResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

@EventBusSubscriber(modid = MiningAndPlacingAnimations.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {

        event.registerReloadListener(new AnimationResourceManager());
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
}
