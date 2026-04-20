package com.gaura.mining_and_placing_animations.fabric.mixin;

import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.TerrainRenderContext;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TerrainRenderContext.class)
public class TerrainRenderContextMixin {

    @Inject(method = "bufferModel", at = @At("HEAD"), cancellable = true)
    public void bufferModel(BlockStateModel model, BlockState blockState, BlockPos blockPos, CallbackInfo ci) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            ci.cancel();
        }
    }
}