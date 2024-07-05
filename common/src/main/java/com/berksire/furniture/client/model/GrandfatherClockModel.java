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

public class GrandfatherClockModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Furniture.MODID, "grandfatherclock"), "main");
    private final ModelPart grandfatherclock;
    public final ModelPart pendulum;
    public final ModelPart minutes;
    public final ModelPart hours;

    public GrandfatherClockModel(ModelPart root) {
        this.grandfatherclock = root.getChild("grandfatherclock");
        this.pendulum = grandfatherclock.getChild("pendulum");
        ModelPart clock = grandfatherclock.getChild("clock");
        this.minutes = clock.getChild("minutes");
        this.hours = clock.getChild("hours");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition grandfatherclock = partdefinition.addOrReplaceChild("grandfatherclock", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(2.0F, 10.5F, -3.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-5.0F, 10.5F, -3.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(2.0F, 10.5F, 2.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 0).mirror().addBox(-5.0F, 10.5F, 2.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 49).addBox(-5.0F, 3.5F, -3.5F, 9.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(3.0F, -8.5F, -3.5F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).mirror().addBox(-5.0F, -8.5F, -3.5F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(17, 21).addBox(-4.0F, -8.5F, 3.5F, 7.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(33, 21).addBox(-4.0F, -8.5F, -3.5F, 7.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-6.0F, -9.5F, -4.5F, 11.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 34).addBox(-5.0F, -16.5F, -3.5F, 9.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(3, 4).addBox(-1.0F, -14.5F, -4.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 11).addBox(-5.0F, -19.5F, -3.5F, 9.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(4, 6).addBox(4.0F, -19.5F, -3.5F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(4, 6).addBox(-5.0F, -19.5F, -3.5F, 0.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 11.5F, -0.5F));

        PartDefinition pendulum = grandfatherclock.addOrReplaceChild("pendulum", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, -0.25F, -0.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.25F, 0.0F));

        PartDefinition pendulum_r1 = pendulum.addOrReplaceChild("pendulum_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.25F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition clock = grandfatherclock.addOrReplaceChild("clock", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition minutes = clock.addOrReplaceChild("minutes", CubeListBuilder.create().texOffs(4, 4).addBox(-0.5F, -2.5F, 0.0F, 1.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -14.0F, -3.75F));

        PartDefinition hours = clock.addOrReplaceChild("hours", CubeListBuilder.create().texOffs(3, 5).addBox(0.0F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -14.0F, -3.75F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        grandfatherclock.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}