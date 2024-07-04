package com.berksire.furniture.client.model;

import com.berksire.furniture.client.entity.PellsEntity;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;

public class PellsModel<T extends Entity> extends EntityModel<T>{
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart Stand;
    private final ModelPart Baseplate;

    public PellsModel(ModelPart root){
        this.Head = root.getChild("Head");
        this.Body = root.getChild("Body");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.Stand = root.getChild("Stand");
        this.Baseplate = root.getChild("Baseplate");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer(){
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 13).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));
        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(27, 24).addBox(-4.0F, -8.0F, -2.5F, 8.0F, 16.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));
        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(32, 13).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 1.5F, 1.0F));
        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 13).mirror().addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-6.0F, 1.5F, 1.0F));
        PartDefinition Stand = partdefinition.addOrReplaceChild("Stand", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition Baseplate = partdefinition.addOrReplaceChild("Baseplate", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -0.5F, -6.0F, 12.0F, 1.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float HeadPitch){
        if(entityIn instanceof PellsEntity entity){
            float factor = (float)Math.sin(ageInTicks + entity.getLastDamage() * 0.5);
            float speed = 0.1f;
            this.Body.xRot = Mth.cos(limbSwing * 0.5662F * speed + (float)Math.PI) * 0.4F * factor * limbSwingAmount;
            this.Body.zRot = Mth.sin(limbSwing * 0.2262F * speed + (float)Math.PI) * 0.1F * factor * limbSwingAmount;
            this.Head.xRot = Mth.cos(limbSwing * 0.5662F * speed + (float)Math.PI) * 0.4F * factor * limbSwingAmount;
            this.Head.zRot = Mth.sin(limbSwing * 0.2262F * speed + (float)Math.PI) * 0.1F * factor * limbSwingAmount;
            this.LeftArm.xRot = Mth.cos(limbSwing * 0.5662F * speed + (float)Math.PI) * 0.4F * factor * limbSwingAmount;
            this.LeftArm.zRot = Mth.sin(limbSwing * 0.2262F * speed + (float)Math.PI) * 0.1F * factor * limbSwingAmount;
            this.RightArm.xRot = Mth.cos(limbSwing * 0.5662F * speed + (float)Math.PI) * 0.4F * factor * limbSwingAmount;
            this.RightArm.zRot = Mth.sin(limbSwing * 0.2262F * speed + (float)Math.PI) * 0.1F * factor * limbSwingAmount;
         }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Stand.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Baseplate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}