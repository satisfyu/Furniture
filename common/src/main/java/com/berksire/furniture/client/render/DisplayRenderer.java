package com.berksire.furniture.client.render;

import com.berksire.furniture.block.FacingBlock;
import com.berksire.furniture.block.entity.DisplayBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.Objects;

public class DisplayRenderer implements BlockEntityRenderer<DisplayBlockEntity> {

    public DisplayRenderer() {
    }

    @Override
    public void render(DisplayBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        ItemStack itemStack = blockEntity.getDisplayedItem();
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();

            double offset = Math.sin((Objects.requireNonNull(blockEntity.getLevel()).getGameTime() + partialTick) / 8.0) * 0.1;
            poseStack.translate(0.5, 0.6 + offset * 0.05, 0.5);

            Direction facing = blockEntity.getBlockState().getValue(FacingBlock.FACING);
            switch (facing) {
                case NORTH -> poseStack.mulPose(new Quaternionf().rotateY(0));
                case SOUTH -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(180)));
                case WEST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(90)));
                case EAST -> poseStack.mulPose(new Quaternionf().rotateY((float) Math.toRadians(-90)));
            }
            poseStack.scale(0.75f, 0.75f, 0.75f);
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.GROUND, combinedLight, combinedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }
    }
}
