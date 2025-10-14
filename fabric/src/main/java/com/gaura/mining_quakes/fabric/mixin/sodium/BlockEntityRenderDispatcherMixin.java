package com.gaura.mining_quakes.fabric.mixin.sodium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import com.mojang.blaze3d.vertex.PoseStack;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("sodium"))
@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {

    /**
     * When Sodium is installed, it calls translate() BEFORE calling this vanilla render method.
     * So injecting at HEAD here is actually AFTER the translate, which is exactly what we need
     * for proper synchronization between shake and destroy animations.
     */
    @Inject(
        method = "render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
        at = @At("HEAD")
    )
    private void miningquakes$applyShake(BlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();

        // Safety checks: only run in-world and if block entity exists
        if (mc.player == null || mc.level == null || blockEntity == null) return;

        // Your MiningQuakes effect
        if (BlockQuakeParticleManager.isBlockInvisible(blockEntity.getBlockPos())) {
            BlockQuakeParticleManager.addQuake(blockEntity.getBlockPos(), poseStack, tickDelta);
        }
    }
}
