package com.berksire.furniture.block.entity;

import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GrandfatherClockBlockEntity extends BlockEntity {
    public GrandfatherClockBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.GRANDFATHER_CLOCK_BLOCK_ENTITY.get(), pos, state);
    }
}
