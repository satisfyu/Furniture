package com.berksire.furniture.block;

import com.berksire.furniture.registry.ObjectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BinBlock extends Block {
    public static final IntegerProperty FILL_STAGE = IntegerProperty.create("fill_stage", 0, 11);
    private static final VoxelShape SHAPE = Shapes.box(2.0 / 16.0, 0.0, 2.0 / 16.0, 14.0 / 16.0, 15.0 / 16.0, 14.0 / 16.0);
    private static final Random RANDOM = new Random();
    private static final double BAG_DROP_PROBABILITY = 0.25;

    public BinBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FILL_STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILL_STAGE);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.isEmpty()) {
            int currentFillStage = state.getValue(FILL_STAGE);

            if (currentFillStage < 11) {
                world.setBlock(pos, state.setValue(FILL_STAGE, currentFillStage + 1), 3);
                world.playSound(null, pos, SoundEvents.COMPOSTER_FILL_SUCCESS, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (currentFillStage >= 9) {
                    spawnParticles(world, pos);
                }
            } else {
                world.playSound(null, pos, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (RANDOM.nextDouble() < BAG_DROP_PROBABILITY) {
                    dropItem(world, pos);
                }
                world.setBlock(pos, state.setValue(FILL_STAGE, 0), 3);
            }
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void dropItem(Level world, BlockPos pos) {
        ItemStack droppedItem = new ItemStack(ObjectRegistry.TRASH_BAG.get());
        Block.popResource(world, pos, droppedItem);
    }

    private void spawnParticles(Level world, BlockPos pos) {
        for (int i = 0; i < 20; i++) {
            double offsetX = RANDOM.nextDouble() * 0.6 - 0.3;
            double offsetY = RANDOM.nextDouble() * 0.6 - 0.3;
            double offsetZ = RANDOM.nextDouble() * 0.6 - 0.3;
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5 + offsetX, pos.getY() + 1.0 + offsetY, pos.getZ() + 0.5 + offsetZ, 0.0, 1, 0.0);
        }
    }
}
