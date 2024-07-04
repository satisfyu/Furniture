package com.berksire.furniture.item;

import com.berksire.furniture.registry.TagRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class TrashBagItem extends Item {
    public TrashBagItem(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide || user.isCrouching()) {
            return super.use(world, user, hand);
        }

        ItemStack itemStack = user.getItemInHand(hand);

        Random random = new Random();
        List<Item> items = BuiltInRegistries.ITEM.stream()
                .filter(item -> {
                    String itemName = BuiltInRegistries.ITEM.getKey(item).getPath();
                    return !item.builtInRegistryHolder().is(TagRegistry.TRASH_BAG_BLACKLIST)
                            && !itemName.contains("spawn_egg")
                            && !itemName.contains("_head")
                            && !itemName.equals("dragon_egg")
                            && !itemName.equals("light")
                            && !itemName.contains("command_block");
                })
                .toList();

        Item randomItem = items.get(random.nextInt(items.size()));
        ItemStack spawnedItem = new ItemStack(randomItem);

        world.addFreshEntity(new ItemEntity(world, user.getX(), user.getY(), user.getZ(), spawnedItem));

        if (!user.isCreative()) {
            itemStack.shrink(1);
        }

        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BUNDLE_INSERT, SoundSource.PLAYERS, 1.0F, 1.0F);
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.furniture.trash_bag").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC));
    }

}
