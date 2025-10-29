package com.gaura.mining_quakes.fabric;

import com.gaura.mining_quakes.MiningQuakes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class MiningQuakesImpl implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        MiningQuakes.init();
    }

    public static boolean isContinuityLoaded() {

        return FabricLoader.getInstance().isModLoaded(MiningQuakes.CONTINUITY_MOD_ID);
    }
}
