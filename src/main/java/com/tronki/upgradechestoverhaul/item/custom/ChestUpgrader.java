package com.tronki.upgradechestoverhaul.item.custom;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import com.tronki.upgradechestoverhaul.block.ExtendedModBlocks;

import de.maxhenkel.storage.ChestTier;
import de.maxhenkel.storage.blocks.ModChestBlock;
import de.maxhenkel.storage.blocks.tileentity.ModChestTileEntity;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unchecked")
public class ChestUpgrader extends Item {
  public static Minecraft mc = Minecraft.getInstance();
  public static final DirectionProperty FACING = HorizontalBlock.FACING;

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  private static final Properties PROPS = new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_MISC);

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
      ItemUseContext context, ItemStack stack) throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {

    // Get Next Tier
    String nextTier = ((ModChestBlock) clickedBlock.getBlock()).getRegistryName().toString();
    // Get Direction
    Direction direction = clickedBlock.getValue(FACING);
    // Get Chest Tile Entity (Used in getting the current items in the chest)
    ModChestTileEntity chestTileEntity = (ModChestTileEntity) world.getBlockEntity(clickedPos);

    // Store the class of the Chest's tile entity to use it in the reflections of
    // getItems() and setItems()
    Class<?> chestEntityClass = chestTileEntity.getClass();

    // Get the Items of the Chest Tile Entity of the clicked block
    // Parent method is marked as protected and so we have to reflect it, in order
    // to access it
    NonNullList<ItemStack> chestContents = reflectedGetItems(chestEntityClass, chestTileEntity);

    if (nextTier == null) {
      mc.player.chat("This chest is either max tier or can't be upgraded.");
      return;
    }

    // Assign the new Upgraded chest to a local var to place it in the world later
    BlockState newChest = ExtendedModBlocks.CHESTBLOCK_BY_MAP.get(nextTier)
        .defaultBlockState().setValue(FACING, direction);

    // Remove existing chest and its tileEntity
    world.removeBlockEntity(clickedPos);
    world.removeBlock(clickedPos, false);

    // Add Updated chest back to world
    world.setBlock(clickedPos, newChest, 1);

    // Update the Chest Tile Entity var so we have the entity of the upgraded chest
    // (We use this, to set the items of the chest back to the privious ones)
    chestTileEntity = (ModChestTileEntity) world.getBlockEntity(clickedPos);
    ChestTier upgradedChestTier = ((ModChestBlock) world.getBlockState(clickedPos).getBlock()).getTier();

    // Add items from previous chest back into the upgraded one
    reflectedSetItems(chestEntityClass, chestTileEntity, upgradedChestTier, chestContents);

    world.blockEntityChanged(clickedPos, chestTileEntity);

    // Damage item
    stack.shrink(1);
  }

  public static NonNullList<ItemStack> reflectedGetItems(Class<?> chestEntityClass, ModChestTileEntity chestTileEntity)
      throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException {
    Method getItems = chestEntityClass.getDeclaredMethod("getItems");
    getItems.setAccessible(true);

    if (getItems.invoke(chestTileEntity) instanceof NonNullList<?>) {
      return (NonNullList<ItemStack>) getItems.invoke(chestTileEntity);
    }

    return null;
  }

  public static void reflectedSetItems(Class<?> chestEntityClass, ModChestTileEntity chestTileEntity,
      ChestTier upgradedChestTier,
      NonNullList<ItemStack> chestContents) throws IllegalAccessException, IllegalArgumentException,
      InvocationTargetException, NoSuchMethodException, SecurityException {

    Method setItems = chestEntityClass.getDeclaredMethod("setItems", NonNullList.class);
    setItems.setAccessible(true);

    NonNullList<ItemStack> upgradedChestContents = NonNullList.<ItemStack>withSize(upgradedChestTier.numSlots(),
        ItemStack.EMPTY);

    for (int i = 0; i < chestContents.size(); i++) {
      if (i < chestContents.size()) {
        upgradedChestContents.set(i, chestContents.get(i));
      }
    }

    setItems.invoke(chestTileEntity, upgradedChestContents);
  }
}