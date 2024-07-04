package com.berksire.furniture.registry;

import com.berksire.furniture.Furniture;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;

public final class TagRegistry {
    public static final TagKey<PaintingVariant> PAINTINGS = paintingTag();
    public static final TagKey<Item> TRASH_BAG_BLACKLIST = itemTag();

    private static TagKey<PaintingVariant> paintingTag() {
        return TagKey.create(Registries.PAINTING_VARIANT, new ResourceLocation(Furniture.MODID, "paintings"));
    }

    private static TagKey<Item> itemTag() {
        return TagKey.create(Registries.ITEM, new ResourceLocation(Furniture.MODID, "trash_bag_blacklist"));
    }
}
