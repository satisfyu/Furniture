package com.berksire.furniture.mixin;

import dev.architectury.platform.Platform;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ConditionalMixinPlugin implements IMixinConfigPlugin {

    Logger logger = LoggerFactory.getLogger(ConditionalMixinPlugin.class);

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            Class<?> mixin = Class.forName(mixinClassName);
            if (mixin.isAnnotationPresent(ConditionalMixin.class)) {
                String requiredMod = mixin.getAnnotation(ConditionalMixin.class).value();
                boolean modIsLoaded = Platform.isModLoaded(requiredMod);
                if (Platform.isDevelopmentEnvironment())
                    logger.info("Mixin {} requires {} to be loaded, result: {}", mixinClassName, requiredMod, modIsLoaded);
            }
        } catch (ClassNotFoundException ignored) {} // should never trigger
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }
}
