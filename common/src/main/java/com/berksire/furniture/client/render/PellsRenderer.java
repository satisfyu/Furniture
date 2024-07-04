package com.berksire.furniture.client.render;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.client.entity.PellsEntity;
import com.berksire.furniture.client.model.PellsModel;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.NotNull;
import org.joml.*;

import java.awt.*;
import java.lang.Math;
import java.text.*;
import java.util.*;

public class PellsRenderer extends MobRenderer<PellsEntity, PellsModel<PellsEntity>>{

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Furniture.MODID, "textures/entity/pells.png");
    private static final DecimalFormat FORMAT = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.ENGLISH));

    public PellsRenderer(EntityRendererProvider.Context context){
        super(context, new PellsModel<>(PellsModel.createBodyLayer().bakeRoot()), 0.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(PellsEntity entity){
        return TEXTURE;
    }

    @Override
    public void render(PellsEntity entityIn, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn){
        super.render(entityIn, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        float lastDamage = entityIn.getLastDamage();
        if(lastDamage > 0f){
            Color color = getColorByDamage(entityIn, lastDamage);
            renderText(entityIn, FORMAT.format(lastDamage), stack, bufferIn, packedLightIn, color);
        }
    }

    private Color getColorByDamage(PellsEntity entityIn, float damage) {
        boolean isCrit = entityIn.isCrit();
        if (isCrit) {
            return Color.ORANGE;
        } else {
            int red = Math.min(255, (int)(damage * 255 / 100));
            return new Color(red, 255 - red, 255 - red);
        }
    }


    protected void renderText(PellsEntity entityIn, String text, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Color textColor){
        if(entityIn.hurtTime > 0){
            float partialTicks = Minecraft.getInstance().getFrameTime();
            Component component = Component.literal(text);
            entityIn.lastDamageOffset = Mth.lerp(partialTicks, entityIn.lastDamageOffsetPrev, (float)Math.abs(Math.sin(((float)entityIn.hurtTime) / 4f)));
            entityIn.lastDamageOffsetPrev = entityIn.lastDamageOffset;
            float alpha = entityIn.lastDamageOffset;

            matrixStackIn.pushPose();
            matrixStackIn.translate(0, entityIn.getBbHeight() + entityIn.lastDamageOffset, 0.0D);
            matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
            matrixStackIn.scale(-entityIn.lastDamageOffset / 20f, -entityIn.lastDamageOffset / 20f, entityIn.lastDamageOffset / 20f);
            Matrix4f matrix4f = matrixStackIn.last().pose();

            Font font = this.getFont();
            Color color = new Color(textColor.getRed() / 255f, textColor.getGreen() / 255f, textColor.getBlue() / 255f, alpha);
            font.drawInBatch(component, (float)(-font.width(component) / 2) * entityIn.lastDamageOffset, entityIn.lastDamageOffset, color.getRGB(), false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, 0, packedLightIn);
            matrixStackIn.popPose();
        }
    }
}