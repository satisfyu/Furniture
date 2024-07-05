package com.berksire.furniture.block;

import com.berksire.furniture.block.entity.FishTankBlockEntity;
import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
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

        return rotate(shape, Direction.NORTH, state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 0) {
            world.playLocalSound(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.05F, 0.95F, false);
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
        return createTickerHelper(blockEntityType, EntityTypeRegistry.FISH_TANK_BLOCK_ENTITY.get(), (level1, pos, state, blockEntity) -> FishTankBlockEntity.tick(level1, pos, blockEntity));
    }

    private static VoxelShape rotate(VoxelShape shape, Direction originalDir, Direction newDir) {
        if (originalDir != newDir) {
            VoxelShape[] newShape = new VoxelShape[]{Shapes.empty()};
            shape.forAllBoxes((x, y, z, a, b, c) -> {
                double i = 1 - c;
                double j = 1 - z;
                newShape[0] = Shapes.or(newShape[0], Shapes.box(Math.min(i, j), y, x, Math.max(i, j), b, a));
            });
            return rotate(newShape[0], originalDir.getClockWise(), newDir);
        }
        return shape;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, net.minecraft.world.entity.player.Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        FishTankBlockEntity blockEntity = (FishTankBlockEntity) world.getBlockEntity(pos);

        if (blockEntity != null) {
            if (itemStack.getItem() == Items.SALMON_BUCKET && !blockEntity.hasSalmon()) {
                blockEntity.setHasSalmon(true);
                world.setBlock(pos, state.setValue(HAS_SALMON, true), 3);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                return InteractionResult.SUCCESS;
            } else if (itemStack.getItem() == Items.PUFFERFISH_BUCKET && !blockEntity.hasPufferfish()) {
                blockEntity.setHasPufferfish(true);
                world.setBlock(pos, state.setValue(HAS_PUFFERFISH, true), 3);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                return InteractionResult.SUCCESS;
            } else if (itemStack.getItem() == Items.COD_BUCKET && !blockEntity.hasCod()) {
                blockEntity.setHasCod(true);
                world.setBlock(pos, state.setValue(HAS_COD, true), 3);
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player) {
        super.playerWillDestroy(world, pos, state, player);
        FishTankBlockEntity blockEntity = (FishTankBlockEntity) world.getBlockEntity(pos);

        if (blockEntity != null) {
            if (blockEntity.hasCod()) {
                Cod cod = new Cod(EntityType.COD, world);
                cod.moveTo(pos, 0.0F, 0.0F);
                world.addFreshEntity(cod);
            }
            if (blockEntity.hasSalmon()) {
                Salmon salmon = new Salmon(EntityType.SALMON, world);
                salmon.moveTo(pos, 0.0F, 0.0F);
                world.addFreshEntity(salmon);
            }
            if (blockEntity.hasPufferfish()) {
                Pufferfish pufferfish = new Pufferfish(EntityType.PUFFERFISH, world);
                pufferfish.moveTo(pos, 0.0F, 0.0F);
                world.addFreshEntity(pufferfish);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }
}
