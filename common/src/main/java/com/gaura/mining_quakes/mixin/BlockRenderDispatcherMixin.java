package com.gaura.mining_quakes.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderDispatcher.class)
public class BlockRenderDispatcherMixin {

    @Inject(method = "renderBreakingTexture", at = @At("HEAD"), cancellable = true)
    private void onRenderBreakingTexture(BlockState blockState, BlockPos blockPos, BlockAndTintGetter blockAndTintGetter, PoseStack poseStack, VertexConsumer vertexConsumer, CallbackInfo ci) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            ci.cancel();
        }
    }
}
