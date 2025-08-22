package com.gaura.mining_quakes.mixin;

import com.gaura.mining_quakes.MiningQuakes;
import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    private void onRenderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, double x, double y, double z, BlockPos blockPos, BlockState blockState, int i, CallbackInfo ci) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            if (MiningQuakes.CONFIG.renderOutline) {

                Vec3 center = new Vec3(blockPos.getX() - x, blockPos.getY() - y, blockPos.getZ() - z);
                float f = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

                poseStack.translate(center);
                BlockQuakeParticleManager.addQuake(blockPos, poseStack, f);
                poseStack.translate(center.reverse());
            }
            else {

                ci.cancel();
            }
        }
    }
}
