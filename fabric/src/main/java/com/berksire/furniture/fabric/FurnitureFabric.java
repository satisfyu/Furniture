package com.berksire.furniture.fabric;

import net.fabricmc.api.ModInitializer;
import com.berksire.furniture.Furniture;

public class FurnitureFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Furniture.init();
    }
}
