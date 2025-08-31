package com.gaura.mining_quakes.fabric.mixin.sodium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition("sodium"))
@Mixin(value = BlockOcclusionCache.class, remap = false)
public class BlockOcclusionCacheMixin {
    
    @Inject(
            method = "shouldDrawSide",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onShouldDrawSide(BlockState selfState, BlockGetter world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        // Calculate the neighbor position
        BlockPos neighborPos = pos.relative(direction);
        
        if (BlockQuakeParticleManager.isBlockInvisible(neighborPos)) {
            cir.setReturnValue(true);
        }
    }
}