package com.gaura.mining_and_placing_animations.animation;

import com.gaura.mining_and_placing_animations.animation.data.AnimationModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Predicate;

public class BlockAnimationManager {

    private static final Map<BlockPos, BlockAnimation> ANIMATED_BLOCKS = new HashMap<>();

    private static final Set<BlockPos> INVISIBLE_BLOCKS = new CopyOnWriteArraySet<>();

    public static void addAnimation(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState, AnimationModel animationModel) {

        ANIMATED_BLOCKS.put(blockPos, new BlockAnimation(clientLevel, blockPos, blockState, animationModel));
        setBlockInvisible(clientLevel, blockPos, blockState);
    }

    public static void removeAnimation(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState) {

        ANIMATED_BLOCKS.remove(blockPos);
        setBlockVisible(clientLevel, blockPos, blockState);
    }

    @Nullable
    public static BlockAnimation getAnimation(BlockPos blockPos) {

        return ANIMATED_BLOCKS.get(blockPos);
    }

    public static void tick() {

        ANIMATED_BLOCKS.values().forEach(BlockAnimation::tick);
        ANIMATED_BLOCKS.values().removeIf(Predicate.not(BlockAnimation::isAlive));
    }

    public static void render(PoseStack poseStack, MultiBufferSource multiBufferSource, Camera camera, float partialTick) {

        ANIMATED_BLOCKS.values().forEach(blockAnimation -> blockAnimation.render(poseStack, multiBufferSource, camera, partialTick));
    }

    public static void setBlockInvisible(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState) {

        if (INVISIBLE_BLOCKS.add(blockPos)) {

            updateBlock(clientLevel, blockPos, blockState);
        }
    }

    public static void setBlockVisible(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState) {

        if (INVISIBLE_BLOCKS.remove(blockPos)) {

            updateBlock(clientLevel, blockPos, blockState);
        }
    }

    public static boolean isBlockInvisible(BlockPos blockPos) {

        return INVISIBLE_BLOCKS.contains(blockPos);
    }

    private static void updateBlock(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState) {

        clientLevel.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS);
    }
}
