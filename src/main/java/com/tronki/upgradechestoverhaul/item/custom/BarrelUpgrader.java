package com.tronki.upgradechestoverhaul.item.custom;

import java.util.Objects;

import com.tronki.upgradechestoverhaul.block.ExtendedModBlocks;
import com.tronki.upgradechestoverhaul.item.ModItemGroup;

import de.maxhenkel.storage.ChestTier;
import de.maxhenkel.storage.blocks.ModBarrelBlock;
import de.maxhenkel.storage.blocks.tileentity.ModBarrelTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BarrelUpgrader extends Item {
  public static Minecraft mc = Minecraft.getInstance();
  public static final DirectionProperty FACING = BlockStateProperties.FACING;

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final Properties PROPS = new Item.Properties().stacksTo(64).tab(ModItemGroup.TAB_STORAGE_UPGRADES);

  private ChestTier upgraderTier;

  public BarrelUpgrader() {
    super(PROPS);
  }

  public BarrelUpgrader(ChestTier tier) {
    super(PROPS);
    this.upgraderTier = tier;
  }

  @Override
  public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
    World world = context.getLevel();

    if (!world.isClientSide) {
      // Get the position of the Barrel you want to upgrade
      BlockPos clickedPos = context.getClickedPos();

      BlockState clickedBlock = world.getBlockState(clickedPos);
      TileEntity clickedBlockEntity = world.getBlockEntity(clickedPos);
      PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());

      rightClickedOnBarrelState(clickedBlock, clickedBlockEntity, clickedPos, context, playerEntity, world, stack);
    }

    return super.onItemUseFirst(stack, context);
  }

  private void rightClickedOnBarrelState(BlockState clickedBlock, TileEntity clickedBlockEntity, BlockPos clickedPos,
      ItemUseContext context,
      PlayerEntity playerEntity,
      World world, ItemStack stack) {
    if (blockIsBarrelAndUpgradable(clickedBlockEntity)) {
      upgradeBarrel(playerEntity, clickedBlock, clickedPos, world, context, stack);
    }
  }

  private boolean blockIsBarrelAndUpgradable(TileEntity clickedBlockEntity) {
    // If Barrel doesn't have a tier assigned to it, it means that is the
    // debug/creative item and needs to work on all Barrel types
    if (this.upgraderTier == null) {
      return clickedBlockEntity instanceof ModBarrelTileEntity;
    }

    // Otherwise work only on the specific tier of the item
    if (clickedBlockEntity instanceof ModBarrelTileEntity) {
      ChestTier clickedBarrelTier = ((ModBarrelTileEntity) clickedBlockEntity).getTier();
      return clickedBarrelTier == this.upgraderTier;
    }

    return false;
  }

  public static void upgradeBarrel(PlayerEntity entity, BlockState clickedBlock, BlockPos clickedPos, World world,
      ItemUseContext context, ItemStack stack) {

    // Get Next Tier
    String nextTier = ((ModBarrelBlock) clickedBlock.getBlock()).getRegistryName().toString();
    // Get Direction
    Direction direction = clickedBlock.getValue(FACING);
    // Get Barrel Tile Entity (Used in getting the current items in the Barrel)
    ModBarrelTileEntity BarrelTileEntity = (ModBarrelTileEntity) world.getBlockEntity(clickedPos);

    if (nextTier == null) {
      mc.player.chat("This Barrel is either max tier or can't be upgraded.");
      return;
    }

    // Some Parent methods are marked as protected and so we have to use the
    // IItemHandler, in order to get and set the contents of the conteiner
    LazyOptional<IItemHandler> firstItemHandler = BarrelTileEntity
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

    firstItemHandler.ifPresent(handler -> {
      changeBlockEntity(handler, nextTier, direction, world, clickedPos, stack);
    });

  }

  public static void changeBlockEntity(IItemHandler handler, String nextTier, Direction direction, World world,
      BlockPos clickedPos, ItemStack stack) {
    // Get the Items of the Barrel Tile Entity of the clicked block
    NonNullList<ItemStack> barrelContents = reflectedGetItems(handler);

    // Assign the new Upgraded Barrel to a local var to place it in the world later
    BlockState newBarrel = ExtendedModBlocks.BARRELBLOCK_BY_MAP.get(nextTier)
        .defaultBlockState().setValue(FACING, direction);

    // Remove existing Barrel and its tileEntity
    world.removeBlockEntity(clickedPos);
    world.removeBlock(clickedPos, false);

    // Add Updated Barrel back to world
    world.setBlock(clickedPos, newBarrel, 1);

    // Update the Barrel Tile Entity var so we have the entity of the upgraded
    // Barrel
    // (We use this, to set the items of the Barrel back to the privious ones)
    ModBarrelTileEntity upgradedBarrelTileEntity = (ModBarrelTileEntity) world.getBlockEntity(clickedPos);
    ChestTier upgradedChestTier = upgradedBarrelTileEntity.getTier();

    LazyOptional<IItemHandler> upgradedItemHandler = upgradedBarrelTileEntity
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

    upgradedItemHandler.ifPresent(upgradedHandler -> {
      reflectedSetItems(upgradedHandler, upgradedChestTier, barrelContents);

      world.blockEntityChanged(clickedPos, upgradedBarrelTileEntity);

      // Damage item
      stack.shrink(1);
    });
  }

  public static NonNullList<ItemStack> reflectedGetItems(IItemHandler handler) {
    NonNullList<ItemStack> barrelContents = NonNullList.<ItemStack>withSize(handler.getSlots(),
        ItemStack.EMPTY);
    for (int slot = 0; slot < handler.getSlots(); slot++) {
      ItemStack currentItemStack = handler.getStackInSlot(slot);
      barrelContents.set(slot, currentItemStack);
    }

    return barrelContents;
  }

  public static void reflectedSetItems(IItemHandler upgradedHandler, ChestTier upgradedChestTier,
      NonNullList<ItemStack> barrelContents) {

    for (int slot = 0; slot < barrelContents.size(); slot++) {
      if (slot < barrelContents.size()) {
        upgradedHandler.insertItem(slot, barrelContents.get(slot), false);
      }
    }
  }
}
