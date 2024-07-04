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
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class ActualFishTankEntity extends Entity {

    public AnimationState idleAnimationState;
    private int idleAnimationTimeout = 0;

    public ActualFishTankEntity(EntityType<?> entityType, Level level) {
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
        Vec3 blockPos = this.position();
        compoundTag.putDouble("TankX", blockPos.x());
        compoundTag.putDouble("TankY", blockPos.y());
        compoundTag.putDouble("TankZ", blockPos.z());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        Vec3 blockPos = new Vec3(
                compoundTag.getDouble("TankX"),
                compoundTag.getDouble("TankY"),
                compoundTag.getDouble("TankZ")
        );
        this.setPos(blockPos);
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
