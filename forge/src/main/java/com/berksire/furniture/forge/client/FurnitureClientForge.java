package com.berksire.furniture.forge.client;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.client.FurnitureClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Furniture.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FurnitureClientForge {

    @SubscribeEvent
    public static void onClientSetup(RegisterEvent event) {
        FurnitureClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        FurnitureClient.onInitializeClient();
    }

}
