package com.gaura.mining_quakes.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mining_quakes")
public class MiningQuakesConfig implements ConfigData {

    public int lifetime = 6;
    public float horizontalMaxAngle = 15.0F;
    public float verticalMaxAngle = 15.0F;
    public int horizontalOscillations = 3;
    public int verticalOscillations = 2;
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public EasingFunction easingFunction = EasingFunction.EASE_IN_OUT;
    public boolean renderOutline = true;
    public boolean randomQuakes = true;
    public String[] blacklist = {
            "minecraft:tall_grass"
    };
}
