package com.berksire.furniture.block;

import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
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
public class StreetLanternBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final EnumProperty<FurnitureUtil.VerticalConnectingType> TYPE = FurnitureUtil.VerticalConnectingType.VERTICAL_CONNECTING_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final IntegerProperty BULBS = IntegerProperty.create("bulbs", 0, 1);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private static final Supplier<VoxelShape> SINGLE_SHAPE_SUPPLIER = StreetLanternBlock::makeSingleShape;
    private static final Supplier<VoxelShape> DOUBLE_SHAPE_SUPPLIER = StreetLanternBlock::makeDoubleShape;
    private static final Supplier<VoxelShape> MIDDLE_SHAPE_SUPPLIER = StreetLanternBlock::makeMiddleShape;
    private static final Supplier<VoxelShape> BOTTOM_SHAPE_SUPPLIER = StreetLanternBlock::makeBottomShape;

    public static final Map<Direction, VoxelShape> SINGLE_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, SINGLE_SHAPE_SUPPLIER.get()));
        }
    });

    public static final Map<Direction, VoxelShape> DOUBLE_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, DOUBLE_SHAPE_SUPPLIER.get()));
        }
    });

    public static final Map<Direction, VoxelShape> MIDDLE_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, MIDDLE_SHAPE_SUPPLIER.get()));
        }
    });

    public static final Map<Direction, VoxelShape> BOTTOM_SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, BOTTOM_SHAPE_SUPPLIER.get()));
        }
    });

    public StreetLanternBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TYPE, FurnitureUtil.VerticalConnectingType.NONE)
                .setValue(WATERLOGGED, false)
                .setValue(BULBS, 0)
                .setValue(LIT, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, TYPE, WATERLOGGED, BULBS, LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        BlockPos belowPos = clickedPos.below();
        BlockState belowBlockState = context.getLevel().getBlockState(belowPos);

        if (!belowBlockState.is(this)
                && !(belowBlockState.getBlock() instanceof WallBlock)
                && !(belowBlockState.getBlock() instanceof FenceBlock)
                && !belowBlockState.isFaceSturdy(context.getLevel(), belowPos, Direction.UP)) {
            return null;
        }

        BlockState blockState = context.getLevel().getBlockState(clickedPos);
        if (blockState.is(this) && (blockState.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.TOP || blockState.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.NONE)) {
            return blockState.setValue(BULBS, Math.min(1, blockState.getValue(BULBS) + 1));
        }

        boolean flag = context.getLevel().getFluidState(clickedPos).getType() == Fluids.WATER;
        Direction direction = context.getHorizontalDirection().getOpposite();
        blockState = this.defaultBlockState().setValue(FACING, direction);
        blockState = blockState.setValue(TYPE, getType(blockState, context.getLevel().getBlockState(clickedPos.above()), context.getLevel().getBlockState(clickedPos.below())));

        return blockState.setValue(WATERLOGGED, flag);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = world.getBlockState(belowPos);

        return belowState.is(this)
                || belowState.getBlock() instanceof WallBlock
                || belowState.getBlock() instanceof FenceBlock
                || belowState.isFaceSturdy(world, belowPos, Direction.UP);
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;
        FurnitureUtil.VerticalConnectingType type = getType(state, world.getBlockState(pos.above()), world.getBlockState(pos.below()));
        if (state.getValue(TYPE) != type) {
            state = state.setValue(TYPE, type);
        }
        world.setBlock(pos, state, 3);
    }

    public FurnitureUtil.VerticalConnectingType getType(BlockState state, BlockState above, BlockState below) {
        boolean shapeAboveSame = above.getBlock() == state.getBlock();
        boolean shapeBelowSame = below.getBlock() == state.getBlock();
        if (shapeAboveSame && shapeBelowSame) {
            return FurnitureUtil.VerticalConnectingType.MIDDLE;
        } else if (shapeAboveSame) {
            return FurnitureUtil.VerticalConnectingType.BOTTOM;
        } else if (shapeBelowSame) {
            return FurnitureUtil.VerticalConnectingType.TOP;
        } else {
            return FurnitureUtil.VerticalConnectingType.NONE;
        }
    }

    private static VoxelShape makeDoubleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.5625, 0.3125, 0.375, 1, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.625, 0.5625, 0.3125, 1, 1, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.5, 0.375, 0.3125, 0.5625, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.6875, 0.5, 0.375, 0.9375, 0.5625, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.375, 0.625, 0.125, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.125, 0.4375, 0.5625, 0.375, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.25, 0.5, 0.875, 0.5, 0.5), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeBottomShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 1, 0.6875), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeSingleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.5, 0.3125, 0.6875, 0.9375, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0.4375, 0.375, 0.625, 0.5, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.125, 0.4375, 0.5625, 0.4375, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.375, 0.625, 0.125, 0.625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeMiddleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.375, 0, 0.375, 0.625, 1, 0.625), BooleanOp.OR);
        return shape;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        FurnitureUtil.VerticalConnectingType type = state.getValue(TYPE);
        if (state.getValue(BULBS) == 1) {
            return DOUBLE_SHAPE.get(direction);
        }
        return switch (type) {
            case MIDDLE -> MIDDLE_SHAPE.get(direction);
            case BOTTOM -> BOTTOM_SHAPE.get(direction);
            default -> SINGLE_SHAPE.get(direction);
        };
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(BULBS) < 1 || super.canBeReplaced(state, useContext);
    }

    public static boolean canProvideLight(BlockState state) {
        return state.getValue(LIT) && (state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.TOP || state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.NONE);
    }

    public static int vanillaLightLevel(BlockState state) {
        return state.getValue(LIT) ? 15 : 0;
    }
}