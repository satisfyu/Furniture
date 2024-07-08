package com.berksire.furniture.block.entity;

import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DeskChairBlockEntity extends BlockEntity implements Clearable {
    private ItemStack displayedBlanket = ItemStack.EMPTY;

    public DeskChairBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.DESK_CHAIR_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public ItemStack getDisplayedBlanket() {
        return this.displayedBlanket;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.displayedBlanket = ItemStack.of(tag.getCompound("DisplayedBlanket"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("DisplayedBlanket", this.displayedBlanket.save(new CompoundTag()));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("DisplayedBlanket", this.displayedBlanket.save(new CompoundTag()));
        return compoundTag;
    }

    public boolean setDisplayedBlanket(ItemStack stack) {
        if (!this.displayedBlanket.isEmpty()) return false;

        this.displayedBlanket = stack;
        this.setChanged();
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
        return true;
    }

    public void removeDisplayedBlanket(int count) {
        if (!this.displayedBlanket.isEmpty()) {
            this.displayedBlanket.shrink(count);
            if (this.displayedBlanket.isEmpty()) {
                this.displayedBlanket = ItemStack.EMPTY;
            }
            this.setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    public void dropContents() {
        if (!this.displayedBlanket.isEmpty()) {
            assert this.level != null;
            ItemEntity itemEntity = new ItemEntity(this.level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), this.displayedBlanket);
            this.level.addFreshEntity(itemEntity);
            this.displayedBlanket = ItemStack.EMPTY;
        }
        this.markUpdated();
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.displayedBlanket = ItemStack.EMPTY;
    }
}
