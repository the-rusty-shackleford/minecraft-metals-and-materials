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
package com.chunkworks.metalsandmaterials.gametest;

import com.chunkworks.metalsandmaterials.MetalsAndMaterials;
import com.chunkworks.metalsandmaterials.ModContent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * What the pack can rely on: three iron and a coal make three steel; nine
 * ingots are a block and a block is nine ingots; an ingot is nine nuggets and
 * nine nuggets an ingot; each grid finds exactly its recipe, so nothing else
 * in the pack answers to it; the common tags name every piece and the
 * parent tags include them; the block is mined with a stone pickaxe and
 * drops itself; the pieces sit beside iron's in the creative tabs.
 *
 * <p>Partitions. Recipes: each of the five, matched by the exact grid, and a
 * near miss (four iron, no coal) that matches nothing of ours. Tags: the
 * three specific tags, the three parents, the two block tags. Drops: the
 * right tool. Tabs: Ingredients and Building Blocks, position after iron.
 */
@GameTestHolder(MetalsAndMaterials.MOD_ID)
@PrefixGameTestTemplate(false)
public final class MetalsGameTests {
    private static final String NS = MetalsAndMaterials.MOD_ID;

    public MetalsGameTests() {}

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(NS, path);
    }

    private static TagKey<Item> itemTag(String ns, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ns, path));
    }

    private static TagKey<Block> blockTag(String ns, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(ns, path));
    }

    /** The one crafting recipe a grid answers to, asserted to be exactly one and ours. */
    private static RecipeHolder<CraftingRecipe> theOnlyRecipeFor(GameTestHelper helper, int width, int height, List<ItemStack> grid, String expected) {
        CraftingInput input = CraftingInput.of(width, height, grid);
        List<RecipeHolder<CraftingRecipe>> found = helper.getLevel().getRecipeManager().getRecipesFor(RecipeType.CRAFTING, input, helper.getLevel());
        helper.assertTrue(found.size() == 1, "exactly one recipe for the " + expected + " grid, found " + found.stream().map(r -> r.id().toString()).toList());
        helper.assertTrue(found.get(0).id().equals(id(expected)), "the recipe is ours: " + found.get(0).id());
        return found.get(0);
    }

    private static ItemStack result(GameTestHelper helper, RecipeHolder<CraftingRecipe> recipe, int width, int height, List<ItemStack> grid) {
        return recipe.value().assemble(CraftingInput.of(width, height, grid), helper.getLevel().registryAccess());
    }

    private static List<ItemStack> nineOf(Item item) {
        return Collections.nCopies(9, new ItemStack(item));
    }

    @GameTest(template = "arena")
    public void threeIronAndACoalMakeThreeSteel(GameTestHelper helper) {
        List<ItemStack> grid = List.of(new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.CHARCOAL));
        RecipeHolder<CraftingRecipe> recipe = theOnlyRecipeFor(helper, 2, 2, grid, "steel_ingot");
        ItemStack out = result(helper, recipe, 2, 2, grid);
        helper.assertTrue(out.is(ModContent.STEEL_INGOT.get()) && out.getCount() == 3, "three steel ingots, got " + out);
        helper.succeed();
    }

    @GameTest(template = "arena")
    public void fourIronWithoutCoalIsNotSteel(GameTestHelper helper) {
        List<ItemStack> grid = Collections.nCopies(4, new ItemStack(Items.IRON_INGOT));
        CraftingInput input = CraftingInput.of(2, 2, grid);
        boolean ours = helper.getLevel().getRecipeManager().getRecipesFor(RecipeType.CRAFTING, input, helper.getLevel())
                .stream().anyMatch(r -> r.id().getNamespace().equals(NS));
        helper.assertFalse(ours, "no steel from iron alone");
        helper.succeed();
    }

    @GameTest(template = "arena")
    public void nineIngotsMakeABlockAndABlockMakesNineIngots(GameTestHelper helper) {
        List<ItemStack> nine = nineOf(ModContent.STEEL_INGOT.get());
        RecipeHolder<CraftingRecipe> block = theOnlyRecipeFor(helper, 3, 3, nine, "steel_block");
        ItemStack out = result(helper, block, 3, 3, nine);
        helper.assertTrue(out.is(ModContent.STEEL_BLOCK_ITEM.get()) && out.getCount() == 1, "one block, got " + out);

        List<ItemStack> one = List.of(new ItemStack(ModContent.STEEL_BLOCK_ITEM.get()));
        RecipeHolder<CraftingRecipe> back = theOnlyRecipeFor(helper, 1, 1, one, "steel_ingot_from_steel_block");
        ItemStack ingots = result(helper, back, 1, 1, one);
        helper.assertTrue(ingots.is(ModContent.STEEL_INGOT.get()) && ingots.getCount() == 9, "nine ingots back, got " + ingots);
        helper.succeed();
    }

    @GameTest(template = "arena")
    public void anIngotMakesNineNuggetsAndNineNuggetsAnIngot(GameTestHelper helper) {
        List<ItemStack> one = List.of(new ItemStack(ModContent.STEEL_INGOT.get()));
        RecipeHolder<CraftingRecipe> nuggets = theOnlyRecipeFor(helper, 1, 1, one, "steel_nugget");
        ItemStack out = result(helper, nuggets, 1, 1, one);
        helper.assertTrue(out.is(ModContent.STEEL_NUGGET.get()) && out.getCount() == 9, "nine nuggets, got " + out);

        List<ItemStack> nine = nineOf(ModContent.STEEL_NUGGET.get());
        RecipeHolder<CraftingRecipe> back = theOnlyRecipeFor(helper, 3, 3, nine, "steel_ingot_from_nuggets");
        ItemStack ingot = result(helper, back, 3, 3, nine);
        helper.assertTrue(ingot.is(ModContent.STEEL_INGOT.get()) && ingot.getCount() == 1, "one ingot back, got " + ingot);
        helper.succeed();
    }

    @GameTest(template = "arena")
    public void theCommonTagsNameEveryPieceOfSteel(GameTestHelper helper) {
        ItemStack ingot = new ItemStack(ModContent.STEEL_INGOT.get());
        ItemStack nugget = new ItemStack(ModContent.STEEL_NUGGET.get());
        ItemStack block = new ItemStack(ModContent.STEEL_BLOCK_ITEM.get());
        helper.assertTrue(ingot.is(itemTag("c", "ingots/steel")), "c:ingots/steel");
        helper.assertTrue(ingot.is(itemTag("c", "ingots")), "c:ingots includes steel");
        helper.assertTrue(nugget.is(itemTag("c", "nuggets/steel")), "c:nuggets/steel");
        helper.assertTrue(nugget.is(itemTag("c", "nuggets")), "c:nuggets includes steel");
        helper.assertTrue(block.is(itemTag("c", "storage_blocks/steel")), "c:storage_blocks/steel (item)");
        helper.assertTrue(block.is(itemTag("c", "storage_blocks")), "c:storage_blocks includes steel (item)");
        BlockState state = ModContent.STEEL_BLOCK.get().defaultBlockState();
        helper.assertTrue(state.is(blockTag("c", "storage_blocks/steel")), "c:storage_blocks/steel (block)");
        helper.assertTrue(state.is(blockTag("c", "storage_blocks")), "c:storage_blocks includes steel (block)");
        helper.assertFalse(new ItemStack(Items.IRON_INGOT).is(itemTag("c", "ingots/steel")), "iron is not steel");
        helper.succeed();
    }

    @GameTest(template = "arena")
    public void theBlockNeedsAStonePickaxeAndDropsItself(GameTestHelper helper) {
        BlockPos at = new BlockPos(4, 1, 4);
        helper.setBlock(at, ModContent.STEEL_BLOCK.get());
        BlockState state = helper.getBlockState(at);
        helper.assertTrue(state.is(BlockTags.MINEABLE_WITH_PICKAXE), "mineable with a pickaxe");
        helper.assertTrue(state.is(BlockTags.NEEDS_STONE_TOOL), "needs a stone tool");
        helper.assertFalse(new ItemStack(Items.WOODEN_PICKAXE).isCorrectToolForDrops(state), "a wooden pickaxe does not mine it");
        helper.assertTrue(new ItemStack(Items.STONE_PICKAXE).isCorrectToolForDrops(state), "a stone pickaxe does");
        List<ItemStack> drops = Block.getDrops(state, helper.getLevel(), helper.absolutePos(at), null, null, new ItemStack(Items.STONE_PICKAXE));
        helper.assertTrue(drops.size() == 1 && drops.get(0).is(ModContent.STEEL_BLOCK_ITEM.get()) && drops.get(0).getCount() == 1,
                "drops one block of steel, dropped " + drops);
        helper.succeed();
    }

    @GameTest(template = "arena")
    public void steelSitsBesideIronInTheCreativeTabs(GameTestHelper helper) {
        CreativeModeTabs.tryRebuildTabContents(FeatureFlags.DEFAULT_FLAGS, true, helper.getLevel().registryAccess());
        for (var item : List.of(ModContent.STEEL_INGOT.get(), ModContent.STEEL_NUGGET.get(), ModContent.STEEL_BLOCK_ITEM.get())) {
            helper.assertTrue(ModContent.CREATIVE_TAB.get().getDisplayItems().stream().anyMatch(stack -> stack.is(item)),
                    "the Metals and Materials tab contains " + item);
        }
        assertAfter(helper, CreativeModeTabs.INGREDIENTS, Items.IRON_INGOT, ModContent.STEEL_INGOT.get());
        assertAfter(helper, CreativeModeTabs.INGREDIENTS, Items.IRON_NUGGET, ModContent.STEEL_NUGGET.get());
        assertAfter(helper, CreativeModeTabs.BUILDING_BLOCKS, Items.IRON_BLOCK, ModContent.STEEL_BLOCK_ITEM.get());
        helper.succeed();
    }

    private static void assertAfter(GameTestHelper helper, net.minecraft.resources.ResourceKey<CreativeModeTab> tab, Item before, Item after) {
        List<Item> items = new ArrayList<>();
        for (ItemStack stack : helper.getLevel().registryAccess().registryOrThrow(Registries.CREATIVE_MODE_TAB).getOrThrow(tab).getDisplayItems()) {
            items.add(stack.getItem());
        }
        int i = items.indexOf(before);
        int j = items.indexOf(after);
        helper.assertTrue(i >= 0 && j == i + 1, after + " right after " + before + " in " + tab.location() + ": " + i + " / " + j);
    }
}
