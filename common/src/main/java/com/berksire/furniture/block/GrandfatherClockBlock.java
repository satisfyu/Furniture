package com.berksire.furniture.block;

import com.berksire.furniture.block.entity.GrandfatherClockBlockEntity;
import com.berksire.furniture.registry.SoundRegistry;
import com.berksire.furniture.util.FurnitureUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class GrandfatherClockBlock extends FacingBlock implements EntityBlock {
    public enum WoodType {
        OAK,
        SPRUCE,
        BIRCH,
        JUNGLE,
        ACACIA,
        DARK_OAK,
        MANGROVE,
        CHERRY
    }

    private final WoodType woodType;

    public GrandfatherClockBlock(BlockBehaviour.Properties settings, WoodType woodType) {
        super(settings);
        this.woodType = woodType;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GrandfatherClockBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.21875, 0, 0.25, 0.34375, 0.125, 0.375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 0.125, 0.25, 0.78125, 0.5625, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 0.5625, 0.25, 0.78125, 1.3125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.15625, 1.3125, 0.1875, 0.84375, 1.375, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 1.375, 0.25, 0.78125, 1.8125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 1.8125, 0.25, 0.78125, 2, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.46875, 1.625, 0.21875, 0.53125, 1.6875, 0.28125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.65625, 0, 0.25, 0.78125, 0.125, 0.375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.65625, 0, 0.625, 0.78125, 0.125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.21875, 0, 0.625, 0.34375, 0.125, 0.75), BooleanOp.OR);
        return shape;
    };

    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.getDayTime() % 24000 == 0) {
            world.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 1.0F, 1.0F);
        } else {
            world.playSound(null, pos, SoundRegistry.GRANDFATHERS_CLOCK_TICKING.get(), SoundSource.BLOCKS, 0.15F, 1.0F);
        }
        world.scheduleTick(pos, this, 20);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isClientSide) {
            world.scheduleTick(pos, this, 20);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!world.isClientSide && state.getBlock() != newState.getBlock()) {
            world.getBlockTicks().schedule(new ScheduledTick<>(state.getBlock(), pos, world.getGameTime(), 0));
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
            long time = world.getDayTime();
            int hours = (int) ((time / 1000 + 6) % 24);
            int minutes = (int) (60 * (time % 1000) / 1000);

            player.displayClientMessage(Component.translatable("tooltip.furniture.clock", String.format(Locale.ENGLISH, "%02d:%02d", hours, minutes)), true);
        }
        return InteractionResult.SUCCESS;
    }
}
