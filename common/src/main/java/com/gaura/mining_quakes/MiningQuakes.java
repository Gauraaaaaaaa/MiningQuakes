package com.gaura.mining_quakes;

import com.gaura.mining_quakes.config.MiningQuakesConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MiningQuakes {

    public static final String MOD_ID = "mining_quakes";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static MiningQuakesConfig CONFIG = new MiningQuakesConfig();

    public static void init() {

        AutoConfig.register(MiningQuakesConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(MiningQuakesConfig.class).getConfig();
    }

    public static boolean isBlockBlacklisted(BlockState blockState) {

        for (String blockId : CONFIG.blacklist) {

            if (blockState.getBlockHolder().is(ResourceLocation.parse(blockId))) {

                return true;
            }
        }

        return false;
    }
}
