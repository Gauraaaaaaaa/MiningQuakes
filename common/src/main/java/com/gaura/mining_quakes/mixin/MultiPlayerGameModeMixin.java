package com.gaura.mining_quakes.mixin;

import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void onDestroyBlock(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {

        ClientLevel clientLevel = this.minecraft.level;

        if (clientLevel != null) {

            BlockState blockState = clientLevel.getBlockState(blockPos);
            BlockQuakeParticleManager.removeQuakeAnimation(clientLevel, blockPos, blockState);

            if (blockState.getBlock() instanceof DoorBlock) {

                DoubleBlockHalf doubleBlockHalf = blockState.getValue(DoorBlock.HALF);

                if (doubleBlockHalf == DoubleBlockHalf.LOWER) {

                    BlockState aboveBlockState = clientLevel.getBlockState(blockPos.above());
                    BlockQuakeParticleManager.removeQuakeAnimation(clientLevel, blockPos.above(), aboveBlockState);
                }
                else {

                    BlockState belowBlockState = clientLevel.getBlockState(blockPos.below());
                    BlockQuakeParticleManager.removeQuakeAnimation(clientLevel, blockPos.below(), belowBlockState);
                }
            }
        }
    }

    @Inject(method = "continueDestroyBlock", at = @At("HEAD"))
    private void onContinueDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {

        ClientLevel clientLevel = this.minecraft.level;

        if (clientLevel != null && !BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            BlockState blockState = clientLevel.getBlockState(blockPos);
            int randomHorizontal = RandomSource.create().nextBoolean() ? 1 : -1;
            int randomVertical = RandomSource.create().nextBoolean() ? 1 : -1;

            if (blockState.getBlock() instanceof DoorBlock) {

                BlockQuakeParticleManager.addQuakeAnimation(clientLevel, blockPos, blockState, direction, true, randomHorizontal, randomVertical);

                DoubleBlockHalf doubleBlockHalf = blockState.getValue(DoorBlock.HALF);

                if (doubleBlockHalf == DoubleBlockHalf.LOWER) {

                    BlockState aboveBlockState = clientLevel.getBlockState(blockPos.above());
                    BlockQuakeParticleManager.addQuakeAnimation(clientLevel, blockPos.above(), aboveBlockState, direction, true, randomHorizontal, randomVertical);
                }
                else {

                    BlockState belowBlockState = clientLevel.getBlockState(blockPos.below());
                    BlockQuakeParticleManager.addQuakeAnimation(clientLevel, blockPos.below(), belowBlockState, direction, true, randomHorizontal, randomVertical);
                }
            }
            else {

                BlockQuakeParticleManager.addQuakeAnimation(clientLevel, blockPos, blockState, direction, false, randomHorizontal, randomVertical);
            }
        }
    }
}
