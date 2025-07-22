package com.gaura.mining_quakes.neoforge;

import com.gaura.mining_quakes.config.MiningQuakesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

import com.gaura.mining_quakes.MiningQuakes;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(MiningQuakes.MOD_ID)
public final class MiningQuakesNeoForge {

    public MiningQuakesNeoForge() {

        MiningQuakes.init();

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> AutoConfig.getConfigScreen(MiningQuakesConfig.class, parent).get()
        );
    }
}
