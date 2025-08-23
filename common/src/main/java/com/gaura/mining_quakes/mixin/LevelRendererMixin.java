package com.gaura.mining_quakes.mixin;

import com.gaura.mining_quakes.MiningQuakes;
import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @WrapOperation(
            method = "renderHitOutline",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDFFFF)V"
            )
    )
    private void onRenderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, float g, float h, float i, float j, Operation<Void> original, @Local(argsOnly = true) BlockPos blockPos) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            if (MiningQuakes.CONFIG.renderOutline) {

                float f = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);

                poseStack.pushPose();

                poseStack.translate(x, y, z);
                BlockQuakeParticleManager.addQuake(blockPos, poseStack, f);
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
