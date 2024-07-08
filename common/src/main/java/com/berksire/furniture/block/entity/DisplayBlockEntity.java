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

public class DisplayBlockEntity extends BlockEntity implements Clearable {
    private ItemStack displayedItem = ItemStack.EMPTY;

    public DisplayBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.DISPLAY_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public ItemStack getDisplayedItem() {
        return this.displayedItem;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.displayedItem = ItemStack.of(tag.getCompound("DisplayedItem"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("DisplayedItem", this.displayedItem.save(new CompoundTag()));
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("DisplayedItem", this.displayedItem.save(new CompoundTag()));
        return compoundTag;
    }

    public boolean setDisplayedItem(ItemStack stack) {
        if (!this.displayedItem.isEmpty()) return false;

        this.displayedItem = stack;
        this.markUpdated();
        return true;
    }

    public void removeDisplayedItem(int count) {
        if (!this.displayedItem.isEmpty()) {
            this.displayedItem.shrink(count);
            if (this.displayedItem.isEmpty()) {
                this.displayedItem = ItemStack.EMPTY;
            }
            this.markUpdated();
        }
    }

    public void dropContents() {
        if (!this.displayedItem.isEmpty()) {
            assert this.level != null;
            ItemEntity itemEntity = new ItemEntity(this.level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), this.displayedItem);
            this.level.addFreshEntity(itemEntity);
            this.displayedItem = ItemStack.EMPTY;
        }
        this.markUpdated();
    }

    private void markUpdated() {
        this.setChanged();
        Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public void clearContent() {
        this.displayedItem = ItemStack.EMPTY;
    }
}