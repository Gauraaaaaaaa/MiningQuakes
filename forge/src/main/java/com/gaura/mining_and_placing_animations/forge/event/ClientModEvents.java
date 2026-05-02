package com.gaura.mining_and_placing_animations.forge.event;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.gaura.mining_and_placing_animations.animation.data.AnimationResourceManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = MiningAndPlacingAnimations.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {

        event.registerReloadListener(new AnimationResourceManager());
    }

    @SubscribeEvent
    public static void onAddPackFinders(AddPackFindersEvent event) {

        if (event.getPackType() == PackType.CLIENT_RESOURCES) {

            Path resourcePath = ModList.get().getModFileById(MiningAndPlacingAnimations.MOD_ID).getFile().findResource("resourcepacks", "default_animations");

            Pack pack = Pack.readMetaAndCreate(
                    "builtin/default_animations",
                    Component.literal("Default Mining & Placing Animations"),
                    false,
                    (id) -> new PathPackResources(id, resourcePath, true),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN
            );


            if (pack != null) {

                event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
            }
        }
    }
}