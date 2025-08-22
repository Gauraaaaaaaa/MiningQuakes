package com.gaura.mining_quakes.config;

import me.fallenbreath.conditionalmixin.api.mixin.RestrictiveMixinConfigPlugin;

import java.util.List;
import java.util.Set;

public class MiningQuakesMixinConfigPlugin extends RestrictiveMixinConfigPlugin {

    @Override
    public String getRefMapperConfig() {

        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {

        return List.of();
    }
}
