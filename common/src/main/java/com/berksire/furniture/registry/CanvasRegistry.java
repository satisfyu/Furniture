package com.berksire.furniture.registry;

import com.berksire.furniture.Furniture;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.decoration.PaintingVariant;

@SuppressWarnings("unused")
public class CanvasRegistry {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(Furniture.MODID, Registries.PAINTING_VARIANT);

    public static final RegistrySupplier<PaintingVariant> LONELY_DAISY = PAINTING_VARIANTS.register("lonely_daisy", () -> new PaintingVariant(16, 16));
    public static final RegistrySupplier<PaintingVariant> SUNFLOWER = PAINTING_VARIANTS.register("sunflower", () -> new PaintingVariant(16, 32));
    public static final RegistrySupplier<PaintingVariant> FORAGING_WOODPECKER = PAINTING_VARIANTS.register("foraging_woodpecker", () -> new PaintingVariant(32, 64));
    public static final RegistrySupplier<PaintingVariant> SLEEPING_FOX = PAINTING_VARIANTS.register("sleeping_fox", () -> new PaintingVariant(16, 48));
    public static final RegistrySupplier<PaintingVariant> HONEY_FALL = PAINTING_VARIANTS.register("honey_fall", () -> new PaintingVariant(16, 64));
    public static final RegistrySupplier<PaintingVariant> RABBIT = PAINTING_VARIANTS.register("rabbit", () -> new PaintingVariant(32, 16));
    public static final RegistrySupplier<PaintingVariant> LAVENDER_FIELDS = PAINTING_VARIANTS.register("lavender_fields", () -> new PaintingVariant(32, 32));
    public static final RegistrySupplier<PaintingVariant> ROSES = PAINTING_VARIANTS.register("roses", () -> new PaintingVariant(32, 48));
    public static final RegistrySupplier<PaintingVariant> ANTS = PAINTING_VARIANTS.register("ants", () -> new PaintingVariant(48, 16));
    public static final RegistrySupplier<PaintingVariant> BUTTERFLY = PAINTING_VARIANTS.register("butterfly", () -> new PaintingVariant(48, 32));
    public static final RegistrySupplier<PaintingVariant> WATERFALL = PAINTING_VARIANTS.register("waterfall", () -> new PaintingVariant(48, 64));
    public static final RegistrySupplier<PaintingVariant> SITTING_BEAR = PAINTING_VARIANTS.register("sitting_bear", () -> new PaintingVariant(48, 48));
    public static final RegistrySupplier<PaintingVariant> STRONG = PAINTING_VARIANTS.register("strong", () -> new PaintingVariant(64, 16));
    public static final RegistrySupplier<PaintingVariant> SAKURA_GROVE = PAINTING_VARIANTS.register("sakura_grove", () -> new PaintingVariant(64, 32));
    public static final RegistrySupplier<PaintingVariant> TULIP_FIELDS = PAINTING_VARIANTS.register("tulip_fields", () -> new PaintingVariant(64, 48));
    public static final RegistrySupplier<PaintingVariant> HOPPER = PAINTING_VARIANTS.register("hopper", () -> new PaintingVariant(64, 64));

    static {
        PAINTING_VARIANTS.register();
    }
}
