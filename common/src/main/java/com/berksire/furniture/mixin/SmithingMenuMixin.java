package com.berksire.furniture.mixin;

import com.berksire.furniture.registry.ObjectRegistry;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingMenu.class)
public class SmithingMenuMixin {

    @Inject(method = "isValidBlock", at = @At("HEAD"), cancellable = true)
    private void injectIsValidBlock(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(Blocks.SMITHING_TABLE) || blockState.is(ObjectRegistry.TOOL_BOX.get())) {
            cir.setReturnValue(true);
        }
    }
}
