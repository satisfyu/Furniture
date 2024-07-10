package com.berksire.furniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class PlanterBlock extends LineConnectingBlock {
    public PlanterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextFloat() < getGrowthChance(serverLevel, blockPos)) {
            applyBonemealEffect(serverLevel, blockPos, randomSource);
        }
    }

    private float getGrowthChance(ServerLevel serverLevel, BlockPos blockPos) {
        int lightLevel = serverLevel.getMaxLocalRawBrightness(blockPos.above());
        return lightLevel >= 10 ? 0.055f : 0.05f;
    }

    private void applyBonemealEffect(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        BlockPos posAbove = blockPos.above();
        BlockState stateAbove = serverLevel.getBlockState(posAbove);
        if (stateAbove.getBlock() instanceof BonemealableBlock bonemealableBlock && bonemealableBlock.isValidBonemealTarget(serverLevel, posAbove, stateAbove, false)) {
            bonemealableBlock.performBonemeal(serverLevel, randomSource, posAbove, stateAbove);
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, posAbove.getX() + 0.5, posAbove.getY() + 1.0, posAbove.getZ() + 0.5, 5, 0.5, 0.5, 0.5, 0.5);
        }
    }
}
