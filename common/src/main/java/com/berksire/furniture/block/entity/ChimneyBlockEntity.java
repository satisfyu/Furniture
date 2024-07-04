package com.berksire.furniture.block.entity;

import com.berksire.furniture.block.ChimneyBlock;
import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChimneyBlockEntity extends BlockEntity {
    public ChimneyBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.CHIMNEY_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide && level.getRandom().nextInt(12000) == 0) {
            level.setBlock(pos, state.setValue(ChimneyBlock.SOOTY, true), 3);
        }
    }
}