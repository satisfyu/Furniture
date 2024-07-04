package com.berksire.furniture.item;

import com.berksire.furniture.client.entity.PellsEntity;
import com.berksire.furniture.registry.EntityTypeRegistry;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.util.function.*;

public class PellsSpawnItem extends Item {
    public PellsSpawnItem(Item.Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Direction direction = pContext.getClickedFace();
        if (direction == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            Level level = pContext.getLevel();
            BlockPlaceContext blockplacecontext = new BlockPlaceContext(pContext);
            BlockPos blockpos = blockplacecontext.getClickedPos();
            ItemStack itemstack = pContext.getItemInHand();
            Vec3 vec3 = Vec3.atBottomCenterOf(blockpos);
            AABB aabb = EntityTypeRegistry.PELLS.get().getDimensions().makeBoundingBox(vec3.x(), vec3.y(), vec3.z());
            if (level.noCollision(null, aabb) && level.getEntities(null, aabb).isEmpty() && !level.getBlockState(blockpos.below()).isAir()) {
                if (level instanceof ServerLevel serverlevel) {
                    Consumer<PellsEntity> consumer = EntityType.createDefaultStackConfig(serverlevel, itemstack, pContext.getPlayer());
                    PellsEntity pells = EntityTypeRegistry.PELLS.get().create(serverlevel, itemstack.getTag(), consumer, blockpos, MobSpawnType.SPAWN_EGG, true, true);
                    if (pells == null) {
                        return InteractionResult.FAIL;
                    }

                    float f = (float) Mth.floor((Mth.wrapDegrees(pContext.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    pells.moveTo(pells.getX(), pells.getY(), pells.getZ(), f, 0.0F);
                    serverlevel.addFreshEntityWithPassengers(pells);
                    level.playSound(null, pells.getX(), pells.getY(), pells.getZ(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.35F, 0.9F);
                    pells.gameEvent(GameEvent.ENTITY_PLACE, pContext.getPlayer());
                }

                itemstack.shrink(1);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}