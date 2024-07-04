package com.berksire.furniture.block.entity;

import com.berksire.furniture.block.CofferBlock;
import com.berksire.furniture.registry.EntityTypeRegistry;
import com.berksire.furniture.registry.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CofferBlockEntity extends RandomizableContainerBlockEntity implements LidBlockEntity {
    private NonNullList<ItemStack> items;
    private final ContainerOpenersCounter openersCounter;
    private final ChestLidController chestLidController = new ChestLidController();

    public CofferBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.COFFER_BLOCK_ENTITY.get(), blockPos, blockState);
        this.items = NonNullList.withSize(36, ItemStack.EMPTY);
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
                CofferBlockEntity.this.playOpenSound(blockState);
            }

            protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
                CofferBlockEntity.this.playCloseSound(blockState);
            }

            protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
                CofferBlockEntity.this.signalOpenCount(level, blockPos, blockState, i, j);
            }

            protected boolean isOwnContainer(Player player) {
                if (player.containerMenu instanceof ChestMenu) {
                    Container container = ((ChestMenu)player.containerMenu).getContainer();
                    return container == CofferBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!this.trySaveLootTable(compoundTag)) {
            ContainerHelper.saveAllItems(compoundTag, this.items);
        }
    }

    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compoundTag)) {
            ContainerHelper.loadAllItems(compoundTag, this.items);
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new ChestMenu(MenuType.GENERIC_9x4, syncId, playerInventory, this, 4);
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items = nonNullList;
    }

    @Override
    public int getContainerSize() {
        return 36;
    }

    public void startOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.incrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player player) {
        if (!this.remove && !player.isSpectator()) {
            this.openersCounter.decrementOpeners(player, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    void playOpenSound(BlockState blockState) {
        Vec3i vec3i = blockState.getValue(CofferBlock.FACING).getNormal();
        double d = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
        double e = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
        double f = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        assert this.level != null;
        this.level.playSound(null, d, e, f, SoundRegistry.COFFER_OPEN.get(), SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat());
    }

    void playCloseSound(BlockState blockState) {
        Vec3i vec3i = blockState.getValue(CofferBlock.FACING).getNormal();
        double d = (double)this.worldPosition.getX() + 0.5D + (double)vec3i.getX() / 2.0D;
        double e = (double)this.worldPosition.getY() + 0.5D + (double)vec3i.getY() / 2.0D;
        double f = (double)this.worldPosition.getZ() + 0.5D + (double)vec3i.getZ() / 2.0D;
        assert this.level != null;
        this.level.playSound(null, d, e, f, SoundRegistry.COFFER_CLOSE.get(), SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat());
    }

    @Override
    public float getOpenNess(float f) {
        return this.chestLidController.getOpenness(f);
    }

    public static void lidAnimateTick(Level level, BlockPos blockPos, BlockState blockState, CofferBlockEntity cofferBlockEntity) {
        cofferBlockEntity.chestLidController.tickLid();
    }

    public boolean triggerEvent(int i, int j) {
        if (i == 1) {
            this.chestLidController.shouldBeOpen(j > 0);
            return true;
        } else {
            return super.triggerEvent(i, j);
        }
    }

    protected void signalOpenCount(Level level, BlockPos blockPos, BlockState blockState, int i, int j) {
        Block block = blockState.getBlock();
        level.blockEvent(blockPos, block, 1, j);
    }
}
