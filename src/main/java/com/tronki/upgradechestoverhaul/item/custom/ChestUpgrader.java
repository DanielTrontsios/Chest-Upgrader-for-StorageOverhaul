package com.tronki.upgradechestoverhaul.item.custom;

import java.util.Objects;

import com.tronki.upgradechestoverhaul.block.ExtendedModBlocks;
import com.tronki.upgradechestoverhaul.item.ModItemGroup;

import de.maxhenkel.storage.ChestTier;
import de.maxhenkel.storage.blocks.ModChestBlock;
import de.maxhenkel.storage.blocks.tileentity.ModChestTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ChestUpgrader extends Item {
  public static Minecraft mc = Minecraft.getInstance();
  public static final DirectionProperty FACING = HorizontalBlock.FACING;

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final Properties PROPS = new Item.Properties().stacksTo(64).tab(ModItemGroup.TAB_STORAGE_UPGRADES);

  private ChestTier upgraderTier;

  public ChestUpgrader() {
    super(PROPS);
  }

  public ChestUpgrader(ChestTier tier) {
    super(PROPS);
    this.upgraderTier = tier;
  }

  @Override
  public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
    World world = context.getLevel();

    if (!world.isClientSide) {
      // Get the position of the chest you want to upgrade
      BlockPos clickedPos = context.getClickedPos();

      BlockState clickedBlock = world.getBlockState(clickedPos);
      PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());

      rightClickedOnChestState(clickedBlock, clickedPos, context, playerEntity, world, stack);
    }

    return super.onItemUseFirst(stack, context);
  }

  private void rightClickedOnChestState(BlockState clickedBlock, BlockPos clickedPos, ItemUseContext context,
      PlayerEntity playerEntity,
      World world, ItemStack stack) {
    if (blockIsChestAndUpgradable(clickedBlock)) {
      try {
        upgradeChest(playerEntity, clickedBlock, clickedPos, world, context, stack);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private boolean blockIsChestAndUpgradable(BlockState clickedBlock) {
    // If chest doesn't have a tier assigned to it, it means that is the
    // debug/creative item and needs to work on all chest types
    if (this.upgraderTier == null) {
      return clickedBlock.getBlock() instanceof ModChestBlock;
    }

    // Otherwise work only on the specific tier of the item
    if (clickedBlock.getBlock() instanceof ModChestBlock) {
      ChestTier clickedChestTier = ((ModChestBlock) clickedBlock.getBlock()).getTier();
      return clickedChestTier == this.upgraderTier;
    }

    return false;
  }

  public static void upgradeChest(PlayerEntity entity, BlockState clickedBlock, BlockPos clickedPos, World world,
      ItemUseContext context, ItemStack stack) {

    // Get Next Tier
    String nextTier = ((ModChestBlock) clickedBlock.getBlock()).getRegistryName().toString();
    // Get Direction
    Direction direction = clickedBlock.getValue(FACING);
    // Get Chest Tile Entity (Used in getting the current items in the chest)
    ModChestTileEntity chestTileEntity = (ModChestTileEntity) world.getBlockEntity(clickedPos);

    if (nextTier == null) {
      mc.player.chat("This chest is either max tier or can't be upgraded.");
      return;
    }

    // Some Parent methods are marked as protected and so we have to use the
    // IItemHandler, in order to get and set the contents of the conteiner
    LazyOptional<IItemHandler> firstItemHandler = chestTileEntity
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

    firstItemHandler.ifPresent(handler -> {
      changeBlockEntity(handler, nextTier, direction, world, clickedPos, stack);
    });
  }

  public static void changeBlockEntity(IItemHandler handler, String nextTier, Direction direction, World world,
      BlockPos clickedPos, ItemStack stack) {
    // Get the Items of the Chest Tile Entity of the clicked block
    NonNullList<ItemStack> containerContents = reflectedGetItems(handler);

    // Assign the new Upgraded Chest to a local var to place it in the world later
    BlockState newChest = ExtendedModBlocks.CHESTBLOCK_BY_MAP.get(nextTier)
        .defaultBlockState().setValue(FACING, direction);

    // Remove existing Chest and its tileEntity
    world.removeBlockEntity(clickedPos);
    world.removeBlock(clickedPos, false);

    // Add Updated Chest back to world
    world.setBlock(clickedPos, newChest, 1);

    // Update the Chest Tile Entity var so we have the entity of the upgraded
    // Chest
    // (We use this, to set the items of the Chest back to the privious ones)
    ModChestTileEntity upgradedChestTileEntity = (ModChestTileEntity) world.getBlockEntity(clickedPos);
    ChestTier upgradedChestTier = upgradedChestTileEntity.getTier();

    LazyOptional<IItemHandler> upgradedItemHandler = upgradedChestTileEntity
        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

    upgradedItemHandler.ifPresent(upgradedHandler -> {
      reflectedSetItems(upgradedHandler, upgradedChestTier, containerContents);

      world.blockEntityChanged(clickedPos, upgradedChestTileEntity);

      // Damage item
      stack.shrink(1);
    });
  }

  public static NonNullList<ItemStack> reflectedGetItems(IItemHandler handler) {
    NonNullList<ItemStack> containerContents = NonNullList.<ItemStack>withSize(handler.getSlots(),
        ItemStack.EMPTY);
    for (int slot = 0; slot < handler.getSlots(); slot++) {
      ItemStack currentItemStack = handler.getStackInSlot(slot);
      containerContents.set(slot, currentItemStack);
    }

    return containerContents;
  }

  public static void reflectedSetItems(IItemHandler upgradedHandler, ChestTier upgradedChestTier,
      NonNullList<ItemStack> containerContents) {
    for (int slot = 0; slot < containerContents.size(); slot++) {
      if (slot < containerContents.size()) {
        upgradedHandler.insertItem(slot, containerContents.get(slot), false);
      }
    }
  }

}