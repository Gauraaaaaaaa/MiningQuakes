package com.gaura.mining_and_placing_animations.forge.mixin;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(conflict = @Condition("embeddium"))
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBlockEntities(PoseStack poseStack, float tickDelta, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(tickDelta));
            }
        }
    }
}