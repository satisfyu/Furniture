package com.berksire.furniture.registry;

import com.berksire.furniture.Furniture;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> FURNITURE_TABS = DeferredRegister.create(Furniture.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> FURNITURE_TAB = FURNITURE_TABS.register("furniture", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.BOAT_IN_A_JAR.get()))
            .title(Component.translatable("itemGroup.furniture.furniture_tab"))
            .displayItems((parameters, out) -> {
                String[] colorOrder = {
                        "white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown"
                };
                String[] woodTypeOrder = {
                        "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry"
                };
                for (String color : colorOrder) {
                    ObjectRegistry.SOFAS.get(color).ifPresent(out::accept);
                }
                for (String color : colorOrder) {
                    ObjectRegistry.POUFFE.get(color).ifPresent(out::accept);
                }
                for (String color : colorOrder) {
                    ObjectRegistry.LAMP_ITEMS.get(color).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.SHUTTERS.get(woodType).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.BENCHES.get(woodType).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.DESK_CHAIRS.get(woodType).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.CABINETS.get(woodType).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.DRESSER.get(woodType).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.DESKS.get(woodType).ifPresent(out::accept);
                }
                out.accept(ObjectRegistry.WOODEN_PLANTER.get());
                out.accept(ObjectRegistry.STONE_BRICK_PLANTER.get());
                out.accept(ObjectRegistry.STEAM_VENT.get());
                out.accept(ObjectRegistry.STONE_BRICKS_CHIMNEY.get());
                out.accept(ObjectRegistry.BRICK_CHIMNEY.get());
                out.accept(ObjectRegistry.COPPER_CHIMNEY.get());
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.CLOCKS.get(woodType).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.GRANDFATHER_CLOCKS.get(woodType).ifPresent(out::accept);
                }
                for (String woodType : woodTypeOrder) {
                    ObjectRegistry.MIRRORS.get(woodType).ifPresent(out::accept);
                }
                for (String color : colorOrder) {
                    ObjectRegistry.CURTAINS.get(color).ifPresent(out::accept);
                }
                out.accept(ObjectRegistry.TELESCOPE.get());
                out.accept(ObjectRegistry.GRAMOPHONE.get());
                out.accept(ObjectRegistry.CASH_REGISTER.get());
                out.accept(ObjectRegistry.COFFER.get());
                out.accept(ObjectRegistry.TOOL_BOX.get());
                out.accept(ObjectRegistry.SEWING_KIT.get());
                out.accept(ObjectRegistry.EXPLORERS_BOX.get());
                out.accept(ObjectRegistry.BLUEPRINTS.get());
                out.accept(ObjectRegistry.STREET_LANTERN_ITEM.get());
                out.accept(ObjectRegistry.PLATED_STREET_LANTERN_ITEM.get());
                out.accept(ObjectRegistry.PELLS.get());
                out.accept(ObjectRegistry.BOAT_IN_A_JAR.get());
                out.accept(ObjectRegistry.COPPER_FISH_TANK.get());
                out.accept(ObjectRegistry.IRON_FISH_TANK.get());
                out.accept(ObjectRegistry.TERRARIUM.get());
                out.accept(ObjectRegistry.DISPLAY.get());
                out.accept(ObjectRegistry.CANVAS.get());
                out.accept(ObjectRegistry.CPHS_PRIDE.get());
                out.accept(ObjectRegistry.LETSDO_THEME.get());
                out.accept(ObjectRegistry.BIN.get());
                out.accept(ObjectRegistry.TRASH_BAG.get());
            })
            .build());

    static {
        FURNITURE_TABS.register();
    }
}
