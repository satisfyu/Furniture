package com.berksire.furniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;

public class TerrariumBlock extends FacingBlock {
    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), Shapes.box(0.0625, 0.125, 0.0625, 0.9375, 0.875, 0.9375));

    public TerrariumBlock(Properties settings) {
        super(settings);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
