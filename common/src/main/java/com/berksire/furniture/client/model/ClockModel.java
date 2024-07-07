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

public class ClockModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Furniture.MODID, "clock"), "main");
    private final ModelPart wallclock;
    public final ModelPart hours;
    public final ModelPart minutes;

    public ClockModel(ModelPart root) {
        this.wallclock = root.getChild("wallclock");
        ModelPart clockwork = wallclock.getChild("clockwork");
        this.hours = clockwork.getChild("hours");
        this.minutes = clockwork.getChild("minutes");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition wallclock = partdefinition.addOrReplaceChild("wallclock", CubeListBuilder.create().texOffs(0, 13).addBox(-4.5F, -5.5F, 0.25F, 9.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 17.0F, 5.75F));

        PartDefinition clockwork = wallclock.addOrReplaceChild("clockwork", CubeListBuilder.create().texOffs(2, 10).addBox(-0.5F, -1.5F, -0.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hours = clockwork.addOrReplaceChild("hours", CubeListBuilder.create().texOffs(3, 9).addBox(-0.5F, -3.5F, 0.0F, 1.0F, 3.5F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition minutes = clockwork.addOrReplaceChild("minutes", CubeListBuilder.create().texOffs(3, 9).addBox(-0.5F, -3.5F, 0.0F, 1.0F, 3.5F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 24, 24);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        wallclock.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}