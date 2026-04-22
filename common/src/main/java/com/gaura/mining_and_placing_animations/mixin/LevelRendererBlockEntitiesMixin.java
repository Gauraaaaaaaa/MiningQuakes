package com.gaura.mining_and_placing_animations.mixin;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(conflict = @Condition("sodium"))
@Mixin(LevelRenderer.class)
public class LevelRendererBlockEntitiesMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "renderBlockEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBlockEntities(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, MultiBufferSource.BufferSource bufferSource2, Camera camera, float f, CallbackInfo ci, @Local BlockEntity blockEntity) {

        if (BlockAnimationManager.isBlockInvisible(blockEntity.getBlockPos())) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockEntity.getBlockPos());

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false)));
            }
        }
    }
}
