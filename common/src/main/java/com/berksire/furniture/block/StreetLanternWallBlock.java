package com.berksire.furniture.block;

import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class StreetLanternWallBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty BULBS = IntegerProperty.create("bulbs", 0, 1);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final Map<Direction, VoxelShape> SHAPE_SINGLE = createDirectionalShapes(StreetLanternWallBlock::makeSingleShape);
    public static final Map<Direction, VoxelShape> SHAPE_DOUBLE = createDirectionalShapes(StreetLanternWallBlock::makeDoubleShape);

    public StreetLanternWallBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(BULBS, 0)
                .setValue(LIT, true));
    }

    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERLOGGED, BULBS, LIT);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        if (state.getValue(BULBS) == 0) {
            return SHAPE_SINGLE.get(direction);
        } else {
            return SHAPE_DOUBLE.get(direction);
        }
    }

    private static VoxelShape makeSingleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.375, 0.5, 0.5, 0.625, 0.5625, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.5625, 0.4375, 0.6875, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.9375, 0.6875, 0.5, 1), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeDoubleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.5, 0.5, 0.3125, 0.5625, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.5625, 0.4375, 0.375, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0.5, 0.5, 0.9375, 0.5625, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.625, 0.5625, 0.4375, 1, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.9375, 0.6875, 0.5, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.3125, 0.4375, 0.8125, 0.5, 0.9375), BooleanOp.OR);
        return shape;
    }

    private static Map<Direction, VoxelShape> createDirectionalShapes(Supplier<VoxelShape> shapeSupplier) {
        return Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, shapeSupplier.get()));
            }
        });
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.is(this)) {
            return blockState.setValue(BULBS, Math.min(1, blockState.getValue(BULBS) + 1));
        }
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        Direction direction = context.getClickedFace();
        if (direction == Direction.DOWN || direction == Direction.UP) {
            direction = context.getHorizontalDirection().getOpposite();
        }
        BlockState newState = this.defaultBlockState().setValue(FACING, direction);
        if (canSurvive(newState, context.getLevel(), context.getClickedPos())) {
            return newState.setValue(WATERLOGGED, flag);
        }
        return null;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(BULBS) < 1 || super.canBeReplaced(state, useContext);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;
        if (!canSurvive(state, world, pos)) {
            world.destroyBlock(pos, true);
        }
    }
}