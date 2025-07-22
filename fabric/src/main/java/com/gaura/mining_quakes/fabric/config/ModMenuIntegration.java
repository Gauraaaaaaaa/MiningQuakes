package com.gaura.mining_quakes.fabric.config;

import com.gaura.mining_quakes.config.MiningQuakesConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return parent -> AutoConfig.getConfigScreen(MiningQuakesConfig.class, parent).get();
    }
}
