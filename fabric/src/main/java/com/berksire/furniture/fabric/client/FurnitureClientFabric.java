package com.berksire.furniture.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import com.berksire.furniture.client.FurnitureClient;

public class FurnitureClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FurnitureClient.preInitClient();
        FurnitureClient.onInitializeClient();
    }
}
