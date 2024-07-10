package com.berksire.furniture.block;

import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
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
public class LampBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<FurnitureUtil.VerticalConnectingType> TYPE = FurnitureUtil.VerticalConnectingType.VERTICAL_CONNECTING_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    private static final Map<FurnitureUtil.VerticalConnectingType, Supplier<VoxelShape>> SHAPES_SUPPLIERS = new HashMap<>();
    private static final Map<FurnitureUtil.VerticalConnectingType, VoxelShape> SHAPES = new HashMap<>();

    static {
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.NONE, LampBlock::makeSingleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.MIDDLE, LampBlock::makeMiddleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.TOP, LampBlock::makeTopShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.BOTTOM, LampBlock::makeBottomShape);

        for (Map.Entry<FurnitureUtil.VerticalConnectingType, Supplier<VoxelShape>> entry : SHAPES_SUPPLIERS.entrySet()) {
            SHAPES.put(entry.getKey(), entry.getValue().get());
        }
    }

    public LampBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TYPE, FurnitureUtil.VerticalConnectingType.NONE)
                .setValue(LIT, false)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level world = context.getLevel();
        BlockPos belowPos = clickedPos.below();
        BlockPos abovePos = clickedPos.above();

        BlockState belowState = world.getBlockState(belowPos);
        BlockState aboveState = world.getBlockState(abovePos);

        BlockState blockState = this.defaultBlockState();
        blockState = blockState.setValue(TYPE, getType(aboveState, belowState));
        blockState = blockState.setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
        return blockState;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClientSide) return;

        FurnitureUtil.VerticalConnectingType type = getType(world.getBlockState(pos.above()), world.getBlockState(pos.below()));
        if (state.getValue(TYPE) != type) {
            world.setBlock(pos, state.setValue(TYPE, type), 3);
        }
    }

    public FurnitureUtil.VerticalConnectingType getType(BlockState above, BlockState below) {
        boolean shapeAboveSame = above.getBlock() instanceof LampBlock;
        boolean shapeBelowSame = below.getBlock() instanceof LampBlock;

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
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(TYPE));
    }

    private static VoxelShape makeTopShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.4375, 0, 0.4375, 0.5625, 1, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.125, 0.875, 1, 0.875), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeBottomShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.75, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.75, 0.4375, 0.5625, 1, 0.5625), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeSingleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.375, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.4375, 0.375, 0.4375, 0.5625, 1, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.5, 0.125, 0.875, 1, 0.875), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeMiddleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.4375, 0, 0.4375, 0.5625, 1, 0.5625), BooleanOp.OR);
        return shape;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.isShiftKeyDown() && (state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.NONE || state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.TOP)) {
            boolean lit = !state.getValue(LIT);
            world.setBlock(pos, state.setValue(LIT, lit), 3);
            world.playSound(null, pos, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, world, pos, player, hand, hit);
    }
}
