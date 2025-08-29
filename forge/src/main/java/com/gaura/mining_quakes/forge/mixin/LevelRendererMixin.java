package com.gaura.mining_quakes.forge.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
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
    private void onRenderBlockEntities(PoseStack poseStack, float f, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            BlockQuakeParticleManager.addQuake(blockPos, poseStack, f);
        }
    }
}
