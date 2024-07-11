package com.berksire.furniture.mixin;

import com.berksire.furniture.client.ShimmerCompat;
import com.lowdragmc.shimmer.client.light.LightManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ConditionalMixin("shimmer")
@Mixin(value = LightManager.class, remap = false)
public class LightManagerMixin {
    @Final
    @Shadow public static LightManager INSTANCE;

    @Inject(method = "loadConfig", at = @At("RETURN"))
    private void loadConfig(CallbackInfo ci) {
        ShimmerCompat.registerLights(INSTANCE);
    }
}
