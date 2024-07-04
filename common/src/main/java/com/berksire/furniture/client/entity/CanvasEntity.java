package com.berksire.furniture.client.entity;

import com.berksire.furniture.registry.EntityTypeRegistry;
import com.berksire.furniture.registry.ObjectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class CanvasEntity extends Painting {

    public CanvasEntity(EntityType<? extends Painting> type, Level level) {
        super(type, level);
    }

    
    public CanvasEntity(Level level, BlockPos pos, Direction direction, Holder<PaintingVariant> variant) {
        super(EntityTypeRegistry.CANVAS.get(), level);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
        this.setDirection(direction);
        this.setVariant(variant);
        this.fixPosition();
    }

    private void fixPosition() {
        this.setPos(this.getX(), this.getY(), this.getZ());
    }

    @Override
    public ItemEntity spawnAtLocation(ItemLike item) {
        return super.spawnAtLocation(ObjectRegistry.CANVAS.get());
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(ObjectRegistry.CANVAS.get());
    }
}