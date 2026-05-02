package com.gaura.mining_and_placing_animations.forge.mixin.embeddium;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedSet;

@Restriction(require = @Condition("embeddium"))
@Mixin(value = SodiumWorldRenderer.class, remap = false)
public class SodiumWorldRendererMixin {

    @Inject(
            method = "renderBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;m_85837_(DDD)V",
                    shift = At.Shift.AFTER,
                    remap = true
            )
    )
    private static void onRenderBlockEntity(PoseStack poseStack, RenderBuffers bufferBuilders, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions, float tickDelta, MultiBufferSource.BufferSource bufferSource, double x, double y, double z, BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, CallbackInfo ci) {

        if (BlockAnimationManager.isBlockInvisible(blockEntity.getBlockPos())) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockEntity.getBlockPos());

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(tickDelta));
            }
        }
    }
}