package com.gaura.mining_and_placing_animations.mixin;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
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
                    ordinal = 2,
                    shift = At.Shift.AFTER
            )
    )
    private void onRenderBlockDestroyAnimation(PoseStack poseStack, float tickDelta, long l, boolean bl, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(tickDelta));
            }
        }
    }

    @WrapOperation(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
            )
    )
    private void onRenderHitOutline(LevelRenderer levelRenderer, PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, double x, double y, double z, BlockPos blockPos, BlockState blockState, Operation<Void> original, @Local(argsOnly = true, ordinal = 0) float tickDelta) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                poseStack.pushPose();

                double offsetX = (double) blockPos.getX() - x;
                double offsetY = (double) blockPos.getY() - y;
                double offsetZ = (double) blockPos.getZ() - z;

                poseStack.translate(offsetX, offsetY, offsetZ);

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(tickDelta));

                original.call(levelRenderer, poseStack, vertexConsumer, entity, (double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ(), blockPos, blockState);

                poseStack.popPose();
            }
        }
        else {

            original.call(levelRenderer, poseStack, vertexConsumer, entity, x, y, z, blockPos, blockState);
        }
    }
}