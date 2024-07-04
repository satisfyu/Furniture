package com.berksire.furniture.client.entity;

import com.berksire.furniture.registry.ObjectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

public class PellsEntity extends Mob {
    private static final EntityDataAccessor<Float> LAST_DAMAGE = SynchedEntityData.defineId(PellsEntity.class, EntityDataSerializers.FLOAT);
    private static final Predicate<Entity> RIDABLE_MINECARTS = (p_31582_) -> p_31582_ instanceof AbstractMinecart;
    private static final EntityDataAccessor<Boolean> IS_CRIT = SynchedEntityData.defineId(PellsEntity.class, EntityDataSerializers.BOOLEAN);

    public float lastDamageOffset = 0;
    public float lastDamageOffsetPrev = 0;

    public PellsEntity(EntityType<? extends Mob> type, Level worldIn) {
        super(type, worldIn);
        this.setMaxUpStep(0.0F);
    }

    public static @NotNull AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(LAST_DAMAGE, 0f);
        entityData.define(IS_CRIT, false);
    }

    public void tick() {
        super.tick();
        if (this.isInWall()) {
            this.spawnAtLocation(ObjectRegistry.PELLS.get());
            this.discard();
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player pPlayer, @NotNull InteractionHand pHand) {
        Level level = pPlayer.level();
        if (pPlayer.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                if (!pPlayer.isCreative()) {
                    level.addFreshEntity(new ItemEntity(level, this.getX(), this.getY() + 0.7D, this.getZ(), ObjectRegistry.PELLS.get().getDefaultInstance()));
                }
                this.removeAfterChangingDimensions();
            } else {
                for (int i = 0; i < 4; i++) {
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.HAY_BLOCK.defaultBlockState()), this.getX(), this.getY() + 0.7D, this.getZ(), 0d, 0.02d, 0d);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public void kill() {
        this.actuallyHurt(this.level().damageSources().genericKill(), Float.MAX_VALUE);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source == this.damageSources().fellOutOfWorld()) {
            this.remove(RemovalReason.KILLED);
            return false;
        }
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            kill();
            return false;
        } else {
            BlockState state = Blocks.HAY_BLOCK.defaultBlockState();
            for (int i = 0; i < 3; i++) {
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), this.getX(), this.getY() + 0.7D, this.getZ(), 0d, 0.02d, 0d);
            }
            entityData.set(LAST_DAMAGE, 0f);
            if (hurtTime == 0) {
                entityData.set(LAST_DAMAGE, amount);
                boolean isCrit = source.getEntity() instanceof Player && ((Player) source.getEntity()).getAttackStrengthScale(0.5f) > 0.9f && source.is(DamageTypeTags.BYPASSES_RESISTANCE);
                entityData.set(IS_CRIT, isCrit);
            }
            this.markHurt();
            this.level().broadcastDamageEvent(this, source);
            return true;
        }
    }

    public boolean isCrit() {
        return entityData.get(IS_CRIT);
    }

    public float getLastDamage() {
        return entityData.get(LAST_DAMAGE);
    }

    public boolean isPushable() {
        return false;
    }

    protected void doPush(@NotNull Entity pEntity) {
    }

    public boolean canBeLeashed(@NotNull Player pPlayer) {
        return false;
    }

    protected void pushEntities() {
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox(), RIDABLE_MINECARTS);
        for (Entity entity : list) {
            if (this.distanceToSqr(entity) <= 0.2D) {
                entity.push(this);
            }
        }
    }

    public boolean shouldRenderAtSqrDistance(double pDistance) {
        double d0 = this.getBoundingBox().getSize() * 4.0D;
        if (Double.isNaN(d0) || d0 == 0.0D) {
            d0 = 4.0D;
        }
        d0 *= 64.0D;
        return pDistance < d0 * d0;
    }

    public boolean isIgnoringBlockTriggers() {
        return true;
    }

    public boolean isPickable() {
        return true;
    }

    public boolean isAffectedByPotions() {
        return false;
    }

    public boolean attackable() {
        return false;
    }

    public boolean canBeSeenAsEnemy() {
        return false;
    }

    public boolean canBeSeenByAnyone() {
        return false;
    }

    protected float tickHeadTurn(float pYRot, float pAnimStep) {
        this.yBodyRotO = this.yRotO;
        this.yBodyRot = this.getYRot();
        return 0.0F;
    }

    public @NotNull PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public boolean skipAttackInteraction(@NotNull Entity pEntity) {
        return pEntity instanceof Player && !this.level().mayInteract((Player) pEntity, this.blockPosition());
    }

    public @NotNull Vec3 getLightProbePosition(float pPartialTicks) {
        return super.getLightProbePosition(pPartialTicks);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    @Override
    public void knockback(double strength, double x, double z) {
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}