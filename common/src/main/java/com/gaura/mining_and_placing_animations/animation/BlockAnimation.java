package com.gaura.mining_and_placing_animations.animation;

import com.gaura.mining_and_placing_animations.animation.data.AnimationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class BlockAnimation {

    private final ClientLevel clientLevel;
    private int age;
    private final int lifetime;
    private boolean alive;
    private final BlockPos blockPos;
    private final BlockState blockState;
    private final List<BlockModelPart> blockModelParts;
    private final AnimationModel animationModel;

    public BlockAnimation(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState, AnimationModel animationModel) {

        this.clientLevel = clientLevel;
        this.age = 0;
        this.lifetime = (int) Math.ceil(animationModel.duration * 20);
        this.alive = true;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.blockModelParts = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).collectParts(RandomSource.create(this.blockState.getSeed(this.blockPos)));
        this.animationModel = animationModel;
    }

    public void tick() {

        this.age++;

        if (this.age == this.lifetime) {

            BlockAnimationManager.setBlockVisible(this.clientLevel, this.blockPos, this.blockState);
        }
        else if (this.age > this.lifetime) {

            this.alive = false;
        }
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, Camera camera, float partialTick) {

        poseStack.pushPose();

        poseStack.translate(this.blockPos.getX() - camera.position().x(), this.blockPos.getY() - camera.position().y(), this.blockPos.getZ() - camera.position().z());

        this.animationModel.apply(poseStack, this.getProgress(partialTick));

        Minecraft.getInstance().getBlockRenderer().getModelRenderer().tesselateBlock(
                this.clientLevel,
                this.blockModelParts,
                this.blockState,
                this.blockPos,
                poseStack,
                multiBufferSource.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(this.blockState)),
                false,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
    }

    public float getProgress(float partialTick) {

        return Math.min(1.0F, (this.age + partialTick) / this.lifetime);
    }

    public AnimationModel getAnimationModel() {

        return animationModel;
    }

    public boolean isAlive() {

        return this.alive;
    }
}
