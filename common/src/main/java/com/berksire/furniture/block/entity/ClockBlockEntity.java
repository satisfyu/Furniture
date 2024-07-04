package com.berksire.furniture.block.entity;

import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ClockBlockEntity extends BlockEntity {
    public ClockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.CLOCK_BLOCK_ENTITY.get(), pos, state);
    }
}
