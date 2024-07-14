package com.berksire.furniture.block;

import com.berksire.furniture.block.entity.ChimneyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.berksire.furniture.util.FurnitureUtil.SmokeType;

@SuppressWarnings("deprecation")
public class ChimneyBlock extends Block implements EntityBlock {
    public static final EnumProperty<SmokeType> SMOKE_TYPE = EnumProperty.create("smoke_type", SmokeType.class);
    public static final BooleanProperty SOOTY = BooleanProperty.create("sooty");
    private static final VoxelShape SHAPE = Shapes.or(Block.box(1, 0, 1, 15, 11, 15), Block.box(0, 11, 0, 16, 16, 16));

    public ChimneyBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(SMOKE_TYPE, SmokeType.NO_SMOKE).setValue(SOOTY, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SMOKE_TYPE, SOOTY);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(SMOKE_TYPE, SmokeType.SMOKE).setValue(SOOTY, false);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && hand == InteractionHand.MAIN_HAND) {
            if (player.getItemInHand(hand).getItem() == Items.BRUSH && state.getValue(SOOTY)) {
                world.setBlock(pos, state.setValue(SOOTY, false), 3);
                world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, Items.BLACK_DYE.getDefaultInstance()));
                for (int i = 0; i < 5; i++) {
                    world.addParticle(ParticleTypes.WAX_ON, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0, 0.1, 0.0);
                }
                return InteractionResult.SUCCESS;
            } else if (player.isShiftKeyDown()) {
                SmokeType currentType = state.getValue(SMOKE_TYPE);
                SmokeType newType = currentType.getNext();
                world.setBlock(pos, state.setValue(SMOKE_TYPE, newType), 3);
                player.displayClientMessage(Component.translatable("tooltip.furniture.smoke_type", Component.translatable(newType.getTranslationKey())), true);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        SmokeType smokeType = state.getValue(SMOKE_TYPE);
        boolean isSooty = state.getValue(SOOTY);

        if (smokeType == SmokeType.HEAVY_SMOKE) {
            for (int i = 0; i < 10; i++) {
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0, 0.1, 0.0);
                world.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0, 0.1, 0.0);
            }
        } else if (smokeType == SmokeType.SMOKE) {
            for (int i = 0; i < 5; i++) {
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0, 0.1, 0.0);
            }
        }

        if (isSooty) {
            for (int i = 0; i < 5; i++) {
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0.0, 0.1, 0.0);
            }
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChimneyBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <E extends BlockEntity> BlockEntityTicker<E> getTicker(Level level, BlockState state, BlockEntityType<E> blockEntityType) {
        return level.isClientSide ? null : (lvl, pos, blockState, t) -> {
            if (t instanceof ChimneyBlockEntity) {
                ChimneyBlockEntity.tick(lvl, pos, blockState);
            }
        };
    }
}