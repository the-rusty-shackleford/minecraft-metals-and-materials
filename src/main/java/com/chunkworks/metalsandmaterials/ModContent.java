/*
 * Metals and Materials - the pack's one steel.
 * Copyright (C) 2026 Rusty Shackleford and nfx
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.chunkworks.metalsandmaterials;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * The three things this mod adds to the game: a steel ingot, a steel nugget
 * and a block of steel. The block copies the iron block's properties (its
 * hardness, its sound, its need for a stone pickaxe is in the block tags).
 */
public final class ModContent {
    private ModContent() {}

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MetalsAndMaterials.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MetalsAndMaterials.MOD_ID);

    /** Iron with a little carbon: what receivers, engines and chassis are made of. */
    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.registerSimpleItem("steel_ingot");
    /** A ninth of an ingot. */
    public static final DeferredItem<Item> STEEL_NUGGET = ITEMS.registerSimpleItem("steel_nugget");
    /** Nine ingots as a block. */
    public static final DeferredBlock<Block> STEEL_BLOCK = BLOCKS.registerSimpleBlock("steel_block",
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
    public static final DeferredItem<BlockItem> STEEL_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(STEEL_BLOCK);

    /** effects: registers the blocks and items on {@code modBus} */
    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
    }
}
