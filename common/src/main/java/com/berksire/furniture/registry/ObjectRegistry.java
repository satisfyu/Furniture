package com.berksire.furniture.registry;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.block.*;
import com.berksire.furniture.item.CanvasItem;
import com.berksire.furniture.item.PellsSpawnItem;
import com.berksire.furniture.item.TrashBagItem;
import com.berksire.furniture.util.FurnitureIdentifier;
import com.berksire.furniture.util.FurnitureUtil;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ObjectRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Furniture.MODID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Furniture.MODID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final Map<String, RegistrySupplier<Block>> SOFAS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> POUFFE = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> LAMPS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> WALL_LAMPS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Item>> LAMP_ITEMS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> CURTAINS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> CABINETS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> DESK_CHAIRS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> DESKS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> DRESSER = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> GRANDFATHER_CLOCKS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> CLOCKS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> BENCHES = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> MIRRORS = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> SHUTTERS = new HashMap<>();
    public static final RegistrySupplier<Block> GRAMOPHONE = registerWithItem("gramophone", () -> new GramophoneBlock(BlockBehaviour.Properties.copy(Blocks.JUKEBOX)));
    public static final RegistrySupplier<Block> TELESCOPE = registerWithItem("telescope", () -> new TelescopeBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> COFFER = registerWithItem("coffer", () -> new CofferBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.DESTROY)));
    public static final RegistrySupplier<Block> EXPLORERS_BOX = registerWithItem("explorers_box", () -> new ExplorersBoxBlock(BlockBehaviour.Properties.copy(Blocks.CARTOGRAPHY_TABLE)));
    public static final RegistrySupplier<Block> CASH_REGISTER = registerWithItem("cash_register", () -> new CashRegisterBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistrySupplier<Block> TOOL_BOX = registerWithItem("tool_box", () -> new ToolBoxBlock(BlockBehaviour.Properties.copy(Blocks.SMITHING_TABLE).pushReaction(PushReaction.DESTROY)));
    public static final RegistrySupplier<Block> BLUEPRINTS = registerWithItem("blueprints", () -> new BlueprintsBlock(BlockBehaviour.Properties.copy(Blocks.CAKE).instabreak()));
    public static final RegistrySupplier<Block> SEWING_KIT = registerWithItem("sewing_kit", () -> new SewingKitBlock(BlockBehaviour.Properties.copy(Blocks.LOOM)));
    public static final RegistrySupplier<Item> CANVAS = registerItem("canvas", () -> new CanvasItem(new Item.Properties(), CanvasRegistry.LONELY_DAISY, TagRegistry.PAINTINGS));
    public static final RegistrySupplier<Block> BIN = registerWithItem("bin", () -> new BinBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<Item> TRASH_BAG = registerItem("trash_bag", () -> new TrashBagItem(new Item.Properties()));
    public static final RegistrySupplier<Block> STEAM_VENT = registerWithItem("steam_vent", () -> new SteamVentBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<Block> COPPER_FISH_TANK = registerWithItem("copper_fish_tank", () -> new FishTankBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistrySupplier<Block> IRON_FISH_TANK = registerWithItem("iron_fish_tank", () -> new FishTankBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<Block> BRICK_CHIMNEY = registerWithItem("brick_chimney", () -> new ChimneyBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<Block> STONE_BRICKS_CHIMNEY = registerWithItem("stone_bricks_chimney", () -> new ChimneyBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistrySupplier<Block> COPPER_CHIMNEY = registerWithItem("copper_chimney", () -> new CopperChimneyBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_BLOCK)));
    public static final RegistrySupplier<Block> BOAT_IN_A_JAR = registerWithItem("boat_in_a_jar", () -> new BoatInAJarBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> STREET_LANTERN = registerWithoutItem("street_lantern", () -> new StreetLanternBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT).lightLevel(StreetLanternBlock::vanillaLightLevel)));
    public static final RegistrySupplier<Block> STREET_WALL_LANTERN = registerWithoutItem("street_lantern_wall", () -> new StreetLanternWallBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT).lightLevel(StreetLanternBlock::vanillaLightLevel)));
    public static final RegistrySupplier<Item> STREET_LANTERN_ITEM = registerItem("street_lantern_item", () -> new StandingAndWallBlockItem(ObjectRegistry.STREET_LANTERN.get(), ObjectRegistry.STREET_WALL_LANTERN.get(), new Item.Properties(), Direction.DOWN));
    public static final RegistrySupplier<Block> PLATED_STREET_LANTERN = registerWithoutItem("plated_street_lantern", () -> new StreetLanternBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT).lightLevel(StreetLanternBlock::vanillaLightLevel)));
    public static final RegistrySupplier<Block> PLATED_STREET_WALL_LANTERN = registerWithoutItem("plated_street_lantern_wall", () -> new StreetLanternWallBlock(BlockBehaviour.Properties.copy(Blocks.DECORATED_POT).lightLevel(StreetLanternBlock::vanillaLightLevel)));
    public static final RegistrySupplier<Item> PLATED_STREET_LANTERN_ITEM = registerItem("plated_street_lantern_item", () -> new StandingAndWallBlockItem(ObjectRegistry.PLATED_STREET_LANTERN.get(), ObjectRegistry.PLATED_STREET_WALL_LANTERN.get(), new Item.Properties(), Direction.DOWN));
    public static final RegistrySupplier<Item> PELLS = registerItem("pells", () -> new PellsSpawnItem(new Item.Properties()));
    public static final RegistrySupplier<Item> CPHS_PRIDE = registerItem("cphs_pride", () -> new RecordItem(1, SoundRegistry.CPHS_PRIDE.get(), getSettings().stacksTo(1), 191));
    public static final RegistrySupplier<Item> LETSDO_THEME = registerItem("letsdo_theme", () -> new RecordItem(1, SoundRegistry.LETSDO_THEME.get(), getSettings().stacksTo(1), 124));
    public static final RegistrySupplier<Block> DISPLAY = registerWithItem("display", () -> new DisplayBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> TERRARIUM = registerWithItem("terrarium", () -> new TerrariumBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> WOODEN_PLANTER = registerWithItem("wooden_planter", () -> new PlanterBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistrySupplier<Block> STONE_BRICK_PLANTER = registerWithItem("stone_brick_planter", () -> new PlanterBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS)));

    public static final String[] colors = {
            "white", "light_gray", "gray", "black", "red", "orange", "yellow", "lime", "green", "cyan", "light_blue", "blue", "purple", "magenta", "pink", "brown"
    };

    public static final String[] woodTypes = {
            "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry"
    };

    static {
        for (String woodType : woodTypes) {
            BENCHES.put(woodType, registerWithItem(woodType + "_bench", () -> new BenchBlock(BlockBehaviour.Properties.copy(getCorrespondingPlank(woodType)).pushReaction(PushReaction.IGNORE))));
            CABINETS.put(woodType, registerWithItem(woodType + "_cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD), SoundRegistry.CABINET_OPEN, SoundRegistry.CABINET_CLOSE)));
            CLOCKS.put(woodType, registerWithItem(woodType + "_clock", () -> new ClockBlock(BlockBehaviour.Properties.copy(getCorrespondingPlank(woodType)).pushReaction(PushReaction.IGNORE), ClockBlock.WoodType.valueOf(woodType.toUpperCase()))));
            GRANDFATHER_CLOCKS.put(woodType, registerWithItem(woodType + "_grandfather_clock", () -> new GrandfatherClockBlock(BlockBehaviour.Properties.copy(getCorrespondingPlank(woodType)).pushReaction(PushReaction.IGNORE), GrandfatherClockBlock.WoodType.valueOf(woodType.toUpperCase()))));
            MIRRORS.put(woodType, registerWithItem(woodType + "_mirror", () -> new MirrorBlock(BlockBehaviour.Properties.copy(getCorrespondingPlank(woodType)).pushReaction(PushReaction.IGNORE))));
            SHUTTERS.put(woodType, registerWithItem(woodType + "_shutter", () -> new ShutterBlock(BlockBehaviour.Properties.copy(getCorrespondingPlank(woodType)).pushReaction(PushReaction.IGNORE))));
            DESKS.put(woodType, registerWithItem(woodType + "_desk", () -> new DeskBlock(BlockBehaviour.Properties.copy(getCorrespondingPlank(woodType)).pushReaction(PushReaction.IGNORE))));
            DRESSER.put(woodType, registerWithItem(woodType + "_dresser", () -> new DresserBlock(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD), SoundRegistry.CABINET_OPEN, SoundRegistry.CABINET_CLOSE)));
            DESK_CHAIRS.put(woodType, registerWithItem(woodType + "_desk_chair", () -> new DeskChairBlock(BlockBehaviour.Properties.copy(getCorrespondingPlank(woodType)))));
        }
        for (String color : colors) {
            DyeColor dyeColor = DyeColor.valueOf(color.toUpperCase(Locale.ENGLISH));
            SOFAS.put(color, registerWithItem("sofa_" + color, () -> new SofaBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).pushReaction(PushReaction.DESTROY), dyeColor)));
            POUFFE.put(color, registerWithItem("pouffe_" + color, () -> new PouffeBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.NORMAL), dyeColor)));
            CURTAINS.put(color, registerWithItem("curtain_" + color, () -> new CurtainBlock(BlockBehaviour.Properties.copy(Blocks.RED_WOOL).pushReaction(PushReaction.DESTROY), dyeColor)));

            String lampName = "lamp_" + color;
            String wallLampName = "lamp_wall_" + color;

            RegistrySupplier<Block> lamp = registerWithoutItem(lampName, () -> new LampBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .lightLevel(state -> state.getValue(LampBlock.LIT) ? 15 : 0).pushReaction(PushReaction.DESTROY), dyeColor));
            LAMPS.put(color, lamp);

            RegistrySupplier<Block> wallLamp = registerWithoutItem(wallLampName, () -> new LampWallBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)
                    .lightLevel(state -> state.getValue(LampBlock.LIT) ? 15 : 0).pushReaction(PushReaction.DESTROY), dyeColor));
            WALL_LAMPS.put(color, wallLamp);

            LAMP_ITEMS.put(color, registerItem(lampName, () -> new StandingAndWallBlockItem(lamp.get(), wallLamp.get(), new Item.Properties(), Direction.DOWN)));
        }
        BLOCKS.register();
        ITEMS.register();
    }

    private static Block getCorrespondingPlank(String woodType) {
        return switch (woodType) {
            case "spruce" -> Blocks.SPRUCE_PLANKS;
            case "birch" -> Blocks.BIRCH_PLANKS;
            case "jungle" -> Blocks.JUNGLE_PLANKS;
            case "acacia" -> Blocks.ACACIA_PLANKS;
            case "dark_oak" -> Blocks.DARK_OAK_PLANKS;
            case "mangrove" -> Blocks.MANGROVE_PLANKS;
            case "cherry" -> Blocks.CHERRY_PLANKS;
            default -> Blocks.OAK_PLANKS;
        };
    }

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(settings -> {
        });
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return FurnitureUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, new FurnitureIdentifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return FurnitureUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, new FurnitureIdentifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return FurnitureUtil.registerItem(ITEMS, ITEM_REGISTRAR, new FurnitureIdentifier(path), itemSupplier);
    }
}
