package com.berksire.furniture.util;

import com.berksire.furniture.Furniture;
import net.minecraft.resources.ResourceLocation;

public class FurnitureIdentifier extends ResourceLocation {
    public FurnitureIdentifier(String path) {
        super(Furniture.MODID, path);
    }
}
