package com.berksire.furniture.fabric.client;

import com.berksire.furniture.client.FurnitureClient;
import net.fabricmc.api.ClientModInitializer;

public class FurnitureClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FurnitureClient.preInitClient();
        FurnitureClient.onInitializeClient();
    }
}
