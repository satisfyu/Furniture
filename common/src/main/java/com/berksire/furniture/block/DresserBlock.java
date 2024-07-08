package com.berksire.furniture.block;

import com.berksire.furniture.block.entity.DresserBlockEntity;
import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class DresserBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    private static final Map<FurnitureUtil.LineConnectingType, Supplier<VoxelShape>> SHAPES_SUPPLIERS = new HashMap<>();
    private static final Map<Direction, Map<FurnitureUtil.LineConnectingType, VoxelShape>> SHAPES = new EnumMap<>(Direction.class);
    public static final BooleanProperty WATERLOGGED;
    public static final DirectionProperty FACING;
    public static final EnumProperty<FurnitureUtil.LineConnectingType> TYPE;
    private final Supplier<SoundEvent> openSound;
    private final Supplier<SoundEvent> closeSound;

    public DresserBlock(Properties settings, Supplier<SoundEvent> openSound, Supplier<SoundEvent> closeSound) {
        super(settings);
        this.openSound = openSound;
        this.closeSound = closeSound;
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH).setValue(TYPE, FurnitureUtil.LineConnectingType.NONE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockState blockState = this.defaultBlockState().setValue(FACING, facing)
                .setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
        return switch (facing) {
            case EAST -> blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.south()), world.getBlockState(clickedPos.north())));
            case SOUTH -> blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.west()), world.getBlockState(clickedPos.east())));
            case WEST -> blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.north()), world.getBlockState(clickedPos.south())));
            default -> blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.east()), world.getBlockState(clickedPos.west())));
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, TYPE);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Container container) {
                Containers.dropContents(world, pos, container);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DresserBlockEntity blockEntity1) {
                player.openMenu(blockEntity1);
            }

            return InteractionResult.CONSUME;
        }
    }

    public void playSound(Level world, BlockPos pos, boolean isOpen) {
        world.playSound(null, pos, isOpen ? openSound.get() : closeSound.get(), SoundSource.BLOCKS, 1.0f, 1.1f);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DresserBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DresserBlockEntity blockEntity1) {
                blockEntity1.setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public FurnitureUtil.LineConnectingType getType(BlockState state, BlockState left, BlockState right) {
        boolean shape_left_same = isConnectable(left, state);
        boolean shape_right_same = isConnectable(right, state);

        if (shape_left_same && shape_right_same) {
            return FurnitureUtil.LineConnectingType.MIDDLE;
        } else if (shape_left_same) {
            return FurnitureUtil.LineConnectingType.LEFT;
        } else if (shape_right_same) {
            return FurnitureUtil.LineConnectingType.RIGHT;
        }
        return FurnitureUtil.LineConnectingType.NONE;
    }

    protected boolean isConnectable(BlockState state1, BlockState state2) {
        return (state1.getBlock() == state2.getBlock() && state1.getValue(FACING) == state2.getValue(FACING))
                || (state1.getBlock() instanceof DeskBlock && state2.getBlock() instanceof DresserBlock)
                || (state1.getBlock() instanceof DresserBlock && state2.getBlock() instanceof DeskBlock);
    }


    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;

        Direction facing = state.getValue(FACING);
        FurnitureUtil.LineConnectingType type;
        switch (facing) {
            case EAST -> type = getType(state, world.getBlockState(pos.south()), world.getBlockState(pos.north()));
            case SOUTH -> type = getType(state, world.getBlockState(pos.west()), world.getBlockState(pos.east()));
            case WEST -> type = getType(state, world.getBlockState(pos.north()), world.getBlockState(pos.south()));
            default -> type = getType(state, world.getBlockState(pos.east()), world.getBlockState(pos.west()));
        }
        if (state.getValue(TYPE) != type) {
            state = state.setValue(TYPE, type);
        }
        world.setBlock(pos, state, 3);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        FurnitureUtil.LineConnectingType type = state.getValue(TYPE);
        return SHAPES.get(direction).get(type);
    }

    private static VoxelShape makeSingleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.25, 0.1875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.0625, 0.9375, 0.1875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.75, 0.25, 0.1875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.75, 0.9375, 0.1875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.0625, 0.9375, 0.8125, 0.9375), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeRightShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.1875, 0.0625, 0.9375, 0.8125, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.0625, 0.9375, 0.1875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.75, 0, 0.75, 0.9375, 0.1875, 0.9375), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeLeftShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.1875, 0.0625, 1, 0.8125, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.75, 0.25, 0.1875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.25, 0.1875, 0.25), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeMiddleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0.1875, 0.0625, 1, 0.8125, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.8125, 0, 1, 1, 1), BooleanOp.OR);
        return shape;
    }

    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        FACING = BlockStateProperties.HORIZONTAL_FACING;
        TYPE = FurnitureUtil.LINE_CONNECTING_TYPE;
        SHAPES_SUPPLIERS.put(FurnitureUtil.LineConnectingType.NONE, DresserBlock::makeSingleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.LineConnectingType.MIDDLE, DresserBlock::makeMiddleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.LineConnectingType.RIGHT, DresserBlock::makeRightShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.LineConnectingType.LEFT, DresserBlock::makeLeftShape);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            SHAPES.put(direction, new HashMap<>());
            for (Map.Entry<FurnitureUtil.LineConnectingType, Supplier<VoxelShape>> entry : SHAPES_SUPPLIERS.entrySet()) {
                SHAPES.get(direction).put(entry.getKey(), FurnitureUtil.rotateShape(Direction.NORTH, direction, entry.getValue().get()));
            }
        }
    }
}