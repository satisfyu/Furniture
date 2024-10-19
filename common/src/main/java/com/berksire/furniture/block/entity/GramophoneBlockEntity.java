package com.berksire.furniture.block.entity;

import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

public class GramophoneBlockEntity extends BlockEntity implements Clearable {
    private ItemStack recordItem = ItemStack.EMPTY;
    private long tickCount;
    private long recordStartedTick;
    private boolean isPlaying;
    private boolean repeat;

    public GramophoneBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(EntityTypeRegistry.GRAMOPHONE_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("RecordItem")) {
            this.recordItem = ItemStack.of(tag.getCompound("RecordItem"));
        }
        this.isPlaying = tag.getBoolean("IsPlaying");
        this.recordStartedTick = tag.getLong("RecordStartTick");
        this.tickCount = tag.getLong("TickCount");
        this.repeat = tag.getBoolean("Repeat");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!this.recordItem.isEmpty()) {
            tag.put("RecordItem", this.recordItem.save(new CompoundTag()));
        }
        tag.putBoolean("IsPlaying", this.isPlaying);
        tag.putLong("RecordStartTick", this.recordStartedTick);
        tag.putLong("TickCount", this.tickCount);
        tag.putBoolean("Repeat", this.repeat);
    }

    public boolean isRecordPlaying() {
        return !this.recordItem.isEmpty() && this.isPlaying && this.getBlockState().getValue(JukeboxBlock.HAS_RECORD);
    }

    private void startPlaying() {
        this.recordStartedTick = this.tickCount;
        this.isPlaying = true;
        assert this.level != null;
        this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
        this.level.levelEvent(null, 1010, this.worldPosition, Item.getId(this.recordItem.getItem()));
        this.setChanged();
    }

    public static void playRecordTick(Level level, BlockPos pos, BlockState state, GramophoneBlockEntity blockEntity) {
        if (level.getBlockEntity(pos) instanceof GramophoneBlockEntity && blockEntity.isRecordPlaying()) {
            blockEntity.tick(level, pos, state);
        } else {
            blockEntity.stopPlaying();
        }
    }


    @SuppressWarnings("all")
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (this.isRecordPlaying()) {
            ItemStack item = this.recordItem;
            if (item.getItem() instanceof RecordItem recordItem) {
                if (this.tickCount >= this.recordStartedTick + (long) recordItem.getLengthInTicks() + 20L) {
                    if (this.repeat) {
                        this.startPlaying();
                    } else {
                        this.stopPlaying();
                    }
                } else if (this.tickCount % 20 == 0) {
                    level.gameEvent(GameEvent.JUKEBOX_PLAY, pos, GameEvent.Context.of(state));
                    this.spawnMusicParticles(level, pos);

                }
            }
        }
        this.tickCount++;
    }

    private void spawnMusicParticles(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 vec3 = Vec3.atBottomCenterOf(pos).add(-0.25, 2, -0.25);
            float f = (float) level.getRandom().nextInt(4) / 24.0F;
            serverLevel.sendParticles(ParticleTypes.NOTE, vec3.x(), vec3.y(), vec3.z(), 0, f, 0.0, 0.0, 1.0);
        }
    }

    public void popOutRecord() {
        if (this.level != null && !this.level.isClientSide) {
            this.stopPlaying();
            BlockPos blockPos = this.getBlockPos();
            ItemStack itemStack = this.recordItem;
            if (!itemStack.isEmpty()) {
                this.recordItem = ItemStack.EMPTY;
                ItemStack singleDisk = itemStack.copy();
                singleDisk.setCount(1);
                Vec3 vec3 = Vec3.atLowerCornerWithOffset(blockPos, 0.5, 1.01, 0.5).offsetRandom(this.level.random, 0.7F);
                ItemEntity itemEntity = new ItemEntity(this.level, vec3.x(), vec3.y(), vec3.z(), singleDisk);
                itemEntity.setDefaultPickUpDelay();
                this.level.addFreshEntity(itemEntity);
                this.setChanged();
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
                if (this.level.getBlockState(this.worldPosition).is(Blocks.AIR)) {
                    return;
                }
                BlockState state = this.level.getBlockState(this.worldPosition);
                this.level.setBlock(this.worldPosition, state.setValue(JukeboxBlock.HAS_RECORD, false), 3);
                this.level.updateNeighborsAt(this.worldPosition, state.getBlock());
            }
        }
    }

    @Override
    public void setRemoved() {
        this.stopPlayingOnRemove();
        super.setRemoved();
    }

    public void setRecord(ItemStack record) {
        if (!record.isEmpty()) {
            this.recordItem = record.copy();
            this.recordItem.setCount(1);
            this.setHasRecordBlockState(true);
            this.startPlaying();
            this.setChanged();
        }
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
        this.setChanged();
    }

    public boolean isRepeat() {
        return this.repeat;
    }

    private void setHasRecordBlockState(boolean hasRecord) {
        if (this.level != null && !this.level.getBlockState(this.worldPosition).is(Blocks.AIR)) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            this.level.setBlock(this.worldPosition, state.setValue(JukeboxBlock.HAS_RECORD, hasRecord), 3);
            this.level.updateNeighborsAt(this.worldPosition, state.getBlock());
        }
    }

    public void stopPlaying() {
        if (this.isPlaying) {
            this.isPlaying = false;
            assert this.level != null;
            
            this.level.gameEvent(GameEvent.JUKEBOX_STOP_PLAY, this.worldPosition, GameEvent.Context.of(this.getBlockState()));
            this.level.levelEvent(1011, this.worldPosition, 0);

            this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());

            this.setChanged();
        }
    }

    public void stopPlayingOnRemove() {
        if (this.isPlaying) {
            this.stopPlaying();
        }


    }

    @Override
    public void clearContent() {
        this.recordItem = ItemStack.EMPTY;
        this.setHasRecordBlockState(false);
        this.stopPlaying();
    }

    public ItemStack getFirstItem() {
        return this.recordItem;
    }
}
