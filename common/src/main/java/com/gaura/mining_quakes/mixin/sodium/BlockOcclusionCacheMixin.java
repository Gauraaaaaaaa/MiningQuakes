package com.gaura.mining_quakes.mixin.sodium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition("sodium"))
@Mixin(BlockOcclusionCache.class)
public class BlockOcclusionCacheMixin {

    @Inject(
            method = "shouldDrawSide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/core/BlockPos$MutableBlockPos;setWithOffset(Lnet/minecraft/core/Vec3i;Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos$MutableBlockPos;",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void onShouldDrawSide(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) BlockPos.MutableBlockPos neighborBlockPos) {

        if (BlockQuakeParticleManager.isBlockInvisible(neighborBlockPos.setWithOffset(blockPos, direction))) {

            cir.setReturnValue(true);
        }
    }
}