package com.berksire.furniture.client.render;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.block.ClockBlock;
import com.berksire.furniture.block.entity.ClockBlockEntity;
import com.berksire.furniture.client.model.ClockModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ClockRenderer implements BlockEntityRenderer<ClockBlockEntity> {
    private static final Map<ClockBlock.WoodType, ResourceLocation> TEXTURES = new EnumMap<>(ClockBlock.WoodType.class);
    static {
        TEXTURES.put(ClockBlock.WoodType.OAK, new ResourceLocation(Furniture.MODID, "textures/entity/oak_clock.png"));
        TEXTURES.put(ClockBlock.WoodType.SPRUCE, new ResourceLocation(Furniture.MODID, "textures/entity/spruce_clock.png"));
        TEXTURES.put(ClockBlock.WoodType.BIRCH, new ResourceLocation(Furniture.MODID, "textures/entity/birch_clock.png"));
        TEXTURES.put(ClockBlock.WoodType.JUNGLE, new ResourceLocation(Furniture.MODID, "textures/entity/jungle_clock.png"));
        TEXTURES.put(ClockBlock.WoodType.ACACIA, new ResourceLocation(Furniture.MODID, "textures/entity/acacia_clock.png"));
        TEXTURES.put(ClockBlock.WoodType.DARK_OAK, new ResourceLocation(Furniture.MODID, "textures/entity/dark_oak_clock.png"));
        TEXTURES.put(ClockBlock.WoodType.MANGROVE, new ResourceLocation(Furniture.MODID, "textures/entity/mangrove_clock.png"));
        TEXTURES.put(ClockBlock.WoodType.CHERRY, new ResourceLocation(Furniture.MODID, "textures/entity/cherry_clock.png"));
    }

    private final ClockModel<Entity> model;

    public ClockRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart root = context.bakeLayer(ClockModel.LAYER_LOCATION);
        this.model = new ClockModel<>(root);
    }

    @Override
    public void render(ClockBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        long gameTime = Objects.requireNonNull(blockEntity.getLevel()).getDayTime() % 24000;
        int hours = (int) ((gameTime / 1000 + 6) % 24);
        int minutes = (int) ((gameTime % 1000) * 60 / 1000);

        this.model.minutes.zRot = (float) (minutes * Math.PI / 30);
        this.model.hours.zRot = (float) (hours * Math.PI / 6);

        BlockState blockstate = blockEntity.getBlockState();
        poseStack.pushPose();
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(180.0F)));

        Direction direction = blockstate.getValue(BlockStateProperties.HORIZONTAL_FACING);
        float rotation = direction.getOpposite().toYRot();
        poseStack.mulPose(new Quaternionf().rotateYXZ((float) Math.toRadians(rotation), 0.0F, 0.0F));


        ClockBlock block = (ClockBlock) blockEntity.getBlockState().getBlock();
        ClockBlock.WoodType woodType = block.getWoodType();
        ResourceLocation texture = TEXTURES.get(woodType);

        renderModel(poseStack, bufferSource.getBuffer(RenderType.entityCutout(texture)), combinedLight, combinedOverlay);
        poseStack.popPose();
    }

    private void renderModel(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay) {
        this.model.renderToBuffer(poseStack, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}