package com.gaura.mining_quakes.particle;

import com.gaura.mining_quakes.MiningQuakes;
import com.gaura.mining_quakes.mixin.ClientLevelAccessor;
import com.gaura.mining_quakes.mixin.LevelRendererAccessor;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SortedSet;

public class BlockQuakeParticle extends Particle {

    private final BlockPos blockPos;
    private final BlockState blockState;
    private final Direction direction;

    private final ModelBlockRenderer modelBlockRenderer;
    private final BakedModel bakedModel;
    private final RandomSource source;

    private Vec3 pivotPoint;

    private final int randomHorizontal;
    private final int randomVertical;

    record RotationPair(Axis horizontalAxis, Axis verticalAxis) {}

    static final Map<Direction, RotationPair> ROTATIONS = Map.of(
            Direction.SOUTH, new RotationPair(Axis.ZP, Axis.YP),
            Direction.NORTH, new RotationPair(Axis.ZN, Axis.YP),
            Direction.EAST,  new RotationPair(Axis.XP, Axis.YP),
            Direction.WEST,  new RotationPair(Axis.XN, Axis.YP),
            Direction.DOWN,  new RotationPair(Axis.XP, Axis.ZP),
            Direction.UP,    new RotationPair(Axis.XN, Axis.ZP)
    );

    public BlockQuakeParticle(ClientLevel clientLevel, BlockPos blockPos, BlockState blockState, Direction direction, int randomHorizontal, int randomVertical) {

        super(clientLevel, blockPos.getX(), blockPos.getY(), blockPos.getZ());

        this.age = 0;
        this.lifetime = MiningQuakes.CONFIG.lifetime;
        this.hasPhysics = false;

        this.blockPos = blockPos;
        this.blockState = blockState;
        this.direction = direction;

        this.modelBlockRenderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
        this.bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
        this.source = RandomSource.create();

        this.pivotPoint = new Vec3(0.5, 0.5, 0.5);

        if (blockState.getBlock() instanceof DoorBlock) {

            this.pivotPoint = this.pivotPoint.relative(blockState.getValue(DoorBlock.HALF).getDirectionToOther(), 0.5);
        }
        else if (blockState.getBlock() instanceof ChestBlock && blockState.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {

            this.pivotPoint = this.pivotPoint.relative(ChestBlock.getConnectedDirection(blockState), 0.5);
        }
        else if (blockState.getBlock() instanceof BedBlock) {

            this.pivotPoint = this.pivotPoint.relative(BedBlock.getConnectedDirection(blockState), 0.5);
        }

        this.randomHorizontal = randomHorizontal;
        this.randomVertical = randomVertical;
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
    public void render(VertexConsumer vertexConsumer, Camera camera, float f) {

        PoseStack poseStack = new PoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        float x = (float) (Mth.lerp(f, this.xo, this.x) - camera.getPosition().x());
        float y = (float) (Mth.lerp(f, this.yo, this.y) - camera.getPosition().y());
        float z = (float) (Mth.lerp(f, this.zo, this.z) - camera.getPosition().z());

        poseStack.translate(x, y, z);

        this.animate(poseStack, f);

        this.modelBlockRenderer.tesselateBlock(
                this.level,
                this.bakedModel,
                this.blockState,
                this.blockPos,
                poseStack,
                bufferSource.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(this.blockState)),
                false,
                this.source,
                this.blockState.getSeed(this.blockPos),
                OverlayTexture.NO_OVERLAY
        );

        Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress = ((LevelRendererAccessor) ((ClientLevelAccessor) this.level).getLevelRenderer()).getDestructionProgress();
        SortedSet<BlockDestructionProgress> progressSet = destructionProgress.get(this.blockPos.asLong());

        if (progressSet != null && !progressSet.isEmpty()) {

            this.modelBlockRenderer.tesselateBlock(
                    this.level,
                    this.bakedModel,
                    this.blockState,
                    this.blockPos,
                    poseStack,
                    new SheetedDecalTextureGenerator(bufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(progressSet.last().getProgress())), poseStack.last(), 1.0F),
                    true,
                    this.source,
                    this.blockState.getSeed(this.blockPos),
                    OverlayTexture.NO_OVERLAY
            );
        }

        bufferSource.endBatch();
    }

    public void animate(PoseStack poseStack, float f) {

        float t = (this.age + f) / this.lifetime;
        float horizontalOscillations = (float) Math.sin(MiningQuakes.CONFIG.horizontalOscillations * Math.PI * t);
        float verticalOscillations = (float) Math.sin(MiningQuakes.CONFIG.verticalOscillations * Math.PI * t);
        float decay = MiningQuakes.CONFIG.easingFunction.apply(t);
        float horizontalAngle = MiningQuakes.CONFIG.horizontalMaxAngle * horizontalOscillations * decay;
        float verticalAngle = MiningQuakes.CONFIG.verticalMaxAngle * verticalOscillations * decay;

        if (MiningQuakes.CONFIG.randomQuakes) {

            horizontalAngle *= this.randomHorizontal;
            verticalAngle *= this.randomVertical;
        }

        RotationPair rotation = ROTATIONS.get(this.direction);

        poseStack.translate(this.pivotPoint.x, this.pivotPoint.y, this.pivotPoint.z);

        poseStack.mulPose(rotation.horizontalAxis.rotationDegrees(verticalAngle));
        poseStack.mulPose(rotation.verticalAxis.rotationDegrees(horizontalAngle));

        poseStack.translate(-this.pivotPoint.x, -this.pivotPoint.y, -this.pivotPoint.z);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {

        return ParticleRenderType.CUSTOM;
    }
}
