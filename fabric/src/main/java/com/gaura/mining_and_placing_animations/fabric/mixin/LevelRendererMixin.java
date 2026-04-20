package com.gaura.mining_and_placing_animations.fabric.mixin;

import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.state.LevelRenderState;
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
    private RenderBuffers renderBuffers;

    @Shadow
    @Final
    private Minecraft minecraft;

    /**
     * Forced to do a mixin to render after entities since WorldRenderEvents.AFTER_ENTITIES doesn't exist in 1.21.9
     */
    @Inject(method = "renderBlockDestroyAnimation", at = @At("HEAD"))
    private void renderAfterEntities(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, LevelRenderState levelRenderState, CallbackInfo ci) {

        MultiBufferSource.BufferSource buffers = this.renderBuffers.bufferSource();

        BlockAnimationManager.render(
                poseStack,
                buffers,
                this.minecraft.gameRenderer.getMainCamera(),
                this.minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false)
        );

        buffers.endBatch();
    }
}
