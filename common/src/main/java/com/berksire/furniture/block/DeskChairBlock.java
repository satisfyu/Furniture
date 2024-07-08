package com.berksire.furniture.block;

import com.berksire.furniture.block.entity.DeskChairBlockEntity;
import com.berksire.furniture.registry.ObjectRegistry;
import com.berksire.furniture.util.FurnitureUtil;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
public class DeskChairBlock extends FacingBlock implements EntityBlock {
    public DeskChairBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        InteractionResult result = FurnitureUtil.onUse(world, player, hand, hit, 0);
        if (result != InteractionResult.PASS) return result;

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof DeskChairBlockEntity deskChairBlockEntity)) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        Block blanketBlock = getBlanketBlock(stack);
        if (blanketBlock != null) {
            if (!deskChairBlockEntity.getDisplayedBlanket().isEmpty()) return InteractionResult.PASS;
            if (!world.isClientSide && deskChairBlockEntity.setDisplayedBlanket(new ItemStack(blanketBlock))) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                world.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                deskChairBlockEntity.setChanged();
                world.sendBlockUpdated(pos, state, state, 3);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        if (!deskChairBlockEntity.getDisplayedBlanket().isEmpty()) {
            if (!world.isClientSide) {
                ItemStack item = deskChairBlockEntity.getDisplayedBlanket().copy();
                item.setCount(1);
                if (!player.addItem(item)) {
                    ItemEntity itemEntity = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), item);
                    world.addFreshEntity(itemEntity);
                }
                deskChairBlockEntity.removeDisplayedBlanket(1);
                world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                deskChairBlockEntity.setChanged();
                world.sendBlockUpdated(pos, state, state, 3);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    private Block getBlanketBlock(ItemStack stack) {
        if (stack.is(Items.WHITE_WOOL)) return getBlanket("white");
        if (stack.is(Items.LIGHT_GRAY_WOOL)) return getBlanket("light_gray");
        if (stack.is(Items.GRAY_WOOL)) return getBlanket("gray");
        if (stack.is(Items.BLACK_WOOL)) return getBlanket("black");
        if (stack.is(Items.RED_WOOL)) return getBlanket("red");
        if (stack.is(Items.ORANGE_WOOL)) return getBlanket("orange");
        if (stack.is(Items.YELLOW_WOOL)) return getBlanket("yellow");
        if (stack.is(Items.LIME_WOOL)) return getBlanket("lime");
        if (stack.is(Items.GREEN_WOOL)) return getBlanket("green");
        if (stack.is(Items.CYAN_WOOL)) return getBlanket("cyan");
        if (stack.is(Items.LIGHT_BLUE_WOOL)) return getBlanket("light_blue");
        if (stack.is(Items.BLUE_WOOL)) return getBlanket("blue");
        if (stack.is(Items.PURPLE_WOOL)) return getBlanket("purple");
        if (stack.is(Items.MAGENTA_WOOL)) return getBlanket("magenta");
        if (stack.is(Items.PINK_WOOL)) return getBlanket("pink");
        if (stack.is(Items.BROWN_WOOL)) return getBlanket("brown");
        return null;
    }

    @Nullable
    private Block getBlanket(String color) {
        RegistrySupplier<Block> supplier = ObjectRegistry.DESK_CHAIR_BLANKET.get(color);
        return supplier != null ? supplier.get() : null;
    }


    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        FurnitureUtil.onStateReplaced(world, pos);
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof DeskChairBlockEntity deskChairBlockEntity) {
                deskChairBlockEntity.dropContents();
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DeskChairBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.25, 0.3125, 0.1875, 0.75, 0.4375, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.6875, 0.375, 0.6875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.625, 0, 0.6875, 0.75, 0.6875, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.6875, 0.6875, 0.75, 1, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.1875, 0.375, 0.3125, 0.3125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.625, 0, 0.1875, 0.75, 0.3125, 0.3125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.1875, 0.25, 0.6875, 0.3125, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.1875, 0.25, 0.6875, 0.3125, 0.75), BooleanOp.OR);
        return shape;
    };

    public static final Map<Direction, VoxelShape> SHAPE = net.minecraft.Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, FurnitureUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }
}
