package com.gaura.mining_and_placing_animations.mixin;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @WrapOperation(
            method = "submitBlockEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;submit(Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V"
            )
    )
    private void onRenderBlockEntities(BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntityRenderState blockEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, Operation<Void> original) {

        if (BlockAnimationManager.isBlockInvisible(blockEntityRenderState.blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockEntityRenderState.blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)));
            }
        }

        original.call(blockEntityRenderDispatcher, blockEntityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    @WrapOperation(
            method = "extractVisibleBlockEntities*",
            at = @At(
                    value = "NEW",
                    target = "(ILcom/mojang/blaze3d/vertex/PoseStack$Pose;)Lnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;"
            )
    )
    private ModelFeatureRenderer.CrumblingOverlay onExtractVisibleBlockEntities(int progress, PoseStack.Pose cameraPose, Operation<ModelFeatureRenderer.CrumblingOverlay> original, @Local(ordinal = 0) PoseStack poseStack, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)));
            }
        }

        return original.call(progress, cameraPose);
    }

    @WrapOperation(
            method = "renderBlockDestroyAnimation",
            at = @At(
                    value = "NEW",
                    target = "(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lcom/mojang/blaze3d/vertex/PoseStack$Pose;F)Lcom/mojang/blaze3d/vertex/SheetedDecalTextureGenerator;"
            )
    )
    private SheetedDecalTextureGenerator onRenderBlockDestroyAnimation(VertexConsumer vertexConsumer, PoseStack.Pose pose, float f, Operation<SheetedDecalTextureGenerator> original, @Local(argsOnly = true, ordinal = 0) PoseStack poseStack, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)));
            }
        }

        return original.call(vertexConsumer, pose, f);
    }

    @WrapMethod(method = "renderHitOutline")
    private void onRenderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, double x, double y, double z, BlockOutlineRenderState blockOutlineRenderState, int i, float g, Operation<Void> original) {

        BlockPos blockPos = blockOutlineRenderState.pos();

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            poseStack.pushPose();

            try {

                BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

                if (blockAnimation != null) {

                    Vec3 center = new Vec3(blockPos.getX() - x, blockPos.getY() - y, blockPos.getZ() - z);

                    poseStack.translate(center);

                    blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)));

                    poseStack.translate(center.reverse());

                    original.call(poseStack, vertexConsumer, x, y, z, blockOutlineRenderState, i, g);
                }
            }
            finally {

                poseStack.popPose();
            }
        }
        else {

            original.call(poseStack, vertexConsumer, x, y, z, blockOutlineRenderState, i, g);
        }
    }
}