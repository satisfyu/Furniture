package com.berksire.furniture.client.entity;

import com.berksire.furniture.block.entity.FishTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class FakeFishTankEntity extends Entity {

    public AnimationState idleAnimationState;
    private int idleAnimationTimeout = 0;

    public FakeFishTankEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        idleAnimationState = new AnimationState();
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean isColliding(BlockPos blockPos, BlockState blockState) {
        return false;
    }

    public Optional<FishTankBlockEntity> getNearestTankEntity() {
        if (this.level().getBlockEntity(this.blockPosition()) instanceof FishTankBlockEntity be) {
            return Optional.of(be);
        } else return Optional.empty();
    }

    @Override
    public void handleDamageEvent(DamageSource damageSource) {
    }

    public void updateAnimations() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 123;
            this.idleAnimationState.start(0);
        } else {
            this.idleAnimationTimeout--;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.updateAnimations();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putLong("TankPos", this.blockPosition().asLong());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        long posLong = compoundTag.getLong("TankPos");
        BlockPos pos = BlockPos.of(posLong);
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    public boolean shouldBeSaved() {
        return true;
    }
}
