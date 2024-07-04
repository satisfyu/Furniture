package com.berksire.furniture.block;

import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
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

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class SteamVentBlock extends Block implements SimpleWaterloggedBlock {
    public static final EnumProperty<FurnitureUtil.VerticalConnectingType> TYPE = FurnitureUtil.VerticalConnectingType.VERTICAL_CONNECTING_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty PARTICLES_ENABLED = BooleanProperty.create("particles_enabled");

    private static final Map<FurnitureUtil.VerticalConnectingType, Supplier<VoxelShape>> SHAPES_SUPPLIERS = new HashMap<>();
    private static final Map<FurnitureUtil.VerticalConnectingType, VoxelShape> SHAPES = new EnumMap<>(FurnitureUtil.VerticalConnectingType.class);

    static {
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.NONE, SteamVentBlock::makeSingleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.MIDDLE, SteamVentBlock::makeMiddleShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.TOP, SteamVentBlock::makeTopShape);
        SHAPES_SUPPLIERS.put(FurnitureUtil.VerticalConnectingType.BOTTOM, SteamVentBlock::makeBottomShape);

        for (Map.Entry<FurnitureUtil.VerticalConnectingType, Supplier<VoxelShape>> entry : SHAPES_SUPPLIERS.entrySet()) {
            SHAPES.put(entry.getKey(), entry.getValue().get());
        }
    }

    public SteamVentBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TYPE, FurnitureUtil.VerticalConnectingType.NONE)
                .setValue(WATERLOGGED, false)
                .setValue(PARTICLES_ENABLED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, PARTICLES_ENABLED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = this.defaultBlockState();

        Level world = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();

        blockState = blockState.setValue(TYPE, getType(blockState, world.getBlockState(clickedPos.above()), world.getBlockState(clickedPos.below())));

        return blockState.setValue(WATERLOGGED, world.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    @Override
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
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        FurnitureUtil.VerticalConnectingType type = state.getValue(TYPE);
        return SHAPES.get(type);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            boolean particlesEnabled = state.getValue(PARTICLES_ENABLED);
            world.setBlock(pos, state.setValue(PARTICLES_ENABLED, !particlesEnabled), 3);
            String messageKey = particlesEnabled ? "tooltip.furniture.vent_disabled" : "tooltip.furniture.vent_enabled";
            player.displayClientMessage(Component.translatable(messageKey), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(PARTICLES_ENABLED) && (state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.NONE || state.getValue(TYPE) == FurnitureUtil.VerticalConnectingType.BOTTOM)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() - 1;
            double z = pos.getZ() + 0.5;
            double vx = 0;
            double vy = 0.02 + random.nextDouble() * 0.02;
            double vz = 0;
            world.addParticle(ParticleTypes.CLOUD, x, y, z, vx, vy, vz);
        }
    }

    private static VoxelShape makeTopShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 0.9375, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeBottomShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.375, 0.25, 0.75, 1, 0.75), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeSingleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.375, 0.25, 0.75, 1, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.875, 0.1875, 0.8125, 1, 0.8125), BooleanOp.OR);
        return shape;
    }

    private static VoxelShape makeMiddleShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 0.9375, 0.75), BooleanOp.OR);
        return shape;
    }
}
