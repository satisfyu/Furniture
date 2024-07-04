package com.berksire.furniture.client.render;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.client.entity.PellsEntity;
import com.berksire.furniture.client.model.PellsModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PellsRenderer extends MobRenderer<PellsEntity, PellsModel<PellsEntity>>{
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Furniture.MODID, "textures/entity/pells.png");
    private static final DecimalFormat FORMAT = new DecimalFormat("###.##", new DecimalFormatSymbols(Locale.GERMAN));

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
            Color color = getColorByDamage(lastDamage);
            renderText(entityIn, FORMAT.format(lastDamage), stack, bufferIn, packedLightIn, color);
        }
    }

    private Color getColorByDamage(float damage) {
        if (damage >= 0 && damage <= 2) {
            return Color.GREEN;
        } else if (damage >= 3 && damage <= 6) {
            return Color.WHITE;
        } else if (damage >= 7 && damage <= 12) {
            return Color.YELLOW;
        } else if (damage >= 13 && damage <= 19) {
            return Color.ORANGE;
        } else {
            return Color.RED;
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
