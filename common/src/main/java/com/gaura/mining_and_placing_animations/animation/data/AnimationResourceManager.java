package com.gaura.mining_and_placing_animations.animation.data;

import com.gaura.mining_and_placing_animations.MiningAndPlacingAnimations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class AnimationResourceManager implements ResourceManagerReloadListener {

    private static final Gson GSON = new Gson();

    private static AnimationConfig animationConfig = null;

    private static final Map<ResourceLocation, AnimationModel> ANIMATION_MODELS = new HashMap<>();

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {

        animationConfig = null;
        ANIMATION_MODELS.clear();

        resourceManager.getResource(new ResourceLocation(MiningAndPlacingAnimations.MOD_ID, "animations_config.json")).ifPresent(resource -> {

            try (Reader reader = new InputStreamReader(resource.open())) {

                animationConfig = AnimationConfig.fromJson(GsonHelper.fromJson(GSON, reader, JsonObject.class));

                MiningAndPlacingAnimations.LOGGER.info("animations_config.json loaded successfully!");
            }
            catch (Exception exception) {

                MiningAndPlacingAnimations.LOGGER.error("Failed to load animations_config.json", exception);
            }
        });

        if (animationConfig == null) {

            return;
        }

        resourceManager.listResources("animations", path -> path.getPath().endsWith(".json")).forEach((location, resource) -> {

            try (Reader reader = new InputStreamReader(resource.open())) {

                AnimationModel animationDefinition = AnimationModel.fromJson(GsonHelper.fromJson(GSON, reader, JsonObject.class));

                String path = location.getPath().replace(".json", "");
                ResourceLocation identifier = new ResourceLocation(location.getNamespace(), path);

                ANIMATION_MODELS.put(identifier, animationDefinition);
            }
            catch (Exception exception) {

                MiningAndPlacingAnimations.LOGGER.error("Failed to load animation file: {}", location, exception);
            }
        });
    }

    @Nullable
    public static ResourceLocation getMiningAnimationId(BlockState blockState) {

        return animationConfig != null ? animationConfig.getMiningAnimation(blockState) : null;
    }

    @Nullable
    public static ResourceLocation getPlacingAnimationId(BlockState blockState) {

        return animationConfig != null ? animationConfig.getPlacingAnimation(blockState) : null;
    }

    @Nullable
    public static AnimationModel getAnimationModel(@Nullable ResourceLocation identifier) {

        return ANIMATION_MODELS.get(identifier);
    }
}