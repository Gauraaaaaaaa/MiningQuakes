package com.gaura.mining_quakes.fabric.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SectionCompiler.class)
public class SectionCompilerMixin {

    @Redirect(
            method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"
            )
    )
    private RenderShape onGetRenderShape(BlockState blockState, @Local(ordinal = 2) BlockPos blockPos) {

        if (blockState.getRenderShape() != RenderShape.INVISIBLE && BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            return RenderShape.INVISIBLE;
        }

        return blockState.getRenderShape();
    }

    @Redirect(
            method = "compile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;isSolidRender()Z"
            )
    )
    private boolean onIsSolidRender(BlockState instance, @Local(ordinal = 2) BlockPos blockPos) {

        if (instance.isSolidRender() && BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            return false;
        }

        return instance.isSolidRender();
    }
}
