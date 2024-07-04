package com.berksire.furniture.block.entity;

import com.berksire.furniture.client.entity.ActualFishTankEntity;
import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class FishTankBlockEntity extends BlockEntity {
    private boolean hasCod;
    private boolean hasPufferfish;
    private boolean hasSalmon;
    private int linkedRealTankBlock = -1;

    public FishTankBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.FISH_TANK_BLOCK_ENTITY.get(), pos, state);
    }

    public boolean hasCod() {
        return hasCod;
    }

    public void setHasCod(boolean hasCod) {
        this.hasCod = hasCod;
        setChanged();
        System.out.println("Set HasCod to " + hasCod);
    }

    public boolean hasPufferfish() {
        return hasPufferfish;
    }

    public void setHasPufferfish(boolean hasPufferfish) {
        this.hasPufferfish = hasPufferfish;
        setChanged();
        System.out.println("Set HasPufferfish to " + hasPufferfish);
    }

    public boolean hasSalmon() {
        return hasSalmon;
    }

    public void setHasSalmon(boolean hasSalmon) {
        this.hasSalmon = hasSalmon;
        setChanged();
        System.out.println("Set HasSalmon to " + hasSalmon);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        hasCod = tag.getBoolean("HasCod");
        hasPufferfish = tag.getBoolean("HasPufferfish");
        hasSalmon = tag.getBoolean("HasSalmon");
        linkedRealTankBlock = tag.getInt("LinkedRealTankBlock");
        System.out.println("Loaded HasCod: " + hasCod + ", HasPufferfish: " + hasPufferfish + ", HasSalmon: " + hasSalmon);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("HasCod", hasCod);
        tag.putBoolean("HasPufferfish", hasPufferfish);
        tag.putBoolean("HasSalmon", hasSalmon);
        tag.putInt("LinkedRealTankBlock", linkedRealTankBlock);
        System.out.println("Saved HasCod: " + hasCod + ", HasPufferfish: " + hasPufferfish + ", HasSalmon: " + hasSalmon);
    }

    public Optional<ActualFishTankEntity> getLinkedRealEntity() {
        if (this.getLevel().getEntity(linkedRealTankBlock) instanceof ActualFishTankEntity entity) {
            return Optional.of(entity);
        } else return Optional.empty();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FishTankBlockEntity blockEntity) {
        if (blockEntity.linkedRealTankBlock == -1) {
            level.getEntitiesOfClass(ActualFishTankEntity.class, new AABB(pos)).stream().findAny().ifPresentOrElse(
                    e -> blockEntity.linkedRealTankBlock = e.getId(), () -> {
                        ActualFishTankEntity fishTank = EntityTypeRegistry.ACTUAL_FISH_TANK.get().create(level);
                        fishTank.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                        level.addFreshEntity(fishTank);
                    }
            );
        }
    }

    @Override
    public void setRemoved() {
        this.getLinkedRealEntity().ifPresent(e -> {
            e.remove(Entity.RemovalReason.DISCARDED);
            this.linkedRealTankBlock = -1;
        });
        super.setRemoved();
    }
}
