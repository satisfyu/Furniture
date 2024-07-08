package com.berksire.furniture.client.render;

import com.berksire.furniture.block.FacingBlock;
import com.berksire.furniture.block.entity.DeskChairBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class DeskChairRenderer implements BlockEntityRenderer<DeskChairBlockEntity> {

    public DeskChairRenderer() {
    }

    @Override
    public void render(DeskChairBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        ItemStack itemStack = blockEntity.getDisplayedBlanket();
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();

            Direction facing = blockEntity.getBlockState().getValue(FacingBlock.FACING);
            switch (facing) {
                case NORTH -> poseStack.mulPose(new Quaternionf().rotateY(0));
                case SOUTH -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
                case WEST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));
                case EAST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-90)));
            }

            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.scale(1.0f, 1.0f, 1.0f);

            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
