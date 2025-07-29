package com.gaura.mining_quakes.neoforge.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

    @WrapOperation(
            method = "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Ljava/util/List;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/function/Function;ZI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;shouldRenderFace(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;ZLnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean onTesselateWithAO(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, BlockState blockState, boolean bl, Direction direction, BlockPos searchPos, Operation<Boolean> original) {

        if (BlockQuakeParticleManager.isBlockInvisible(searchPos)) {

            return true;
        }

        return original.call(blockAndTintGetter, blockPos, blockState, bl, direction, searchPos);
    }

    @WrapOperation(
            method = "tesselateWithoutAO(Lnet/minecraft/world/level/BlockAndTintGetter;Ljava/util/List;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/function/Function;ZI)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;shouldRenderFace(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;ZLnet/minecraft/core/Direction;Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean onTesselateWithoutAO(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, BlockState blockState, boolean bl, Direction direction, BlockPos searchPos, Operation<Boolean> original) {

        if (BlockQuakeParticleManager.isBlockInvisible(searchPos)) {

            return true;
        }

        return original.call(blockAndTintGetter, blockPos, blockState, bl, direction, searchPos);
    }
}
