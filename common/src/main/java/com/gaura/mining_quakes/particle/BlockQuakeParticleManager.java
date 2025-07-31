package com.gaura.mining_quakes.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class BlockQuakeParticleManager {

    private static final Map<BlockPos, BlockQuakeParticle> BLOCK_QUAKE_PARTICLES = new HashMap<>();

    private static final Set<BlockPos> INVISIBLE_BLOCKS = new CopyOnWriteArraySet<>();

    public static void addQuakeAnimation(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState, Direction direction, boolean isDoorBlock) {

        BlockQuakeParticle blockQuakeParticle = new BlockQuakeParticle(clientLevel, blockPos, blockState, direction, isDoorBlock);

        BlockQuakeParticle previousBlockQuakeParticle = BLOCK_QUAKE_PARTICLES.put(blockPos, blockQuakeParticle);

        if (previousBlockQuakeParticle != null) {

            previousBlockQuakeParticle.remove();
        }

        Minecraft.getInstance().particleEngine.add(blockQuakeParticle);

        setBlockInvisible(clientLevel, blockPos, blockState);
    }

    public static void removeQuakeAnimation(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState) {

        BlockQuakeParticle blockQuakeParticle = BLOCK_QUAKE_PARTICLES.remove(blockPos);

        if (blockQuakeParticle != null) {

            blockQuakeParticle.remove();
        }

        setBlockVisible(clientLevel, blockPos, blockState);
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
