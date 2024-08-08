package com.berksire.furniture.block;

import com.berksire.furniture.block.entity.DisplayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class DisplayBlock extends FacingBlock implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.or(box(1, 0, 1, 15, 7, 15), box(2, 7, 2, 14, 16, 14));

    public DisplayBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof DisplayBlockEntity displayBlockEntity)) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty()) {
            if (!level.isClientSide && displayBlockEntity.setDisplayedItem(stack.copy())) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }

        if (!displayBlockEntity.getDisplayedItem().isEmpty()) {
            if (!level.isClientSide) {
                ItemStack item = displayBlockEntity.getDisplayedItem().copy();
                if (!player.addItem(item)) {
                    ItemEntity itemEntity = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), item);
                    level.addFreshEntity(itemEntity);
                }
                displayBlockEntity.removeDisplayedItem(1);
                level.playSound(null, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DisplayBlockEntity displayBlockEntity) {
                displayBlockEntity.dropContents();
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DisplayBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
