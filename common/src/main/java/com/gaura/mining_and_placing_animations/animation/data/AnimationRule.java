package com.gaura.mining_and_placing_animations.animation.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record AnimationRule(List<String> blocks, @Nullable ResourceLocation miningAnimationId, @Nullable ResourceLocation placingAnimationId) {

    public static AnimationRule fromJson(JsonObject jsonObject) {

        List<String> blocks = new ArrayList<>();

        for (JsonElement jsonElement : jsonObject.getAsJsonArray("blocks")) {

            blocks.add(jsonElement.getAsString());
        }

        ResourceLocation miningAnimationId = jsonObject.has("mining_animation") ? ResourceLocation.tryParse(jsonObject.get("mining_animation").getAsString()) : null;

        ResourceLocation placingAnimationId = jsonObject.has("placing_animation") ? ResourceLocation.tryParse(jsonObject.get("placing_animation").getAsString()) : null;

        return new AnimationRule(blocks, miningAnimationId, placingAnimationId);
    }
}