package com.berksire.furniture.client;

import com.berksire.furniture.client.model.ClockModel;
import com.berksire.furniture.client.model.FishTankModel;
import com.berksire.furniture.client.model.GramophoneModel;
import com.berksire.furniture.client.model.GrandfatherClockModel;
import com.berksire.furniture.client.render.*;
import com.berksire.furniture.registry.EntityTypeRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.stream.Stream;

import static com.berksire.furniture.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class FurnitureClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(), BIN.get(), GRAMOPHONE.get(), CASH_REGISTER.get());
        for (RegistrySupplier<Block> block : Stream.concat(LAMPS.values().stream(), BENCHES.values().stream()).toList()) {
            RenderTypeRegistry.register(RenderType.cutout(), block.get());
        }

        BlockEntityRendererRegistry.register(EntityTypeRegistry.GRANDFATHER_CLOCK_BLOCK_ENTITY.get(), GrandfatherClockRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.CLOCK_BLOCK_ENTITY.get(), ClockRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.GRAMOPHONE_BLOCK_ENTITY.get(), GramophoneRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.COFFER_BLOCK_ENTITY.get(), CofferRenderer::new);
    }

    public static void preInitClient() {
        registerEntityModelLayer();
        registerEntityRenderers();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.CHAIR, ChairRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.CANVAS, CanvasRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.PELLS, PellsRenderer::new);
        EntityRendererRegistry.register(EntityTypeRegistry.FAKE_FISH_TANK, FishTankRenderer::new);
    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(GrandfatherClockModel.LAYER_LOCATION, GrandfatherClockModel::getTexturedModelData);
        EntityModelLayerRegistry.register(ClockModel.LAYER_LOCATION, ClockModel::getTexturedModelData);
        EntityModelLayerRegistry.register(GramophoneModel.LAYER_LOCATION, GramophoneModel::getTexturedModelData);
        EntityModelLayerRegistry.register(FishTankModel.LAYER_LOCATION, FishTankModel::getTexturedModelData);
        EntityModelLayerRegistry.register(CofferRenderer.LAYER_LOCATION, CofferRenderer::getTexturedModelData);
    }
}
