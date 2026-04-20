package com.gaura.mining_and_placing_animations.mixin.sodium;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRendererMixin {

    @WrapOperation(
            method = "extractBlockEntity",
            at = @At(
                    value = "NEW",
                    target = "(ILcom/mojang/blaze3d/vertex/PoseStack$Pose;)Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;"
            )
    )
    private ModelFeatureRenderer.CrumblingOverlay onExtractBlockEntity(int progress, PoseStack.Pose cameraPose, Operation<ModelFeatureRenderer.CrumblingOverlay> original, @Local(argsOnly = true, ordinal = 0) PoseStack poseStack, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)));
            }
        }

        return original.call(progress, cameraPose);
    }
}
