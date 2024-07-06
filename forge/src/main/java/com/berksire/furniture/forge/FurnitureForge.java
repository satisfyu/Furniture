package com.berksire.furniture.forge;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.registry.FlammableBlockRegistry;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Furniture.MODID)
public class FurnitureForge {
    public FurnitureForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Furniture.MODID, modEventBus);
        Furniture.init();

        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(FlammableBlockRegistry::registerFlammables);
        Furniture.commonSetup();
    }
}
