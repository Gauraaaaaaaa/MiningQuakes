package com.gaura.mining_and_placing_animations.fabric.mixin.indium;

import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import link.infra.indium.renderer.render.TerrainRenderContext;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Continuity support thanks to @Arona74
 */
@Restriction(require = @Condition("indium"))
@Mixin(value = TerrainRenderContext.class, remap = false)
public class TerrainRenderContextMixin {

    @Inject(method = "tessellateBlock", at = @At("HEAD"), cancellable = true)
    private void onTessellateBlock(BlockRenderContext blockRenderContext, CallbackInfo ci) {

        if (BlockAnimationManager.isBlockInvisible(blockRenderContext.pos())) {

            ci.cancel();
        }
    }
}