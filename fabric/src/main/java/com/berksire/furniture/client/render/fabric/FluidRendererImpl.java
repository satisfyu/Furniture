package com.berksire.furniture.client.render.fabric;

import com.berksire.furniture.client.render.FluidRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.fabric.FluidStackHooksFabric;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class FluidRendererImpl {
    public static void renderFluidBox(FluidStack fluidStack, float xMin, float yMin, float zMin, float xMax,
                                      float yMax, float zMax, VertexConsumer builder, PoseStack ms, int light, boolean renderBottom) {
            FluidVariant fluidVariant = FluidStackHooksFabric.toFabric(fluidStack);
            TextureAtlasSprite[] sprites = FluidVariantRendering.getSprites(fluidVariant);
            TextureAtlasSprite fluidTexture = sprites != null ? sprites[0] : null;
            if (fluidTexture == null)
                return;

            int color = FluidVariantRendering.getColor(fluidVariant);
            int blockLightIn = (light >> 4) & 0xF;
            int luminosity = Math.max(blockLightIn, FluidVariantAttributes.getLuminance(fluidVariant));
            light = (light & 0xF00000) | luminosity << 4;

            Vec3 center = new Vec3(xMin + (xMax - xMin) / 2, yMin + (yMax - yMin) / 2, zMin + (zMax - zMin) / 2);
            ms.pushPose();

            for (Direction side : Direction.values()) {
                if (side == Direction.DOWN && !renderBottom)
                    continue;

                boolean positive = side.getAxisDirection() == Direction.AxisDirection.POSITIVE;
                if (side.getAxis()
                        .isHorizontal()) {
                    if (side.getAxis() == Direction.Axis.X) {
                        FluidRenderer.renderStillTiledFace(side, zMin, yMin, zMax, yMax, positive ? xMax : xMin, builder, ms, light,
                                color, fluidTexture);
                    } else {
                        FluidRenderer.renderStillTiledFace(side, xMin, yMin, xMax, yMax, positive ? zMax : zMin, builder, ms, light,
                                color, fluidTexture);
                    }
                } else {
                    FluidRenderer.renderStillTiledFace(side, xMin, zMin, xMax, zMax, positive ? yMax : yMin, builder, ms, light, color,
                            fluidTexture);
                }
            }

            ms.popPose();
    }
}
