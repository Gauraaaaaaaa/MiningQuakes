package com.gaura.mining_quakes.neoforge.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SectionCompiler.class)
public class SectionCompilerMixin {

    @WrapOperation(
            method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderSectionRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"
            )
    )
    private RenderShape onGetRenderShape(BlockState blockState, Operation<RenderShape> original, @Local(ordinal = 2) BlockPos blockPos) {

        if (original.call(blockState) != RenderShape.INVISIBLE && BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            return RenderShape.INVISIBLE;
        }

        return original.call(blockState);
    }

    @WrapOperation(
            method = "compile(Lnet/minecraft/core/SectionPos;Lnet/minecraft/client/renderer/chunk/RenderSectionRegion;Lcom/mojang/blaze3d/vertex/VertexSorting;Lnet/minecraft/client/renderer/SectionBufferBuilderPack;Ljava/util/List;)Lnet/minecraft/client/renderer/chunk/SectionCompiler$Results;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;isSolidRender()Z"
            )
    )
    private boolean onIsSolidRender(BlockState blockState, Operation<Boolean> original, @Local(ordinal = 2) BlockPos blockPos) {

        if (original.call(blockState) && BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            return false;
        }

        return original.call(blockState);
    }
}
