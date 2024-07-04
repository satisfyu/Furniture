package com.berksire.furniture.client.render;

import com.berksire.furniture.block.CofferBlock;
import com.berksire.furniture.block.entity.CofferBlockEntity;
import com.berksire.furniture.util.FurnitureIdentifier;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

@SuppressWarnings("unused")
public class CofferRenderer implements BlockEntityRenderer<CofferBlockEntity> {
    private static final ResourceLocation TEXTURE = new FurnitureIdentifier("textures/entity/coffer.png");
    private final ModelPart coffer;
    private final ModelPart top;

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new FurnitureIdentifier("coffer"), "main");

    public CofferRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(LAYER_LOCATION);
        this.coffer = modelPart.getChild("coffer");
        ModelPart bottom = coffer.getChild("bottom");
        this.top = coffer.getChild("top");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition coffer = partdefinition.addOrReplaceChild("coffer", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bottom = coffer.addOrReplaceChild("bottom", CubeListBuilder.create().texOffs(0, 0).addBox(-13.0F, -5.0F, -1.0F, 14.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.0F, -4.0F));

        PartDefinition top = coffer.addOrReplaceChild("top", CubeListBuilder.create().texOffs(0, 15).addBox(-13.0F, -3.0F, -1.0F, 14.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -1.0F, 9.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -5.0F, -4.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void render(CofferBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        float lidAngle = blockEntity.getOpenNess(partialTicks);
        lidAngle = 1.0F - lidAngle;
        lidAngle = 1.0F - (float) Math.pow(lidAngle, 1);

        float bounce = (float) Math.sin(lidAngle * Math.PI);
        this.top.xRot = lidAngle * ((float) Math.PI / 2F) + bounce;

        poseStack.pushPose();
        Direction facing = blockEntity.getBlockState().getValue(CofferBlock.FACING);
        switch (facing) {
            case NORTH:
                poseStack.translate(1D, 1D, 0D);
                break;
            case SOUTH:
                poseStack.translate(0D, 1D, 1D);
                break;
            case EAST:
                poseStack.translate(1D, 1D, 1D);
                break;
            case WEST:
                poseStack.translate(0D, 1D, 0D);
                break;
        }
        poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-facing.toYRot() + 180)));
        poseStack.mulPose(new Quaternionf().rotateX((float) Math.toRadians(180)));
        poseStack.translate(-0.5D, -0.5D, -0.5D);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        this.coffer.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }


}