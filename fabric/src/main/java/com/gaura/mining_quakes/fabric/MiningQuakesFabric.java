package com.gaura.mining_quakes.fabric;

import com.gaura.mining_quakes.MiningQuakes;
import net.fabricmc.api.ClientModInitializer;

public final class MiningQuakesFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        MiningQuakes.init();
    }
}
