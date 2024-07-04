package com.berksire.furniture.block;

import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class CurtainBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<FurnitureUtil.VerticalConnectingType> TYPE;
    public static final BooleanProperty OPEN;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty WATERLOGGED;

    public CurtainBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(((((this.stateDefinition.any().setValue(FACING, Direction.NORTH)).setValue(TYPE, FurnitureUtil.VerticalConnectingType.NONE)).setValue(OPEN, false).setValue(POWERED, false)).setValue(WATERLOGGED, false)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockState blockState = this.defaultBlockState().setValue(FACING, facing);

        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        if (world.hasNeighborSignal(clickedPos)) {
            blockState = blockState.setValue(OPEN, true).setValue(POWERED, true);
        }

        blockState = blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.above()), world.getBlockState(clickedPos.below())));

        return blockState.setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;

        boolean powered = world.hasNeighborSignal(pos);
        if (powered != state.getValue(POWERED)) {
            if (state.getValue(OPEN) != powered) {
                state = state.setValue(OPEN, powered);
                world.playSound(null, pos, Curtainsound(powered), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            state = state.setValue(POWERED, powered);
            if (state.getValue(WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
        }
        FurnitureUtil.VerticalConnectingType type = getType(state, world.getBlockState(pos.above()), world.getBlockState(pos.below()));
        if (state.getValue(TYPE) != type) {
            state = state.setValue(TYPE, type);
        }
        world.setBlock(pos, state, 3);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return toggleCurtain(state, level, pos, player);
    }

    public InteractionResult toggleCurtain(BlockState state, Level level, BlockPos pos, Player player) {
        state = state.cycle(OPEN);
        level.setBlock(pos, state, 3);
        if (player == null || !player.isCrouching()) toggleCurtain(state, level, pos, state.getValue(OPEN));
        level.playSound(null, pos, Curtainsound(state.getValue(OPEN)), SoundSource.BLOCKS, 1.0F, 1.0F);

        if (state.getValue(WATERLOGGED)) level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void toggleCurtain(BlockState state, Level level, BlockPos pos, boolean open) {
        BlockState updateState = state;
        BlockPos updatePos = pos;
        if (state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.MIDDLE || state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.BOTTOM) {
            int heightUp = level.dimensionType().height() - updatePos.getY();
            for (int i = 0; i < heightUp; i++) {
                BlockState above = level.getBlockState(updatePos.above());
                if (above.is(state.getBlock()) && above.getValue(FACING) == updateState.getValue(FACING) && above.getValue(OPEN) != open) {
                    updateState = above;
                    updatePos = updatePos.above();
                    level.setBlock(updatePos, updateState.setValue(OPEN, open), 3);
                } else {
                    break;
                }
            }
        }
        if (state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.MIDDLE || state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.TOP) {
            updateState = state;
            updatePos = pos;
            int heightDown = level.dimensionType().minY() - updatePos.getY();
            heightDown = (heightDown < 0) ? -heightDown : heightDown;
            for (int i = 0; i < heightDown; i++) {
                BlockState below = level.getBlockState(updatePos.below());
                if (below.is(state.getBlock()) && below.getValue(FACING) == updateState.getValue(FACING) && below.getValue(OPEN) != open) {
                    updateState = below;
                    updatePos = updatePos.below();
                    level.setBlock(updatePos, updateState.setValue(OPEN, open), 3);
                } else {
                    break;
                }
            }
        }
    }

    public static SoundEvent Curtainsound(boolean open) {
        if (open) {
            return SoundEvents.WOOL_FALL;
        }
        return SoundEvents.WOOL_PLACE;
    }

    public FurnitureUtil.VerticalConnectingType getType(BlockState state, BlockState above, BlockState below) {
        boolean shape_above_same = above.getBlock() == state.getBlock() && above.getValue(FACING) == state.getValue(FACING)
                && above.getValue(OPEN) == state.getValue(OPEN);
        boolean shape_below_same = below.getBlock() == state.getBlock() && below.getValue(FACING) == state.getValue(FACING)
                && below.getValue(OPEN) == state.getValue(OPEN);

        if (shape_above_same && !shape_below_same) {
            return FurnitureUtil.VerticalConnectingType.BOTTOM;
        } else if (!shape_above_same && shape_below_same) {
            return FurnitureUtil.VerticalConnectingType.TOP;
        } else if (shape_above_same) {
            return FurnitureUtil.VerticalConnectingType.MIDDLE;
        }
        return FurnitureUtil.VerticalConnectingType.NONE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE, OPEN, POWERED, WATERLOGGED);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    private static VoxelShape makeTopOrSingleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0.9375, 1, 0.9375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 1, 1, 0.9375), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeOtherShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 1, 1, 0.9375), BooleanOp.OR);
        return shape;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        FurnitureUtil.VerticalConnectingType type = state.getValue(TYPE);
        if (type == FurnitureUtil.VerticalConnectingType.TOP || type == FurnitureUtil.VerticalConnectingType.NONE) {
            return SHAPE_TOP_OR_SINGLE.get(direction);
        } else {
            return SHAPE_OTHER.get(direction);
        }
    }

    public static final Map<Direction, VoxelShape> SHAPE_TOP_OR_SINGLE = Util.make(new HashMap<>(), map -> {
        Supplier<VoxelShape> voxelShapeSupplier = CurtainBlock::makeTopOrSingleShape;
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public static final Map<Direction, VoxelShape> SHAPE_OTHER = Util.make(new HashMap<>(), map -> {
        Supplier<VoxelShape> voxelShapeSupplier = CurtainBlock::makeOtherShape;
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    static {
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        TYPE = FurnitureUtil.VerticalConnectingType.VERTICAL_CONNECTING_TYPE;
        OPEN = BlockStateProperties.OPEN;
        POWERED = BlockStateProperties.POWERED;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
    }
}
