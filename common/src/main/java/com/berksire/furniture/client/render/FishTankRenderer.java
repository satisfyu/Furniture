package com.berksire.furniture.client.render;

import com.berksire.furniture.block.FishTankBlock;
import com.berksire.furniture.block.entity.FishTankBlockEntity;
import com.berksire.furniture.client.entity.ActualFishTankEntity;
import com.berksire.furniture.client.model.FishTankModel;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import oshi.util.tuples.Pair;

import java.util.LinkedHashMap;
import java.util.Map;

public class FishTankRenderer extends EntityRenderer<ActualFishTankEntity> {

    private final FishTankModel model;
    private static final ResourceLocation TEXTURE = new ResourceLocation("furniture", "textures/entity/fish_tank.png");

    public FishTankRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FishTankModel(context.bakeLayer(FishTankModel.LAYER_LOCATION));
    }

    private static boolean assertNonNullOrPop(Object nullable, PoseStack stack) {
        if (nullable == null) {
            stack.popPose();
            return true;
        } else return false;
    }

    /*Map<Direction, Pair<Vec2, Vec2>> finishVectors = new LinkedHashMap<>() {{
        put(Direction.NORTH, new Vec2(0, 0));
        put(Direction.SOUTH, new Vec2(1, 1));
        put(Direction.EAST, new Vec2(1, 0));
        put(Direction.WEST, new Vec2(0, 1));
    }};*/

    @Override
    public void render(ActualFishTankEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight) {
        poseStack.pushPose();
        FishTankBlockEntity blockEntity = entity.getNearestTankEntity().orElse(null);
        if (assertNonNullOrPop(blockEntity, poseStack)) return;

        Direction direction = blockEntity.getBlockState().getValue(FishTankBlock.FACING);
        float rotation = -direction.toYRot();
        Quaternionf theGreatRotator = new Quaternionf().rotateY((float) Math.toRadians(rotation));
        poseStack.mulPose(theGreatRotator);
        // mimic the block rotation

        poseStack.mulPose(Axis.XP.rotationDegrees(180));
        // flippity flip, models are upside down

        VertexConsumer vertexConsumer = bufferSource.getBuffer(model.renderType(TEXTURE));
        // pass texture to entity shader, [insert wow sound effect]

        Level world = Minecraft.getInstance().level;
        if (assertNonNullOrPop(world, poseStack)) return;
        float renderTick = (world.getGameTime() % 24000L) + Minecraft.getInstance().getFrameTime();
        this.model.setupAnim(entity, f, g, renderTick, 0.0F, 0.0F);

        if (blockEntity.getBlockState().getValue(FishTankBlock.HAS_COD)) {
            this.model.cod_1.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.cod_2.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.cod_3.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.cod_4.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (blockEntity.getBlockState().getValue(FishTankBlock.HAS_PUFFERFISH)) {
            this.model.pufferfish_1.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.pufferfish_2.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (blockEntity.getBlockState().getValue(FishTankBlock.HAS_SALMON)) {
            this.model.salmon_1.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.salmon_2.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.model.tank.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.model.decoration.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        poseStack.pushPose();

        poseStack.mulPose(theGreatRotator);
        poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));
        poseStack.translate(-0.5, 0, -0.5);
        FluidRenderer.renderFluidBox(FluidStack.create(Fluids.WATER, 100L), 1f / 16 + 1f / 128, 2f / 16, 1f / 16 + 1f / 128, 15f / 16 - 1f / 128, 13.6f / 16, 31f / 16 - 1f / 128, bufferSource, poseStack, combinedLight, false);
        // No, not Litres. I keep telling myself L means long. It takes a long, ok?

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ActualFishTankEntity entity) {
        return null;
    }
}
