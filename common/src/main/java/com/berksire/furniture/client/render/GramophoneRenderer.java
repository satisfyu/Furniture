package com.berksire.furniture.client.render;

import com.berksire.furniture.block.GramophoneBlock;
import com.berksire.furniture.block.entity.GramophoneBlockEntity;
import com.berksire.furniture.client.model.GramophoneModel;
import com.berksire.furniture.util.FurnitureIdentifier;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.joml.Quaternionf;

import java.util.Objects;

public class GramophoneRenderer implements BlockEntityRenderer<GramophoneBlockEntity> {
    private static final ResourceLocation TEXTURE = new FurnitureIdentifier("textures/entity/gramophone.png");
    private final GramophoneModel<?> model;

    public GramophoneRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new GramophoneModel<>(context.bakeLayer(GramophoneModel.LAYER_LOCATION));
    }

    @Override
    public void render(GramophoneBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        if (blockState.getValue(GramophoneBlock.HALF) == DoubleBlockHalf.UPPER) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rotation = switch (direction) {
            case NORTH -> 0;
            case SOUTH -> 180;
            default -> direction.toYRot();
        };
        poseStack.mulPose(new Quaternionf().rotateYXZ((float) Math.toRadians(rotation), 0.0F, 0.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        ModelPart base = model.gramophone.getChild("base");
        model.disc.visible = false;
        base.render(poseStack, bufferSource.getBuffer(model.renderType(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        model.disc.visible = true;
        model.gramophone.getChild("horn").render(poseStack, bufferSource.getBuffer(model.renderType(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        if (blockState.getValue(GramophoneBlock.HAS_RECORD)) {
            float gameTime = Objects.requireNonNull(blockEntity.getLevel()).getGameTime() + partialTicks;
            float discRotation = gameTime * 5 % 360;
            float verticalMovement = (float) Math.sin(gameTime * 0.1) * 0.002f;

            poseStack.pushPose();
            poseStack.translate(0.5, 0 + verticalMovement, -0.5);
            poseStack.translate(model.disc.x / 16.0F, 0.0, model.disc.z / 16.0F);
            poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(discRotation)));
            poseStack.translate(-model.disc.x / 16.0F, 0.0, -model.disc.z / 16.0F);
            model.disc.render(poseStack, bufferSource.getBuffer(model.renderType(TEXTURE)), combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }

        poseStack.popPose();
    }
}
