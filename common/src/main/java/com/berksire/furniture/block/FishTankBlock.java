package com.berksire.furniture.block;

import com.berksire.furniture.block.entity.FishTankBlockEntity;
import com.berksire.furniture.client.entity.ActualFishTankEntity;
import com.berksire.furniture.client.model.FishTankModel;
import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class FishTankBlock extends BaseEntityBlock implements EntityBlock {
    public static final EnumProperty<BedPart> PART = EnumProperty.create("part", BedPart.class);
    public static final BooleanProperty HAS_COD = BooleanProperty.create("has_cod");
    public static final BooleanProperty HAS_PUFFERFISH = BooleanProperty.create("has_pufferfish");
    public static final BooleanProperty HAS_SALMON = BooleanProperty.create("has_salmon");
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public FishTankBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PART, BedPart.FOOT)
                .setValue(HAS_COD, false)
                .setValue(HAS_PUFFERFISH, false)
                .setValue(HAS_SALMON, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, HAS_COD, HAS_PUFFERFISH, HAS_SALMON, FACING);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(-1, 0, 0, 1, 0.125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-0.9375, 0.125, 0.0625, 0.9375, 0.875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(-1, 0.875, 0, 1, 1, 1), BooleanOp.OR);

        return shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (state.getValue(PART) == BedPart.HEAD) {
            for (int i = 0; i < 2; ++i) {
                world.addParticle(net.minecraft.core.particles.ParticleTypes.BUBBLE_COLUMN_UP, pos.getX() + 0.5D + (random.nextDouble() - 0.5D), pos.getY() + 1.0D, pos.getZ() + 0.5D + (random.nextDouble() - 0.5D), 0.0D, 0.1D, 0.0D);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FishTankBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, EntityTypeRegistry.FISH_TANK_BLOCK_ENTITY.get(), FishTankBlockEntity::tick);
    }
}
