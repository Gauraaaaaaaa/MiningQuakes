package com.gaura.mining_and_placing_animations.mixin.sodium;

import com.gaura.mining_and_placing_animations.animation.BlockAnimation;
import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedSet;

@Restriction(require = @Condition("sodium"))
@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRendererMixin {

    @Inject(
            method = "renderBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void onRenderBlockEntity(PoseStack matrices, RenderBuffers bufferBuilders, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions, float tickDelta, MultiBufferSource.BufferSource immediate, double x, double y, double z, BlockEntityRenderDispatcher dispatcher, BlockEntity entity, LocalPlayer player, LocalBooleanRef isGlowing, CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) PoseStack poseStack, @Local(ordinal = 0) BlockPos blockPos) {

        if (BlockAnimationManager.isBlockInvisible(blockPos)) {

            BlockAnimation blockAnimation = BlockAnimationManager.getAnimation(blockPos);

            if (blockAnimation != null) {

                blockAnimation.getAnimationModel().apply(poseStack, blockAnimation.getProgress(tickDelta));
            }
        }
    }
}
