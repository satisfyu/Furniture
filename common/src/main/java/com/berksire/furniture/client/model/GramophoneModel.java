package com.berksire.furniture.client.model;

import com.berksire.furniture.Furniture;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class GramophoneModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Furniture.MODID, "gramophone"), "main");
    public final ModelPart gramophone;
    public final ModelPart disc;

    public GramophoneModel(ModelPart root) {
        this.gramophone = root.getChild("gramophone");
        ModelPart base = gramophone.getChild("base");
        this.disc = base.getChild("disc");
        ModelPart horn = gramophone.getChild("horn");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition gramophone = partdefinition.addOrReplaceChild("gramophone", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition base = gramophone.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -8.0F, 1.0F, 14.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(16, 35).addBox(-9.0F, -16.0F, 13.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        PartDefinition disc = base.addOrReplaceChild("disc", CubeListBuilder.create().texOffs(15, 35).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -8.2F, 8.0F));


        PartDefinition horn = gramophone.addOrReplaceChild("horn", CubeListBuilder.create().texOffs(26, 22).addBox(-14.0F, -24.0F, 5.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-12.0F, -22.0F, 6.0F, 8.0F, 8.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(-10.0F, -20.0F, 11.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, -8.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        gramophone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}