package com.berksire.furniture;

import com.berksire.furniture.registry.*;
import com.google.common.reflect.Reflection;
import dev.architectury.hooks.item.tool.AxeItemHooks;
import dev.architectury.hooks.item.tool.ShovelItemHooks;
import dev.architectury.registry.fuel.FuelRegistry;
import net.minecraft.world.level.block.Blocks;

public class Furniture {
    public static final String MODID = "furniture";

    public static void init() {
        Reflection.initialize(
                ObjectRegistry.class,
                EntityTypeRegistry.class,
                CanvasRegistry.class,
                TabRegistry.class,
                SoundRegistry.class
        );
    }

    public static void commonSetup() {
        Reflection.initialize(
                FlammableBlockRegistry.class
        );
    }
}
