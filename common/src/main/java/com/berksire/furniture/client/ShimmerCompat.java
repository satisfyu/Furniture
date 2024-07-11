package com.berksire.furniture.client;

import com.berksire.furniture.block.StreetLanternBlock;
import com.berksire.furniture.registry.ObjectRegistry;
import com.berksire.furniture.util.FurnitureUtil;
import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.client.light.LightManager;
import net.minecraft.world.level.block.Block;

import java.awt.*;

public interface ShimmerCompat {
    // these are here so IDEA shows you what color the numbers are
    Color STRT_LANT_COL = new Color(0xB3FF9100, true);
    Color PLATED_STRT_LANT_COL = new Color(0xB3BE58E4, true);
    Color VERDANT_STRT_LANT_COL = new Color(0xB358E484, true);

    static void registerLights(LightManager manager) {
        streetLantern(manager, ObjectRegistry.PLATED_STREET_LANTERN.get(), 0xB3BE58E4);
        streetLantern(manager, ObjectRegistry.STREET_LANTERN.get(), 0xB3FF9100);
    }

    static void streetLantern(LightManager manager, Block block, int color) {
        manager.registerBlockLight(block, (state, pos) -> {
            if (!StreetLanternBlock.canProvideLight(state)) return null;
            return new ColorPointLight.Template(11, color);
        });
    }
}
