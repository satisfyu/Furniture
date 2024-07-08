package com.berksire.furniture.registry;

import com.berksire.furniture.Furniture;
import com.berksire.furniture.block.entity.*;
import com.berksire.furniture.client.entity.FakeFishTankEntity;
import com.berksire.furniture.client.entity.CanvasEntity;
import com.berksire.furniture.client.entity.SeatEntity;
import com.berksire.furniture.client.entity.PellsEntity;
import com.berksire.furniture.util.FurnitureIdentifier;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static com.berksire.furniture.registry.ObjectRegistry.*;

public final class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Furniture.MODID, Registries.ENTITY_TYPE);
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Furniture.MODID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();

    public static final RegistrySupplier<BlockEntityType<GrandfatherClockBlockEntity>> GRANDFATHER_CLOCK_BLOCK_ENTITY = registerBlockEntity("grandfather_clock", () -> BlockEntityType.Builder.of(GrandfatherClockBlockEntity::new, GRANDFATHER_CLOCKS.get("oak").get(), GRANDFATHER_CLOCKS.get("birch").get(), GRANDFATHER_CLOCKS.get("acacia").get(), GRANDFATHER_CLOCKS.get("cherry").get(), GRANDFATHER_CLOCKS.get("dark_oak").get(), GRANDFATHER_CLOCKS.get("jungle").get(), GRANDFATHER_CLOCKS.get("mangrove").get(), GRANDFATHER_CLOCKS.get("spruce").get()).build(null));
    public static final RegistrySupplier<BlockEntityType<ClockBlockEntity>> CLOCK_BLOCK_ENTITY = registerBlockEntity("clock", () -> BlockEntityType.Builder.of(ClockBlockEntity::new, CLOCKS.get("oak").get(), CLOCKS.get("birch").get(), CLOCKS.get("acacia").get(), CLOCKS.get("cherry").get(), CLOCKS.get("dark_oak").get(), CLOCKS.get("jungle").get(), CLOCKS.get("mangrove").get(), CLOCKS.get("spruce").get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CofferBlockEntity>> COFFER_BLOCK_ENTITY = registerBlockEntity("coffer", () -> BlockEntityType.Builder.of(CofferBlockEntity::new, COFFER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of(CabinetBlockEntity::new, CABINETS.get("oak").get(), CABINETS.get("birch").get(), CABINETS.get("acacia").get(), CABINETS.get("cherry").get(), CABINETS.get("dark_oak").get(), CABINETS.get("jungle").get(), CABINETS.get("mangrove").get(), CABINETS.get("spruce").get()).build(null));
    public static final RegistrySupplier<BlockEntityType<FishTankBlockEntity>> FISH_TANK_BLOCK_ENTITY = registerBlockEntity("fish_tank", () -> BlockEntityType.Builder.of(FishTankBlockEntity::new, COPPER_FISH_TANK.get(), IRON_FISH_TANK.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<GramophoneBlockEntity>> GRAMOPHONE_BLOCK_ENTITY = registerBlockEntity("gramophone", () -> BlockEntityType.Builder.of(GramophoneBlockEntity::new, GRAMOPHONE.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<ChimneyBlockEntity>> CHIMNEY_BLOCK_ENTITY = registerBlockEntity("chimney", () -> BlockEntityType.Builder.of(ChimneyBlockEntity::new, COPPER_CHIMNEY.get(), STONE_BRICKS_CHIMNEY.get(), BRICK_CHIMNEY.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<DresserBlockEntity>> DRESSER_BLOCK_ENTITY = registerBlockEntity("dresser", () -> BlockEntityType.Builder.of(DresserBlockEntity::new, DRESSER.get("oak").get(), DRESSER.get("birch").get(), DRESSER.get("acacia").get(), DRESSER.get("cherry").get(), DRESSER.get("dark_oak").get(), DRESSER.get("jungle").get(), DRESSER.get("mangrove").get(), DRESSER.get("spruce").get()).build(null));
    public static final RegistrySupplier<BlockEntityType<DisplayBlockEntity>> DISPLAY_BLOCK_ENTITY = registerBlockEntity("display", () -> BlockEntityType.Builder.of(DisplayBlockEntity::new, DISPLAY.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<DeskChairBlockEntity>> DESK_CHAIR_BLOCK_ENTITY = registerBlockEntity("desk_chair", () -> BlockEntityType.Builder.of(DeskChairBlockEntity::new, DESK_CHAIRS.get("oak").get(), DESK_CHAIRS.get("birch").get(), DESK_CHAIRS.get("acacia").get(), DESK_CHAIRS.get("cherry").get(), DESK_CHAIRS.get("dark_oak").get(), DESK_CHAIRS.get("jungle").get(), DESK_CHAIRS.get("mangrove").get(), DESK_CHAIRS.get("spruce").get()).build(null));

    public static final RegistrySupplier<EntityType<SeatEntity>> CHAIR = registerEntity("chair", () -> EntityType.Builder.of(SeatEntity::new, MobCategory.MISC).sized(0.001F, 0.001F).build(new FurnitureIdentifier("chair").toString()));
    public static final RegistrySupplier<EntityType<CanvasEntity>> CANVAS = registerEntity("canvas", () -> EntityType.Builder.<CanvasEntity>of(CanvasEntity::new, MobCategory.MISC).sized(1.0F, 2.0F).build(new FurnitureIdentifier("canvas").toString()));
    public static final RegistrySupplier<EntityType<FakeFishTankEntity>> FAKE_FISH_TANK = registerEntity("fake_fish_tank", () -> EntityType.Builder.of(FakeFishTankEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(1000).build(new FurnitureIdentifier("fake_fish_tank").toString()));
    public static final RegistrySupplier<EntityType<PellsEntity>> PELLS = registerEntity("pells", () -> EntityType.Builder.of(PellsEntity::new, MobCategory.MISC).sized(1.0F, 2.0F).build(new FurnitureIdentifier("pells").toString()));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(new FurnitureIdentifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntity(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(path, type);
    }

    public static void registerAttributes() {
        EntityAttributeRegistry.register(PELLS, PellsEntity::createMobAttributes);
    }

    static {
        ENTITY_TYPES.register();
        registerAttributes();
    }
}