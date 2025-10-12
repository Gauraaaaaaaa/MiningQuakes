package com.gaura.mining_quakes.forge.mixin.embedium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("embeddium"))
@Mixin(value = BlockRenderer.class, remap = false)
public class BlockRendererMixin {

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    private void onRenderModel(BlockRenderContext ctx, ChunkBuildBuffers buffers, CallbackInfo ci) {

        if (BlockQuakeParticleManager.isBlockInvisible(ctx.pos())) {

            ci.cancel();
        }
    }
}
