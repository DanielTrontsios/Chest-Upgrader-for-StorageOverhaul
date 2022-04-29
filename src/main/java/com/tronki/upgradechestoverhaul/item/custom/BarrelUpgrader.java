package com.tronki.upgradechestoverhaul.item.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import com.tronki.upgradechestoverhaul.block.ExtendedModBlocks;
import com.tronki.upgradechestoverhaul.item.ModItemGroup;

import de.maxhenkel.storage.ChestTier;
import de.maxhenkel.storage.blocks.ModBarrelBlock;
import de.maxhenkel.storage.blocks.tileentity.ModBarrelTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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
      try {
        upgradeBarrel(playerEntity, clickedBlock, clickedPos, world, context, stack);
      } catch (Exception e) {
        e.printStackTrace();
      }
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
      ItemUseContext context, ItemStack stack) throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    // Get Next Tier
    String nextTier = ((ModBarrelBlock) clickedBlock.getBlock()).getRegistryName().toString();
    // Get Direction
    Direction direction = clickedBlock.getValue(FACING);
    // Get Barrel Tile Entity (Used in getting the current items in the Barrel)
    ModBarrelTileEntity BarrelTileEntity = (ModBarrelTileEntity) world.getBlockEntity(clickedPos);

    // Store the class of the Barrel's tile entity to use it in the reflections of
    // getItems() and setItems()
    Class<?> BarrelEntityClass = BarrelTileEntity.getClass();

    // Get the Items of the Barrel Tile Entity of the clicked block
    // Parent method is marked as protected and so we have to reflect it, in order
    // to access it
    NonNullList<ItemStack> BarrelContents = reflectedGetItems(BarrelEntityClass, BarrelTileEntity);

    if (nextTier == null) {
      mc.player.chat("This Barrel is either max tier or can't be upgraded.");
      return;
    }

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
    BarrelTileEntity = (ModBarrelTileEntity) world.getBlockEntity(clickedPos);
    ChestTier upgradedChestTier = BarrelTileEntity.getTier();

    // Add items from previous Barrel back into the upgraded one
    reflectedSetItems(BarrelEntityClass, BarrelTileEntity, upgradedChestTier, BarrelContents);

    world.blockEntityChanged(clickedPos, BarrelTileEntity);

    // Damage item
    stack.shrink(1);
  }

  public static NonNullList<ItemStack> reflectedGetItems(Class<?> BarrelEntityClass,
      ModBarrelTileEntity BarrelTileEntity)
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    Method getItems = BarrelEntityClass.getDeclaredMethod("getItems");
    getItems.setAccessible(true);

    if (getItems.invoke(BarrelTileEntity) instanceof NonNullList<?>) {
      return (NonNullList<ItemStack>) getItems.invoke(BarrelTileEntity);
    }

    return null;
  }

  public static void reflectedSetItems(Class<?> BarrelEntityClass, ModBarrelTileEntity BarrelTileEntity,
      ChestTier upgradedChestTier,
      NonNullList<ItemStack> BarrelContents) throws IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {

    Method setItems = BarrelEntityClass.getDeclaredMethod("setItems", NonNullList.class);
    setItems.setAccessible(true);

    NonNullList<ItemStack> upgradedBarrelContents = NonNullList.<ItemStack>withSize(upgradedChestTier.numSlots(),
        ItemStack.EMPTY);

    for (int i = 0; i < BarrelContents.size(); i++) {
      if (i < BarrelContents.size()) {
        upgradedBarrelContents.set(i, BarrelContents.get(i));
      }
    }

    setItems.invoke(BarrelTileEntity, upgradedBarrelContents);
  }
}
