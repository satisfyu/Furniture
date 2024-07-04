package com.berksire.furniture.client.model;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.client.entity.FishTankEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class FishTankModel extends HierarchicalModel<FishTankEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Furniture.MODID, "fish_tank"), "main");
    public final ModelPart fish_tank;
    public final ModelPart cod;
    public final ModelPart cod_1;
    public final ModelPart cod_2;
    public final ModelPart cod_3;
    public final ModelPart cod_4;
    public final ModelPart salmon;
    public final ModelPart salmon_1;
    public final ModelPart salmon_2;
    public final ModelPart pufferfish;
    public final ModelPart pufferfish_1;
    public final ModelPart pufferfish_2;
    public final ModelPart tank;
    public final ModelPart decoration;

    public FishTankModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.fish_tank = root.getChild("fish_tank");
        this.cod = fish_tank.getChild("cod");
        this.cod_1 = cod.getChild("cod_1");
        this.cod_2 = cod.getChild("cod_2");
        this.cod_3 = cod.getChild("cod_3");
        this.cod_4 = cod.getChild("cod_4");
        this.salmon = fish_tank.getChild("salmon");
        this.salmon_1 = salmon.getChild("salmon_1");
        this.salmon_2 = salmon.getChild("salmon_2");
        this.pufferfish = fish_tank.getChild("pufferfish");
        this.pufferfish_1 = pufferfish.getChild("pufferfish_1");
        this.pufferfish_2 = pufferfish.getChild("pufferfish_2");
        this.tank = fish_tank.getChild("tank");
        this.decoration = fish_tank.getChild("decoration");
    }

    @SuppressWarnings("unused")
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition fish_tank = partdefinition.addOrReplaceChild("fish_tank", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition salmon = fish_tank.addOrReplaceChild("salmon", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition salmon_1 = salmon.addOrReplaceChild("salmon_1", CubeListBuilder.create().texOffs(17, 89).mirror().addBox(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.0F, -9.0F, 3.0F));

        PartDefinition salmon_2 = salmon.addOrReplaceChild("salmon_2", CubeListBuilder.create().texOffs(17, 89).addBox(-2.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, -12.0F, -1.0F));

        PartDefinition pufferfish = fish_tank.addOrReplaceChild("pufferfish", CubeListBuilder.create(), PartPose.offset(12.0F, -12.0F, 2.0F));

        PartDefinition pufferfish_2 = pufferfish.addOrReplaceChild("pufferfish_2", CubeListBuilder.create().texOffs(42, 89).addBox(-1.5F, -1.0F, -0.8333F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 89).addBox(0.5F, -1.0F, -0.8333F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(33, 88).addBox(-1.5F, 0.0F, -0.8333F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, 1.0F, -5.1667F));

        PartDefinition pufferfish_1 = pufferfish.addOrReplaceChild("pufferfish_1", CubeListBuilder.create().texOffs(42, 89).addBox(-1.5F, -1.0F, -0.8333F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(42, 89).addBox(0.5F, -1.0F, -0.8333F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(33, 88).addBox(-1.5F, 0.0F, -0.8333F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, -2.0F, -1.1667F));

        PartDefinition tank = fish_tank.addOrReplaceChild("tank", CubeListBuilder.create().texOffs(0, 26).addBox(-1.0F, 12.0F, -3.0F, 32.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(0.0F, 0.0F, -2.0F, 30.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-1.0F, -2.0F, -3.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-1.0F, -2.0F, 11.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(28, 52).addBox(29.0F, -2.0F, -1.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -14.0F, -5.0F));

        PartDefinition decoration = fish_tank.addOrReplaceChild("decoration", CubeListBuilder.create().texOffs(20, 66).addBox(6.0F, -8.0F, 0.0F, 5.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(40, 66).addBox(6.0F, -9.0F, 0.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(31, 73).addBox(1.0F, -3.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 76).addBox(-5.0F, -4.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 76).addBox(-4.0F, -5.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 76).addBox(20.0F, -4.0F, 3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 72).addBox(13.0F, -4.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 73).addBox(12.0F, -3.0F, -2.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 80).addBox(-1.0F, -8.0F, 4.0F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(10, 80).addBox(4.0F, -8.0F, 1.0F, 2.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition seagrass_r1 = decoration.addOrReplaceChild("seagrass_r1", CubeListBuilder.create().texOffs(0, 80).addBox(-1.6F, -6.0F, -0.8F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, -2.0F, 4.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition seagrass_r2 = decoration.addOrReplaceChild("seagrass_r2", CubeListBuilder.create().texOffs(0, 80).addBox(-2.0F, -6.0F, -1.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.0F, -2.0F, 4.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition red_coral_r1 = decoration.addOrReplaceChild("red_coral_r1", CubeListBuilder.create().texOffs(0, 66).addBox(-3.6F, -6.0F, -0.8F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -2.0F, 3.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition red_coral_r2 = decoration.addOrReplaceChild("red_coral_r2", CubeListBuilder.create().texOffs(0, 66).addBox(-4.0F, -6.0F, -1.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -2.0F, 5.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition yellow_coral_r1 = decoration.addOrReplaceChild("yellow_coral_r1", CubeListBuilder.create().texOffs(10, 66).addBox(-4.0F, -6.0F, -1.0F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.0F, -2.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition yellow_coral_r2 = decoration.addOrReplaceChild("yellow_coral_r2", CubeListBuilder.create().texOffs(10, 66).addBox(-3.6F, -6.0F, -0.8F, 5.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(20.0F, -2.0F, -3.0F, 0.0F, 0.7854F, 0.0F));

        PartDefinition cod = fish_tank.addOrReplaceChild("cod", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cod_1 = cod.addOrReplaceChild("cod_1", CubeListBuilder.create().texOffs(2, 90).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -8.0F, -0.5F));

        PartDefinition cod_2 = cod.addOrReplaceChild("cod_2", CubeListBuilder.create().texOffs(2, 90).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -6.0F, -2.5F));

        PartDefinition cod_3 = cod.addOrReplaceChild("cod_3", CubeListBuilder.create().texOffs(2, 90).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(14.0F, -8.0F, 3.5F));

        PartDefinition cod_4 = cod.addOrReplaceChild("cod_4", CubeListBuilder.create().texOffs(2, 90).addBox(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, -10.0F, -2.5F));

        PartDefinition box = partdefinition.addOrReplaceChild("box", CubeListBuilder.create().texOffs(48, 88).addBox(-28.0F, -12.0F, 0.0F, 28.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(22.0F, 22.0F, -6.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        fish_tank.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }



    public static final AnimationDefinition FISHY = AnimationDefinition.Builder.withLength(6.1333F).looping()
            .addAnimation("salmon_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8F, KeyframeAnimations.degreeVec(0.0F, 0.65F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.5667F, KeyframeAnimations.degreeVec(0.0F, 73.57F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.1333F, KeyframeAnimations.degreeVec(0.0F, 126.36F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.5667F, KeyframeAnimations.degreeVec(0.0F, 177.39F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.1667F, KeyframeAnimations.degreeVec(0.0F, 178.13F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(0.0F, 350.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("salmon_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2667F, KeyframeAnimations.posVec(18.0F, 0.0F, 1.15F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2333F, KeyframeAnimations.posVec(17.35F, 0.0F, -7.77F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.3F, KeyframeAnimations.posVec(-2.0F, 0.0F, -8.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(-3.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(0.0F, 38.86F, -27.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0333F, KeyframeAnimations.degreeVec(0.0F, 122.92F, -18.32F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4333F, KeyframeAnimations.degreeVec(0.0F, 181.0F, -2.64F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5333F, KeyframeAnimations.degreeVec(0.0F, 177.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6333F, KeyframeAnimations.degreeVec(0.0F, 181.0F, -2.64F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7333F, KeyframeAnimations.degreeVec(0.0F, 177.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8333F, KeyframeAnimations.degreeVec(0.0F, 181.0F, -2.64F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9333F, KeyframeAnimations.degreeVec(0.0F, 177.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.0333F, KeyframeAnimations.degreeVec(0.0F, 181.0F, -2.64F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.1333F, KeyframeAnimations.degreeVec(0.0F, 177.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.degreeVec(0.0F, 230.39F, 0.12F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.9F, KeyframeAnimations.degreeVec(0.0F, 318.74F, 0.1F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.3333F, KeyframeAnimations.degreeVec(0.0F, 365.61F, 0.07F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.4667F, KeyframeAnimations.degreeVec(0.0F, 346.02F, 0.06F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.6F, KeyframeAnimations.degreeVec(0.0F, 365.61F, 0.07F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.7333F, KeyframeAnimations.degreeVec(0.0F, 346.02F, 0.06F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.8667F, KeyframeAnimations.degreeVec(0.0F, 365.61F, 0.07F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.0F, KeyframeAnimations.degreeVec(0.0F, 346.02F, 0.06F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.1333F, KeyframeAnimations.degreeVec(0.0F, 365.61F, 0.07F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.2667F, KeyframeAnimations.degreeVec(0.0F, 346.02F, 0.06F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.4F, KeyframeAnimations.degreeVec(0.0F, 365.61F, 0.07F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5667F, KeyframeAnimations.degreeVec(0.0F, 365.61F, 0.07F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.7F, KeyframeAnimations.degreeVec(0.0F, 346.02F, 0.06F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.8333F, KeyframeAnimations.degreeVec(0.0F, 365.61F, 0.07F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.2333F, KeyframeAnimations.degreeVec(0.0F, 376.08F, 28.45F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.4F, KeyframeAnimations.degreeVec(0.0F, 351.3F, 27.23F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.5333F, KeyframeAnimations.degreeVec(0.0F, 376.92F, 23.15F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.6667F, KeyframeAnimations.degreeVec(0.0F, 363.39F, 18.37F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.8333F, KeyframeAnimations.degreeVec(0.0F, 385.46F, 11.56F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 350.77F, 4.12F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.posVec(-4.0F, -3.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1F, KeyframeAnimations.posVec(-3.0F, -5.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4F, KeyframeAnimations.posVec(15.0F, -3.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.1333F, KeyframeAnimations.posVec(15.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.8667F, KeyframeAnimations.posVec(7.0F, -4.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("pufferfish_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-720.0F, -90.0F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(-720.0F, -92.72F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(-720.0F, -68.49F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1667F, KeyframeAnimations.degreeVec(-720.0F, -90.17F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.1667F, KeyframeAnimations.degreeVec(-720.0F, 4.11F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7F, KeyframeAnimations.degreeVec(-720.0F, 85.89F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0667F, KeyframeAnimations.degreeVec(-720.0F, 58.79F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.4333F, KeyframeAnimations.degreeVec(-720.0F, 106.4F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.7F, KeyframeAnimations.degreeVec(-720.0F, 50.05F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.3667F, KeyframeAnimations.degreeVec(-720.0F, 264.87F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.7F, KeyframeAnimations.degreeVec(-720.0F, 232.63F, -720.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(-720.0F, 267.5F, -720.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("pufferfish_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-3.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7667F, KeyframeAnimations.posVec(14.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.4333F, KeyframeAnimations.posVec(-8.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(-3.0F, 0.0F, 4.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("pufferfish_1", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 145.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.3667F, KeyframeAnimations.degreeVec(35.0F, 92.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6667F, KeyframeAnimations.degreeVec(35.0F, 77.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9F, KeyframeAnimations.degreeVec(35.0F, 115.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1F, KeyframeAnimations.degreeVec(35.0F, 87.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3F, KeyframeAnimations.degreeVec(35.0F, 115.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.degreeVec(35.0F, 87.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7F, KeyframeAnimations.degreeVec(35.0F, 115.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9F, KeyframeAnimations.degreeVec(35.0F, 87.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.1F, KeyframeAnimations.degreeVec(35.0F, 115.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4667F, KeyframeAnimations.degreeVec(35.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7667F, KeyframeAnimations.degreeVec(15.31F, -25.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0F, KeyframeAnimations.degreeVec(0.0F, -7.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.3F, KeyframeAnimations.degreeVec(0.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5F, KeyframeAnimations.degreeVec(-20.0F, -120.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.8333F, KeyframeAnimations.degreeVec(-20.0F, -107.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.1F, KeyframeAnimations.degreeVec(-20.0F, -132.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.4F, KeyframeAnimations.degreeVec(-20.0F, -115.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.7F, KeyframeAnimations.degreeVec(-20.0F, -130.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(0.0F, -215.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("pufferfish_1", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2F, KeyframeAnimations.posVec(-12.0F, -6.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.5333F, KeyframeAnimations.posVec(-8.0F, -6.0F, -4.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("salmon_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.8F, KeyframeAnimations.degreeVec(0.0F, 17.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9667F, KeyframeAnimations.degreeVec(0.0F, -7.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1333F, KeyframeAnimations.degreeVec(0.0F, 17.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3F, KeyframeAnimations.degreeVec(0.0F, -7.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4667F, KeyframeAnimations.degreeVec(0.0F, 17.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6333F, KeyframeAnimations.degreeVec(0.0F, -7.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.8F, KeyframeAnimations.degreeVec(0.0F, 17.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9667F, KeyframeAnimations.degreeVec(0.0F, -7.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.1333F, KeyframeAnimations.degreeVec(0.0F, 17.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.3F, KeyframeAnimations.degreeVec(0.0F, -7.5F, -17.5F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4667F, KeyframeAnimations.degreeVec(15.2727F, 8.6474F, 43.6616F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0333F, KeyframeAnimations.degreeVec(165.441F, -4.1624F, 201.7046F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.4667F, KeyframeAnimations.degreeVec(173.1163F, 8.106F, 194.5732F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.6333F, KeyframeAnimations.degreeVec(177.1441F, -14.3549F, 196.3165F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.8F, KeyframeAnimations.degreeVec(173.1163F, 8.106F, 194.5732F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.9667F, KeyframeAnimations.degreeVec(177.1441F, -14.3549F, 196.3165F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.1333F, KeyframeAnimations.degreeVec(173.1163F, 8.106F, 194.5732F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.3F, KeyframeAnimations.degreeVec(177.1441F, -14.3549F, 196.3165F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.4667F, KeyframeAnimations.degreeVec(173.1163F, 8.106F, 194.5732F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.6333F, KeyframeAnimations.degreeVec(177.1441F, -14.3549F, 196.3165F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.8F, KeyframeAnimations.degreeVec(173.1163F, 8.106F, 194.5732F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.9667F, KeyframeAnimations.degreeVec(177.1441F, -14.3549F, 196.3165F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.1333F, KeyframeAnimations.degreeVec(173.1163F, 8.106F, 194.5732F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.3F, KeyframeAnimations.degreeVec(177.1441F, -14.3549F, 196.3165F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.4667F, KeyframeAnimations.degreeVec(173.1163F, 8.106F, 194.5732F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("salmon_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.3333F, KeyframeAnimations.posVec(-14.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.0333F, KeyframeAnimations.posVec(-14.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.4667F, KeyframeAnimations.posVec(0.0F, -4.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_2", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.7667F, KeyframeAnimations.degreeVec(0.0F, -158.03F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.9667F, KeyframeAnimations.degreeVec(0.0F, -189.54F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.1333F, KeyframeAnimations.degreeVec(0.0F, -171.85F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.3F, KeyframeAnimations.degreeVec(0.0F, -189.54F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.4667F, KeyframeAnimations.degreeVec(0.0F, -171.85F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.6333F, KeyframeAnimations.degreeVec(0.0F, -189.54F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.8F, KeyframeAnimations.degreeVec(0.0F, -171.85F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.9667F, KeyframeAnimations.degreeVec(0.0F, -189.54F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.1333F, KeyframeAnimations.degreeVec(0.0F, -171.85F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.3F, KeyframeAnimations.degreeVec(0.0F, -189.54F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(0.0F, -360.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_2", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2333F, KeyframeAnimations.posVec(-8.0F, -3.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.8333F, KeyframeAnimations.posVec(16.0F, -3.0F, -2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_3", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -187.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7F, KeyframeAnimations.degreeVec(0.0F, -186.41F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.4667F, KeyframeAnimations.degreeVec(0.0F, 83.46F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(0.0F, 172.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_3", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(-1.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.3333F, KeyframeAnimations.posVec(6.0F, -3.0F, 2.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.8F, KeyframeAnimations.posVec(7.0F, -3.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.7F, KeyframeAnimations.posVec(0.0F, -3.0F, -7.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(-1.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_4", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -30.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.2667F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.4333F, KeyframeAnimations.degreeVec(0.0F, -9.27F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.6F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.7667F, KeyframeAnimations.degreeVec(0.0F, -9.27F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.9333F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.1F, KeyframeAnimations.degreeVec(0.0F, -9.27F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.2667F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.4333F, KeyframeAnimations.degreeVec(0.0F, -9.27F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.6F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.7667F, KeyframeAnimations.degreeVec(0.0F, -9.27F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.9333F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.1F, KeyframeAnimations.degreeVec(0.0F, -9.27F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.2667F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.4333F, KeyframeAnimations.degreeVec(0.0F, -9.27F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.6F, KeyframeAnimations.degreeVec(0.0F, 18.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.8F, KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.2F, KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.6667F, KeyframeAnimations.degreeVec(0.0F, 130.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.9667F, KeyframeAnimations.degreeVec(0.0F, 172.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.3F, KeyframeAnimations.degreeVec(0.0F, 189.76F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.5667F, KeyframeAnimations.degreeVec(0.0F, 171.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.7333F, KeyframeAnimations.degreeVec(0.0F, 189.76F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.9F, KeyframeAnimations.degreeVec(0.0F, 171.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.0667F, KeyframeAnimations.degreeVec(0.0F, 189.76F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.2333F, KeyframeAnimations.degreeVec(0.0F, 171.81F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.4F, KeyframeAnimations.degreeVec(0.0F, 189.76F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.6333F, KeyframeAnimations.degreeVec(0.0F, 202.42F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.degreeVec(0.0F, 330.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("cod_4", new AnimationChannel(AnimationChannel.Targets.POSITION,
                    new Keyframe(0.0F, KeyframeAnimations.posVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.5F, KeyframeAnimations.posVec(-7.22F, -2.0F, -0.75F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(2.9333F, KeyframeAnimations.posVec(-17.0F, -3.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(3.7667F, KeyframeAnimations.posVec(-17.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(4.9F, KeyframeAnimations.posVec(-5.54F, -1.85F, 6.73F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(5.7F, KeyframeAnimations.posVec(2.0F, 0.0F, 6.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(6.1333F, KeyframeAnimations.posVec(3.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    @Override
    public void setupAnim(FishTankEntity entity, float f, float g, float h, float i, float j) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.idleAnimationState, FISHY, h, 1.0F); // state, anim, tick time, speed multiplier
    }

    @Override
    public ModelPart root() {
        return fish_tank;
    }
}


