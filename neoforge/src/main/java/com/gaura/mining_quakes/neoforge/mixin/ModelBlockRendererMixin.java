package com.gaura.mining_quakes.neoforge.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelBlockRenderer.class)
public class ModelBlockRendererMixin {

    @Redirect(
            method = "tesselateWithAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean onTesselateWithAO(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, BlockState blockState2, Direction direction) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos.relative(direction))) {

            return true;
        }

        return Block.shouldRenderFace(blockGetter, blockPos, blockState, blockState2, direction);
    }

    @Redirect(
            method = "tesselateWithoutAO(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZLnet/minecraft/util/RandomSource;JILnet/neoforged/neoforge/client/model/data/ModelData;Lnet/minecraft/client/renderer/RenderType;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean onTesselateWithoutAO(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, BlockState blockState2, Direction direction) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos.relative(direction))) {

            return true;
        }

        return Block.shouldRenderFace(blockGetter, blockPos, blockState, blockState2, direction);
    }
}
