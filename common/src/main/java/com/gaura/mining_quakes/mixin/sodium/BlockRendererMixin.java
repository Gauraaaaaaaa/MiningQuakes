package com.gaura.mining_quakes.mixin.sodium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("sodium"))
@Mixin(BlockRenderer.class)
public class BlockRendererMixin {

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    private void onRenderModel(BakedModel bakedModel, BlockState blockState, BlockPos blockPos, BlockPos originBlockPos, CallbackInfo ci) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            ci.cancel();
        }
    }
}
