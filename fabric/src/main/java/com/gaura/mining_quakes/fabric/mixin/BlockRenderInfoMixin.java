package com.gaura.mining_quakes.fabric.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockRenderInfo.class)
public class BlockRenderInfoMixin {

    @Shadow @Final private BlockPos.MutableBlockPos searchPos;

    @Redirect(
            method = "shouldDrawSide",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;shouldRenderFace(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean onShouldRenderFace(BlockState blockState, BlockState blockState2, Direction direction) {

        if (BlockQuakeParticleManager.isBlockInvisible(this.searchPos)) {

            return true;
        }

        return Block.shouldRenderFace(blockState, blockState2, direction);
    }
}
