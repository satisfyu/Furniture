package com.berksire.furniture.block;

import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BenchBlock extends LineConnectingBlock {
    public static final Map<Direction, VoxelShape> SHAPE;
    public static final Map<Direction, VoxelShape> MIDDLE_SHAPE;
    public static final Map<Direction, VoxelShape> LEFT_SHAPE;
    public static final Map<Direction, VoxelShape> RIGHT_SHAPE;
    public static final Map<Direction, VoxelShape> NONE_REST_SHAPE;
    public static final Map<Direction, VoxelShape> LEFT_REST_SHAPE;
    public static final Map<Direction, VoxelShape> RIGHT_REST_SHAPE;
    public static final BooleanProperty REST = BooleanProperty.create("rest");

    private static final Supplier<VoxelShape> noneShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.1875, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.0625, 0.9375, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.8125, 0.1875, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.8125, 0.9375, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.25, 0.0625, 0.9375, 0.375, 0.9375), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> noneRestShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.1875, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.0625, 0.9375, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.8125, 0.1875, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.8125, 0.9375, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.25, 0.0625, 0.9375, 0.375, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0.375, 0.8125, 0.9375, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.375, 0.8125, 0.1875, 1, 0.9375), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> middleShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0.0625, 1, 0.375, 0.9375), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> leftShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.1875, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.8125, 0.1875, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.25, 0.0625, 1, 0.375, 0.9375), BooleanOp.OR);        return shape;
    };

    private static final Supplier<VoxelShape> leftRestShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.1875, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.8125, 0.1875, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.375, 0.8125, 0.1875, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.25, 0.0625, 1, 0.375, 0.9375), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> rightShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.0625, 0.9375, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.8125, 0.9375, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0.0625, 0.9375, 0.375, 0.9375), BooleanOp.OR);
        return shape;
    };

    private static final Supplier<VoxelShape> rightRestShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.0625, 0.9375, 0.25, 0.1875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0, 0.8125, 0.9375, 0.25, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.8125, 0.375, 0.8125, 0.9375, 1, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0.0625, 0.9375, 0.375, 0.9375), BooleanOp.OR);
        return shape;
    };

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Map<Direction, VoxelShape> voxelShape;
        boolean rest = state.getValue(REST);
        switch (state.getValue(TYPE)) {
            case MIDDLE -> voxelShape = MIDDLE_SHAPE;
            case LEFT -> voxelShape = rest ? LEFT_REST_SHAPE : LEFT_SHAPE;
            case RIGHT -> voxelShape = rest ? RIGHT_REST_SHAPE : RIGHT_SHAPE;
            default -> voxelShape = rest ? NONE_REST_SHAPE : SHAPE;
        }
        return voxelShape.get(state.getValue(FACING));
    }

    public BenchBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(REST, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REST, FACING, TYPE);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown()) {
            return toggleRest(state, world, pos);
        }
        return FurnitureUtil.onUse(world, player, hand, hit, -0.1);
    }

    public @NotNull InteractionResult toggleRest(BlockState state, Level world, BlockPos pos) {
        boolean newRestState = !state.getValue(REST);
        updateConnectedBlocks(state, world, pos, newRestState);
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        FurnitureUtil.LineConnectingType type = getType(state, world.getBlockState(pos.relative(state.getValue(FACING).getClockWise())), world.getBlockState(pos.relative(state.getValue(FACING).getCounterClockWise())));
        if (state.getValue(TYPE) != type) {
            state = state.setValue(TYPE, type);
            world.setBlock(pos, state, 3);
        }
    }

    public FurnitureUtil.LineConnectingType getType(BlockState state, BlockState clockwise, BlockState counterClockwise) {
        boolean shape_clockwise_same = clockwise.getBlock() == state.getBlock() && clockwise.getValue(FACING) == state.getValue(FACING)
                && clockwise.getValue(REST) == state.getValue(REST);
        boolean shape_counterClockwise_same = counterClockwise.getBlock() == state.getBlock() && counterClockwise.getValue(FACING) == state.getValue(FACING)
                && counterClockwise.getValue(REST) == state.getValue(REST);

        if (shape_clockwise_same && !shape_counterClockwise_same) {
            return FurnitureUtil.LineConnectingType.RIGHT;
        } else if (!shape_clockwise_same && shape_counterClockwise_same) {
            return FurnitureUtil.LineConnectingType.LEFT;
        } else if (shape_clockwise_same) {
            return FurnitureUtil.LineConnectingType.MIDDLE;
        }
        return FurnitureUtil.LineConnectingType.NONE;
    }

    private void updateConnectedBlocks(BlockState state, Level world, BlockPos pos, boolean newRestState) {
        world.setBlock(pos, state.setValue(REST, newRestState), 3);
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (Direction direction : directions) {
            BlockPos neighborPos = pos.relative(direction);
            while (world.getBlockState(neighborPos).getBlock() instanceof BenchBlock) {
                world.setBlock(neighborPos, world.getBlockState(neighborPos).setValue(REST, newRestState), 3);
                neighborPos = neighborPos.relative(direction);
            }
        }
    }


    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        FurnitureUtil.onStateReplaced(world, pos);
    }

    static {
        SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, noneShapeSupplier.get()));
            }
        });
        MIDDLE_SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, middleShapeSupplier.get()));
            }
        });
        LEFT_SHAPE = Util.make(new HashMap<>(), map -> {
            map.put(Direction.NORTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.NORTH, rightShapeSupplier.get()));
            map.put(Direction.SOUTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.SOUTH, rightShapeSupplier.get()));
            map.put(Direction.EAST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.EAST, rightShapeSupplier.get()));
            map.put(Direction.WEST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.WEST, rightShapeSupplier.get()));
        });
        RIGHT_SHAPE = Util.make(new HashMap<>(), map -> {
            map.put(Direction.NORTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.NORTH, leftShapeSupplier.get()));
            map.put(Direction.SOUTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.SOUTH, leftShapeSupplier.get()));
            map.put(Direction.EAST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.EAST, leftShapeSupplier.get()));
            map.put(Direction.WEST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.WEST, leftShapeSupplier.get()));
        });
        NONE_REST_SHAPE = Util.make(new HashMap<>(), map -> {
            for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
                map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, noneRestShapeSupplier.get()));
            }
        });
        LEFT_REST_SHAPE = Util.make(new HashMap<>(), map -> {
            map.put(Direction.NORTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.NORTH, rightRestShapeSupplier.get()));
            map.put(Direction.SOUTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.SOUTH, rightRestShapeSupplier.get()));
            map.put(Direction.EAST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.EAST, rightRestShapeSupplier.get()));
            map.put(Direction.WEST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.WEST, rightRestShapeSupplier.get()));
        });
        RIGHT_REST_SHAPE = Util.make(new HashMap<>(), map -> {
            map.put(Direction.NORTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.NORTH, leftRestShapeSupplier.get()));
            map.put(Direction.SOUTH, FurnitureUtil.rotateShape(Direction.NORTH, Direction.SOUTH, leftRestShapeSupplier.get()));
            map.put(Direction.EAST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.EAST, leftRestShapeSupplier.get()));
            map.put(Direction.WEST, FurnitureUtil.rotateShape(Direction.NORTH, Direction.WEST, leftRestShapeSupplier.get()));
        });
    }

}
