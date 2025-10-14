package com.gaura.mining_quakes.fabric.mixin.indium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("indium"))
@Pseudo
@Mixin(targets = "link.infra.indium.renderer.render.TerrainRenderContext", remap = false)
public class IndiumTerrainRenderContextMixin {
    @Inject(method = "tessellateBlock", at = @At("HEAD"), cancellable = true, remap = false, require = 0)
    private void cancelInvisibleBlocks(BlockRenderContext ctx, CallbackInfo ci) {
        if (BlockQuakeParticleManager.isBlockInvisible(ctx.pos())) {
            ci.cancel();
        }
    }
}