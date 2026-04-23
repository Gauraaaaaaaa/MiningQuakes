package com.gaura.mining_and_placing_animations.neoforge;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = MiningAndPlacingAnimations.MOD_ID, dist = Dist.CLIENT)
public final class MiningAndPlacingAnimationsNeoForge {

    public MiningAndPlacingAnimationsNeoForge() {

        MiningAndPlacingAnimations.init();
    }
}
