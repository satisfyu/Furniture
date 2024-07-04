package com.berksire.furniture.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class FlammableBlockRegistry {

    public static void registerFlammables() {
        ObjectRegistry.CLOCKS.forEach((woodType, clock) -> addFlammable(5, 20, clock.get()));
        ObjectRegistry.GRANDFATHER_CLOCKS.forEach((woodType, clock) -> addFlammable(5, 20, clock.get()));
        ObjectRegistry.BENCHES.forEach((woodType, bench) -> addFlammable(5, 20, bench.get()));
        ObjectRegistry.MIRRORS.forEach((woodType, mirror) -> addFlammable(5, 20, mirror.get()));

        addFlammableSofas(5, 5);
        addFlammable(30, 60,
                ObjectRegistry.GRAMOPHONE.get(),
                ObjectRegistry.TELESCOPE.get(),
                ObjectRegistry.COFFER.get(),
                ObjectRegistry.EXPLORERS_BOX.get(),
                ObjectRegistry.BLUEPRINTS.get(),
                ObjectRegistry.SEWING_KIT.get(),
                ObjectRegistry.BIN.get(),
                ObjectRegistry.BOAT_IN_A_JAR.get()
        );
    }

    public static void addFlammable(int burnOdd, int igniteOdd, Block... blocks) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        for (Block block : blocks) {
            fireBlock.setFlammable(block, burnOdd, igniteOdd);
        }
    }

    public static void addFlammableSofas(int burnOdd, int igniteOdd) {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        ObjectRegistry.SOFAS.forEach((color, sofa) -> fireBlock.setFlammable(sofa.get(), burnOdd, igniteOdd));
        ObjectRegistry.POUFFE.forEach((color, pouffe) -> fireBlock.setFlammable(pouffe.get(), burnOdd, igniteOdd));
        ObjectRegistry.LAMPS.forEach((color, lamp) -> fireBlock.setFlammable(lamp.get(), burnOdd, igniteOdd));
        ObjectRegistry.CURTAINS.forEach((color, curtain) -> fireBlock.setFlammable(curtain.get(), burnOdd, igniteOdd));
        ObjectRegistry.CABINETS.forEach((color, cabinet) -> fireBlock.setFlammable(cabinet.get(), burnOdd, igniteOdd));
    }
}
