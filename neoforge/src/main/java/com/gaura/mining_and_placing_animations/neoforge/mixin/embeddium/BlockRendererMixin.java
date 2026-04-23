package com.gaura.mining_and_placing_animations.neoforge.mixin.embeddium;

import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.embeddedt.embeddium.api.render.chunk.BlockRenderContext;
import org.embeddedt.embeddium.impl.render.chunk.compile.ChunkBuildBuffers;
import org.embeddedt.embeddium.impl.render.chunk.compile.pipeline.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("embeddium"))
@Mixin(value = BlockRenderer.class, remap = false)
public class BlockRendererMixin {

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    private void onRenderModel(BlockRenderContext blockRenderContext, ChunkBuildBuffers buffers, CallbackInfo ci) {

        if (BlockAnimationManager.isBlockInvisible(blockRenderContext.pos())) {

            ci.cancel();
        }
    }
}