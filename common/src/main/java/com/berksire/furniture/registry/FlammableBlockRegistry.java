package com.berksire.furniture.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class FlammableBlockRegistry {
    static {
        ObjectRegistry.CLOCKS.forEach((woodType, clock) -> addFlammable(5, 20, clock.get()));
        ObjectRegistry.GRANDFATHER_CLOCKS.forEach((woodType, clock) -> addFlammable(5, 20, clock.get()));
        ObjectRegistry.BENCHES.forEach((woodType, bench) -> addFlammable(5, 20, bench.get()));
        ObjectRegistry.MIRRORS.forEach((woodType, mirror) -> addFlammable(5, 20, mirror.get()));

        addFlammableSofas(5, 5);
        addFlammable(30, 60);
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
    }
}

