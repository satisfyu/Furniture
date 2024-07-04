package com.berksire.furniture.forge;

import com.berksire.furniture.Furniture;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Furniture.MODID)
public class FurnitureForge {
    public FurnitureForge() {
        EventBuses.registerModEventBus(Furniture.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        Furniture.init();
    }
}
