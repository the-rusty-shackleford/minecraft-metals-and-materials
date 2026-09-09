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

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * Steel for every mod in the pack: an ingot, a nugget and a block, registered
 * here and tagged the common way in this mod's data. Everything else about
 * steel -- what makes it, what it makes -- is data under
 * {@code data/metalsandmaterials} and the {@code c:} tags, so a recipe in any
 * other mod takes {@code #c:ingots/steel} and never names this mod.
 *
 * <p>The creative tab placement is the only behaviour in the class: the
 * ingot and nugget after iron's in Ingredients, the block after the iron
 * block in Building Blocks.
 */
@Mod(MetalsAndMaterials.MOD_ID)
public final class MetalsAndMaterials {
    public static final String MOD_ID = "metalsandmaterials";

    public MetalsAndMaterials(IEventBus modBus) {
        ModContent.register(modBus);
        modBus.addListener(MetalsAndMaterials::buildCreativeTabs);
    }

    private static void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.insertAfter(Items.IRON_NUGGET.getDefaultInstance(), ModContent.STEEL_NUGGET.toStack(),
                    net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(Items.IRON_INGOT.getDefaultInstance(), ModContent.STEEL_INGOT.toStack(),
                    net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.insertAfter(Items.IRON_BLOCK.getDefaultInstance(), ModContent.STEEL_BLOCK_ITEM.toStack(),
                    net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
