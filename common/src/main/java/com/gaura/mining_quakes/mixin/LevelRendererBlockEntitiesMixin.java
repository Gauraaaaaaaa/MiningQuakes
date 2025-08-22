package com.gaura.mining_quakes.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(conflict = @Condition("sodium"))
@Mixin(LevelRenderer.class)
public class LevelRendererBlockEntitiesMixin {

    @Inject(
            method = "renderBlockEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBlockEntities(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, MultiBufferSource.BufferSource bufferSource2, Camera camera, float f, CallbackInfo ci, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            BlockQuakeParticleManager.addQuake(blockPos, poseStack, f);
        }
    }
}
