package com.gaura.mining_and_placing_animations.fabric.mixin.iris;

import com.gaura.mining_and_placing_animations.animation.BlockAnimationManager;
import com.mojang.blaze3d.vertex.PoseStack;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.irisshaders.iris.mixin.LevelRendererAccessor;
import net.irisshaders.iris.shadows.ShadowRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition("iris"))
@Mixin(ShadowRenderer.class)
public class ShadowRendererMixin {

    @Inject(method = "renderEntities", at = @At("HEAD"))
    private void onRenderEntities(LevelRendererAccessor levelRendererAccessor, EntityRenderDispatcher entityRenderDispatcher, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, float tickDelta, Frustum frustum, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Integer> cir) {

        BlockAnimationManager.render(poseStack, bufferSource, Minecraft.getInstance().gameRenderer.getMainCamera(), tickDelta);
        bufferSource.endBatch();
    }
}