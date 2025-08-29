package com.gaura.mining_quakes.forge;

import com.gaura.mining_quakes.config.MiningQuakesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.common.Mod;

import com.gaura.mining_quakes.MiningQuakes;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MiningQuakes.MOD_ID)
public final class MiningQuakesForge {

    private final FMLJavaModLoadingContext fmlCtx;

    public MiningQuakesForge(FMLJavaModLoadingContext context) {

        this.fmlCtx = context;

        MiningQuakes.init();

        context.getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {

        fmlCtx.getContainer().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (minecraft, parent) -> AutoConfig.getConfigScreen(MiningQuakesConfig.class, parent).get()
                )
        );
    }
}
