package com.gaura.mining_and_placing_animations.mixin;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
                    ordinal = 2,
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBlockDestroyAnimation(DeltaTracker deltaTracker, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local(ordinal = 0) PoseStack poseStack, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(deltaTracker.getGameTimeDeltaPartialTick(false)));
            }
        }
    }

    @WrapOperation(
            method = "renderHitOutline",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDFFFF)V"
            )
    )
    private void onRenderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, float g, float h, float i, float j, Operation<Void> original, @Local(argsOnly = true) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                poseStack.pushPose();

                poseStack.translate(x, y, z);
                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false)));
                poseStack.translate(-x, -y, -z);

                original.call(poseStack, vertexConsumer, voxelShape, x, y, z, g, h, i, j);

                poseStack.popPose();
            }
        }
        else {

            original.call(poseStack, vertexConsumer, voxelShape, x, y, z, g, h, i, j);
        }
    }
}