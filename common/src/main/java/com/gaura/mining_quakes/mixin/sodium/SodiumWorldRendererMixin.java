package com.gaura.mining_quakes.mixin.sodium;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
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
    private static void onRenderBlockEntity(PoseStack poseStack, RenderBuffers renderBuffers, Long2ObjectMap<SortedSet<BlockDestructionProgress>> blockBreakingProgressions, float f, MultiBufferSource.BufferSource bufferSource, double x, double y, double z, BlockEntityRenderDispatcher blockEntityRenderDispatcher, BlockEntity blockEntity, LocalPlayer player, LocalBooleanRef isGlowing, CallbackInfo ci) {

        if (BlockQuakeParticleManager.isBlockInvisible(blockEntity.getBlockPos())) {

            BlockQuakeParticleManager.addQuake(blockEntity.getBlockPos(), poseStack, f);
        }
    }
}
