package com.gaura.mining_quakes.particle;

import com.gaura.mining_quakes.MiningQuakes;
import com.gaura.mining_quakes.mixin.ClientLevelAccessor;
import com.gaura.mining_quakes.mixin.LevelRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.SortedSet;

public class BlockQuakeParticle extends Particle {

    private final BlockPos blockPos;
    private final BlockState blockState;
    private final BakedModel bakedModel;
    private final RandomSource source = RandomSource.create();

    public BlockQuakeParticle(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState) {

        super(clientLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ());

        this.age = 0;
        this.lifetime = MiningQuakes.CONFIG.lifetime;
        this.hasPhysics = false;

        this.blockPos = blockPos;
        this.blockState = blockState;
        this.bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
    }

    @Override
    public void tick() {

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.age++;

        if (this.age == this.lifetime) {

            BlockQuakeParticleManager.setBlockVisible(this.level, this.blockPos, this.blockState);
        }
        else if (this.age > this.lifetime) {

            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float f) {}

    @Override
    public void renderCustom(PoseStack poseStack, MultiBufferSource multiBufferSource, Camera camera, float f) {

        poseStack.pushPose();

        float x = (float) (Mth.lerp(f, this.xo, this.x) - camera.getPosition().x());
        float y = (float) (Mth.lerp(f, this.yo, this.y) - camera.getPosition().y());
        float z = (float) (Mth.lerp(f, this.zo, this.z) - camera.getPosition().z());

        poseStack.translate(x, y, z);

        float t = (this.age + f) / this.lifetime;
        float maxAngle = MiningQuakes.CONFIG.maxAngle;
        float baseOscillation = (float) Math.sin(MiningQuakes.CONFIG.oscillation * Math.PI * t);
        float decay = MiningQuakes.CONFIG.easingFunction.apply(t);
        float angle = maxAngle * baseOscillation * decay;

        poseStack.translate(0.5F, 0.5F, 0.5F);

        poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        poseStack.translate(-0.5F, -0.5F, -0.5F);

        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        blockRenderer.getModelRenderer().tesselateBlock(
                this.level,
                this.bakedModel,
                this.blockState,
                this.blockPos,
                poseStack,
                multiBufferSource.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(this.blockState)),
                false,
                this.source,
                this.blockState.getSeed(this.blockPos),
                OverlayTexture.NO_OVERLAY
        );

        Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress = ((LevelRendererAccessor) ((ClientLevelAccessor) this.level).getLevelRenderer()).getDestructionProgress();
        SortedSet<BlockDestructionProgress> progressSet = destructionProgress.get(this.blockPos.asLong());

        if (progressSet != null && !progressSet.isEmpty()) {

            blockRenderer.getModelRenderer().tesselateBlock(
                    this.level,
                    this.bakedModel,
                    this.blockState,
                    this.blockPos,
                    poseStack,
                    new SheetedDecalTextureGenerator(multiBufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(progressSet.last().getProgress())), poseStack.last(), 1.0F),
                    true,
                    this.source,
                    this.blockState.getSeed(this.blockPos),
                    OverlayTexture.NO_OVERLAY
            );
        }

        poseStack.popPose();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {

        return ParticleRenderType.CUSTOM;
    }
}
