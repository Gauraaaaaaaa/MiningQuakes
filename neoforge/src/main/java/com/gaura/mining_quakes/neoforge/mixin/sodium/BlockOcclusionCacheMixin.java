package com.gaura.mining_quakes.neoforge.mixin.sodium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockOcclusionCache.class)
public class BlockOcclusionCacheMixin {

    @Inject(method = "shouldDrawSide", at = @At(value = "RETURN"), cancellable = true)
    public void onShouldDrawSide(BlockState selfBlockState, BlockGetter view, BlockPos selfPos, Direction facing, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) BlockPos.MutableBlockPos neighborPos) {

        if (BlockQuakeParticleManager.isBlockInvisible(neighborPos)) {

            cir.setReturnValue(true);
        }
    }
}