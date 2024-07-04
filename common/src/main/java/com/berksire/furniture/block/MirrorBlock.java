package com.berksire.furniture.block;

import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MirrorBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<FurnitureUtil.VerticalConnectingType> TYPE = FurnitureUtil.VerticalConnectingType.VERTICAL_CONNECTING_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final Map<FurnitureUtil.VerticalConnectingType, Supplier<VoxelShape>> SHAPES_SUPPLIERS = new HashMap<>();
    private static final Map<Direction, Map<FurnitureUtil.VerticalConnectingType, VoxelShape>> SHAPES = new EnumMap<>(Direction.class);

    static {
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.NONE, MirrorBlock::makeSingleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.MIDDLE, MirrorBlock::makeMiddleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.TOP, MirrorBlock::makeTopShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.BOTTOM, MirrorBlock::makeBottomShape);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPES.put(direction, new HashMap<>());
            for (Map.Entry<FurnitureUtil.VerticalConnectingType, Supplier<VoxelShape>> entry : SHAPES_SUPPLIERS.entrySet()) {
                SHAPES.get(direction).put(entry.getKey(), FurnitureUtil.rotateShape(Direction.NORTH, direction, entry.getValue().get()));
            }
        }
    }

    public MirrorBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TYPE, FurnitureUtil.VerticalConnectingType.NONE)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, TYPE, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockState blockState = this.defaultBlockState().setValue(FACING, facing);

        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        blockState = blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.above()), world.getBlockState(clickedPos.below())));

        return blockState.setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
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

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        FurnitureUtil.VerticalConnectingType type = state.getValue(TYPE);
        return SHAPES.get(direction).get(type);
    }

    private static VoxelShape makeTopShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.8125, 0.875, 0.9375, 0.9375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.875, 0.1875, 0.8125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.875, 0.9375, 0.8125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.9375, 0.8125, 0.8125, 1), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeBottomShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0.875, 0.9375, 0.1875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.875, 0.1875, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0.1875, 0.875, 0.9375, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.1875, 0.9375, 0.8125, 1, 1), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeSingleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.0625, 0.875, 0.9375, 0.1875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.8125, 0.875, 0.9375, 0.9375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.875, 0.1875, 0.8125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0.1875, 0.875, 0.9375, 0.8125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.1875, 0.9375, 0.8125, 0.8125, 1), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeMiddleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.875, 0.1875, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.875, 0.9375, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.9375, 0.8125, 1, 1), BooleanOp.OR);
        return shape;
    }
}
