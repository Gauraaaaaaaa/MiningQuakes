package com.gaura.mining_quakes.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mining_quakes")
public class MiningQuakesConfig implements ConfigData {

    public int lifetime = 8;
    public float maxAngle = 10.0F;
    public int oscillation = 3;
    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public EasingFunction easingFunction = EasingFunction.EASE_OUT;
}
