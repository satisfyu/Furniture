package com.berksire.furniture;

import com.berksire.furniture.registry.EntityTypeRegistry;
import com.berksire.furniture.registry.ObjectRegistry;
import com.berksire.furniture.registry.CanvasRegistry;
import com.berksire.furniture.registry.TabRegistry;
import com.berksire.furniture.registry.SoundRegistry;
import com.berksire.furniture.registry.FlammableBlockRegistry;
import com.google.common.reflect.Reflection;

public class Furniture {
    public static final String MODID = "furniture";

    public static void init() {
        Reflection.initialize(
                ObjectRegistry.class,
                EntityTypeRegistry.class,
                CanvasRegistry.class,
                TabRegistry.class,
                SoundRegistry.class,
                FlammableBlockRegistry.class
        );
    }
}
