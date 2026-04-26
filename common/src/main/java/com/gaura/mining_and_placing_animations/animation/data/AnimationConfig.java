package com.gaura.mining_and_placing_animations.animation.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnimationConfig {

    private final ResourceLocation globalMiningAnimation;
    private final ResourceLocation globalPlacingAnimation;
    private final Map<String, ResourceLocation> specificMiningAnimationByBlock;
    private final Map<TagKey<Block>, ResourceLocation> specificMiningAnimationByTag;
    private final Map<String, ResourceLocation> specificPlacingAnimationByBlock;
    private final Map<TagKey<Block>, ResourceLocation> specificPlacingAnimationByTag;
    private final Set<String> miningBlacklistByBlock;
    private final Set<TagKey<Block>> miningBlacklistByTag;
    private final Set<String> placingBlacklistByBlock;
    private final Set<TagKey<Block>> placingBlacklistByTag;

    public AnimationConfig(ResourceLocation globalMiningAnimation, ResourceLocation globalPlacingAnimation, Map<String, ResourceLocation> specificMiningAnimationByBlock, Map<TagKey<Block>, ResourceLocation> specificMiningAnimationByTag, Map<String, ResourceLocation> specificPlacingAnimationByBlock, Map<TagKey<Block>, ResourceLocation> specificPlacingAnimationByTag, Set<String> miningBlacklistByBlock, Set<TagKey<Block>> miningBlacklistByTag, Set<String> placingBlacklistByBlock, Set<TagKey<Block>> placingBlacklistByTag) {

        this.globalMiningAnimation = globalMiningAnimation;
        this.globalPlacingAnimation = globalPlacingAnimation;
        this.specificMiningAnimationByBlock = specificMiningAnimationByBlock;
        this.specificMiningAnimationByTag = specificMiningAnimationByTag;
        this.specificPlacingAnimationByBlock = specificPlacingAnimationByBlock;
        this.specificPlacingAnimationByTag = specificPlacingAnimationByTag;
        this.miningBlacklistByBlock = miningBlacklistByBlock;
        this.miningBlacklistByTag = miningBlacklistByTag;
        this.placingBlacklistByBlock = placingBlacklistByBlock;
        this.placingBlacklistByTag = placingBlacklistByTag;
    }

    @Nullable
    public ResourceLocation getMiningAnimation(BlockState blockState) {

        String blockId = BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).getNamespace();

        if (this.miningBlacklistByBlock.contains(blockId)) {

            return null;
        }

        for (TagKey<Block> tag : this.miningBlacklistByTag) {

            if (blockState.is(tag)) {

                return null;
            }
        }

        if (this.specificMiningAnimationByBlock.containsKey(blockId)) {

            return this.specificMiningAnimationByBlock.get(blockId);
        }

        for (Map.Entry<TagKey<Block>, ResourceLocation> entry : this.specificMiningAnimationByTag.entrySet()) {

            if (blockState.is(entry.getKey())) {

                return entry.getValue();
            }
        }

        return this.globalMiningAnimation;
    }

    @Nullable
    public ResourceLocation getPlacingAnimation(BlockState blockState) {

        String blockId = BuiltInRegistries.BLOCK.getKey(blockState.getBlock()).getNamespace();

        if (this.placingBlacklistByBlock.contains(blockId)) {

            return null;
        }

        for (TagKey<Block> tag : this.placingBlacklistByTag) {

            if (blockState.is(tag)) {

                return null;
            }
        }

        if (this.specificPlacingAnimationByBlock.containsKey(blockId)) {

            return this.specificPlacingAnimationByBlock.get(blockId);
        }

        for (Map.Entry<TagKey<Block>, ResourceLocation> entry : this.specificPlacingAnimationByTag.entrySet()) {

            if (blockState.is(entry.getKey())) {

                return entry.getValue();
            }
        }

        return this.globalPlacingAnimation;
    }

    public static AnimationConfig fromJson(JsonObject jsonObject) {

        ResourceLocation globalMiningAnimation = null;
        ResourceLocation globalPlacingAnimation = null;

        if (jsonObject.has("global") && !jsonObject.get("global").isJsonNull()) {

            JsonObject global = jsonObject.getAsJsonObject("global");

            if (global.has("mining_animation")) {

                globalMiningAnimation = ResourceLocation.tryParse(global.get("mining_animation").getAsString());
            }

            if (global.has("placing_animation")) {

                globalPlacingAnimation = ResourceLocation.tryParse(global.get("placing_animation").getAsString());
            }
        }

        Map<String, ResourceLocation> specificMiningAnimationByBlock = new HashMap<>();
        Map<TagKey<Block>, ResourceLocation> specificMiningAnimationByTag = new HashMap<>();

        Map<String, ResourceLocation> specificPlacingAnimationByBlock = new HashMap<>();
        Map<TagKey<Block>, ResourceLocation> specificPlacingAnimationByTag = new HashMap<>();

        if (jsonObject.has("specific") && !jsonObject.get("specific").isJsonNull()) {

            for (JsonElement jsonElement : jsonObject.getAsJsonArray("specific")) {

                AnimationRule animationRule = AnimationRule.fromJson(jsonElement.getAsJsonObject());

                for (String blockId : animationRule.blocks()) {

                    if (blockId.startsWith("#")) {

                        String tagId = blockId.substring(1);
                        TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, ResourceLocation.tryParse(tagId));

                        if (animationRule.miningAnimationId() != null) {

                            specificMiningAnimationByTag.put(tagKey, animationRule.miningAnimationId());
                        }

                        if (animationRule.placingAnimationId() != null) {

                            specificPlacingAnimationByTag.put(tagKey, animationRule.placingAnimationId());
                        }

                    }
                    else {

                        if (animationRule.miningAnimationId() != null) {

                            specificMiningAnimationByBlock.put(blockId, animationRule.miningAnimationId());
                        }

                        if (animationRule.placingAnimationId() != null) {

                            specificPlacingAnimationByBlock.put(blockId, animationRule.placingAnimationId());
                        }
                    }
                }
            }
        }

        Set<String> miningBlacklistByBlock = new HashSet<>();
        Set<TagKey<Block>> miningBlacklistByTag = new HashSet<>();

        if (jsonObject.has("mining_blacklist") && !jsonObject.get("mining_blacklist").isJsonNull()) {

            for (JsonElement jsonElement : jsonObject.getAsJsonArray("mining_blacklist")) {

                String blockId = jsonElement.getAsString();

                if (blockId.startsWith("#")) {

                    String tagId = blockId.substring(1);
                    miningBlacklistByTag.add(TagKey.create(Registries.BLOCK, ResourceLocation.tryParse(tagId)));
                }
                else {

                    miningBlacklistByBlock.add(blockId);
                }
            }
        }

        Set<String> placingBlacklistByBlock = new HashSet<>();
        Set<TagKey<Block>> placingBlacklistByTag = new HashSet<>();

        if (jsonObject.has("placing_blacklist") && !jsonObject.get("placing_blacklist").isJsonNull()) {

            for (JsonElement jsonElement : jsonObject.getAsJsonArray("placing_blacklist")) {

                String blockOrTag = jsonElement.getAsString();

                if (blockOrTag.startsWith("#")) {

                    String tagId = blockOrTag.substring(1);
                    placingBlacklistByTag.add(TagKey.create(Registries.BLOCK, ResourceLocation.tryParse(tagId)));
                }
                else {

                    placingBlacklistByBlock.add(blockOrTag);
                }
            }
        }

        return new AnimationConfig(globalMiningAnimation, globalPlacingAnimation, specificMiningAnimationByBlock, specificMiningAnimationByTag, specificPlacingAnimationByBlock, specificPlacingAnimationByTag, miningBlacklistByBlock, miningBlacklistByTag, placingBlacklistByBlock, placingBlacklistByTag);
    }
}