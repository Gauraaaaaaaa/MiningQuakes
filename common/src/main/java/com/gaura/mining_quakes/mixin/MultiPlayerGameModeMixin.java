package com.gaura.mining_quakes.mixin;

import com.gaura.mining_quakes.MiningQuakes;
import com.gaura.mining_quakes.particle.BlockQuakeParticleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
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

        if (clientLevel != null && BlockQuakeParticleManager.isBlockInvisible(blockPos)) {

            BlockState blockState = clientLevel.getBlockState(blockPos);
            BlockQuakeParticleManager.removeQuakeAnimation(clientLevel, blockPos, blockState);

            if (blockState.getBlock() instanceof DoorBlock) {

                BlockPos otherBlockPos = blockPos.relative(blockState.getValue(DoorBlock.HALF).getDirectionToOther());
                BlockState otherBlockState = clientLevel.getBlockState(otherBlockPos);
                BlockQuakeParticleManager.removeQuakeAnimation(clientLevel, otherBlockPos, otherBlockState);
            }
            else if (blockState.getBlock() instanceof ChestBlock && blockState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {

                BlockPos otherBlockPos = blockPos.relative(ChestBlock.getConnectedDirection(blockState));
                BlockState otherBlockState = clientLevel.getBlockState(otherBlockPos);
                BlockQuakeParticleManager.removeQuakeAnimation(clientLevel, otherBlockPos, otherBlockState);
            }
            else if (blockState.getBlock() instanceof BedBlock) {

                BlockPos otherBlockPos = blockPos.relative(BedBlock.getConnectedDirection(blockState));
                BlockState otherBlockState = clientLevel.getBlockState(otherBlockPos);
                BlockQuakeParticleManager.removeQuakeAnimation(clientLevel, otherBlockPos, otherBlockState);
            }
        }
    }

    @Inject(method = "continueDestroyBlock", at = @At("HEAD"))
    private void onContinueDestroyBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {

        ClientLevel clientLevel = this.minecraft.level;

        if (clientLevel != null) {

            BlockState blockState = clientLevel.getBlockState(blockPos);

            if (!BlockQuakeParticleManager.isBlockInvisible(blockPos) && !MiningQuakes.isBlockBlacklisted(blockState)) {

                int randomHorizontal = RandomSource.create().nextBoolean() ? 1 : -1;
                int randomVertical = RandomSource.create().nextBoolean() ? 1 : -1;

                BlockQuakeParticleManager.addQuakeAnimation(clientLevel, blockPos.immutable(), blockState, direction, randomHorizontal, randomVertical);

                if (blockState.getBlock() instanceof DoorBlock) {

                    BlockPos otherBlockPos = blockPos.relative(blockState.getValue(DoorBlock.HALF).getDirectionToOther());
                    BlockState otherBlockState = clientLevel.getBlockState(otherBlockPos);
                    BlockQuakeParticleManager.addQuakeAnimation(clientLevel, otherBlockPos.immutable(), otherBlockState, direction, randomHorizontal, randomVertical);
                }
                else if (blockState.getBlock() instanceof ChestBlock && blockState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {

                    BlockPos otherBlockPos = blockPos.relative(ChestBlock.getConnectedDirection(blockState));
                    BlockState otherBlockState = clientLevel.getBlockState(otherBlockPos);
                    BlockQuakeParticleManager.addQuakeAnimation(clientLevel, otherBlockPos.immutable(), otherBlockState, direction, randomHorizontal, randomVertical);
                }
                else if (blockState.getBlock() instanceof BedBlock) {

                    BlockPos otherBlockPos = blockPos.relative(BedBlock.getConnectedDirection(blockState));
                    BlockState otherBlockState = clientLevel.getBlockState(otherBlockPos);
                    BlockQuakeParticleManager.addQuakeAnimation(clientLevel, otherBlockPos.immutable(), otherBlockState, direction, randomHorizontal, randomVertical);
                }
            }
        }
    }
}
