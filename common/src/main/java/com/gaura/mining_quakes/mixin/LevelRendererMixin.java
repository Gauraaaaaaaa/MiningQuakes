package com.gaura.mining_quakes.mixin;

import com.gaura.mining_quakes.MiningQuakes;
import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @WrapOperation(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V"
            )
    )
    private void onRenderHitOutline(LevelRenderer levelRenderer, PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, Operation<Void> original, @Local(argsOnly = true) float g) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            if (MiningQuakes.CONFIG.renderOutline) {

                poseStack.pushPose();

                double x = blockPos.getX() - d;
                double y = blockPos.getY() - e;
                double z = blockPos.getZ() - f;

                poseStack.translate(x, y, z);
                BlockQuakeParticleManager.addQuake(blockPos, poseStack, g);
                poseStack.translate(-x, -y, -z);

                original.call(levelRenderer, poseStack, vertexConsumer, entity, d, e, f, blockPos, blockState);

                poseStack.popPose();
            }
        }
        else {

            original.call(levelRenderer, poseStack, vertexConsumer, entity, d, e, f, blockPos, blockState);
        }
    }
}
