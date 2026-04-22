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
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
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

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false)));
            }
        }

        return original.call(vertexConsumer, pose, f);
    }

    @WrapMethod(method = "renderHitOutline")
    private void onRenderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, double x, double y, double z, BlockPos blockPos, BlockState blockState, int i, Operation<Void> original) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            poseStack.pushPose();

            try {

                BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

                if (blockAnimation != null) {

                    Vec3 center = new Vec3(blockPos.getX() - x, blockPos.getY() - y, blockPos.getZ() - z);

                    poseStack.translate(center);

                    blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false)));

                    poseStack.translate(center.reverse());

                    original.call(poseStack, vertexConsumer, entity, x, y, z, blockPos, blockState, i);
                }
            }
            finally {

                poseStack.popPose();
            }
        }
        else {

            original.call(poseStack, vertexConsumer, entity, x, y, z, blockPos, blockState, i);
        }
    }
}