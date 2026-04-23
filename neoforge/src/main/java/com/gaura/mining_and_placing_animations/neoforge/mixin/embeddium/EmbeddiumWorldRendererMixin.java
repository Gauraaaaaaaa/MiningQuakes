package com.gaura.mining_and_placing_animations.neoforge.mixin.embeddium;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.embeddedt.embeddium.impl.render.EmbeddiumWorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedSet;

@Restriction(require = @Condition("embeddium"))
@Mixin(EmbeddiumWorldRenderer.class)
public class EmbeddiumWorldRendererMixin {

    @Inject(
            method = "renderBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void onRenderBlockEntity(PoseStack poseStack, RenderBuffers renderBuffers, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockDestructionProgressSet, float tickDelta, MultiBufferSource.BufferSource bufferSource, double x, double y, double z, BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, CallbackInfo ci) {

        if (BlockAnimationManager.isBlockInvisible(blockEntity.getBlockPos())) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockEntity.getBlockPos());

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(tickDelta));
            }
        }
    }
}