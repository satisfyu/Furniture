package com.berksire.furniture.fabric;

import com.berksire.furniture.Furniture;
import net.fabricmc.api.ModInitializer;

public class FurnitureFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Furniture.init();
    }
}
