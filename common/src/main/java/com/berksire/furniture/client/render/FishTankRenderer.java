package com.berksire.furniture.client.render;

import com.berksire.furniture.block.FishTankBlock;
import com.berksire.furniture.block.entity.FishTankBlockEntity;
import com.berksire.furniture.client.entity.FakeFishTankEntity;
import com.berksire.furniture.client.model.FishTankModel;
import com.berksire.furniture.registry.ObjectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import org.joml.Quaternionf;

public class FishTankRenderer extends EntityRenderer<FakeFishTankEntity> {

    private final FishTankModel model;
    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation("furniture", "textures/entity/copper_fish_tank.png");
    private static final ResourceLocation IRON_TEXTURE = new ResourceLocation("furniture", "textures/entity/iron_fish_tank.png");

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

    @Override
    public void render(FakeFishTankEntity entity, float f, float g, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight) {
        poseStack.pushPose();
        FishTankBlockEntity blockEntity = entity.getNearestTankEntity().orElse(null);
        if (assertNonNullOrPop(blockEntity, poseStack)) return;

        assert blockEntity != null;
        Direction direction = blockEntity.getBlockState().getValue(FishTankBlock.FACING);
        float rotation = -direction.toYRot();
        Quaternionf theGreatRotator = new Quaternionf().rotateY((float) Math.toRadians(rotation));
        poseStack.mulPose(theGreatRotator);

        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        ResourceLocation texture;
        if (blockEntity.getBlockState().getBlock() == ObjectRegistry.IRON_FISH_TANK.get()) {
            texture = IRON_TEXTURE;
        } else {
            texture = NORMAL_TEXTURE;
        }
        VertexConsumer vertexConsumer = bufferSource.getBuffer(model.renderType(texture));

        Level world = Minecraft.getInstance().level;
        if (assertNonNullOrPop(world, poseStack)) return;
        assert world != null;
        float renderTick = (world.getGameTime() % 24000L) + Minecraft.getInstance().getFrameTime();
        this.model.setupAnim(entity, f, g, renderTick, 0.0F, 0.0F);

        if (blockEntity.getBlockState().getValue(FishTankBlock.HAS_COD)) {
            this.model.cod_1.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.cod_2.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.cod_3.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            this.model.cod_4.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (blockEntity.getBlockState().getValue(FishTankBlock.HAS_PUFFERFISH)) {
            poseStack.pushPose();
            poseStack.translate(0.5, -0.6, 0);
            this.model.pufferfish_1.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.9, -0.8, 0.1);
            this.model.pufferfish_2.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }

        if (blockEntity.getBlockState().getValue(FishTankBlock.HAS_SALMON)) {
            poseStack.pushPose();
            poseStack.translate(0, 0, 0.05);
            this.model.salmon_1.render(poseStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
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

        poseStack.popPose();
    }

    @Override
    @SuppressWarnings("all")
    public ResourceLocation getTextureLocation(FakeFishTankEntity entity) {
        return null;
    }
}
