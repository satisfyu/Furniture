package com.berksire.furniture.mixin;

import com.lowdragmc.shimmer.client.light.LightCounter;
import com.lowdragmc.shimmer.platform.services.IPlatformHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@ConditionalMixin("shimmer")
@Mixin(value = LightCounter.Render.class, remap = false)
public class LightCounterMixin {

    /**
     * @author cph101
     * @reason I do not want to see the debug hud every single time
     */
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/lowdragmc/shimmer/platform/services/IPlatformHelper;isDevelopmentEnvironment()Z"))
    private static boolean isDevelopmentEnvironment(IPlatformHelper instance) {
        return false;
    }

}
