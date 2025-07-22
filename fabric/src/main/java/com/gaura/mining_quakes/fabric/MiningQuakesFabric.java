package com.gaura.mining_quakes.fabric;

import com.gaura.mining_quakes.MiningQuakes;
import net.fabricmc.api.ModInitializer;

public final class MiningQuakesFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        MiningQuakes.init();
    }
}
