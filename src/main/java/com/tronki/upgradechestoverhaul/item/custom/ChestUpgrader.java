package com.tronki.upgradechestoverhaul.item.custom;

import java.util.Objects;
import java.util.jar.Attributes.Name;

import com.tronki.upgradechestoverhaul.block.ExtendedModBlocks;
import com.tronki.upgradechestoverhaul.block.tileentity.ExtendedModChestTileEntity;

import de.maxhenkel.storage.ChestTier;
import de.maxhenkel.storage.blocks.ModBlocks;
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

public class ChestUpgrader extends Item {
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
      BlockState clickedBlock = world.getBlockState(context.getClickedPos());
      PlayerEntity playerEntity = Objects.requireNonNull(context.getPlayer());

      // TODO look at line80 at
      // https://github.com/progwml6/ironchest/blob/1.16/src/main/java/com/progwml6/ironchest/common/item/ChestUpgradeItem.java

      rightClickedOnChestState(clickedBlock, context, playerEntity, world, stack);
    }

    return super.onItemUseFirst(stack, context);
  }

  private void rightClickedOnChestState(BlockState clickedBlock, ItemUseContext context, PlayerEntity playerEntity,
      World world, ItemStack stack) {
    if (blockIsChestAndUpgradable(clickedBlock)) {
      upgradeChest(playerEntity, clickedBlock, world, context, stack);
    }
  }

  private boolean blockIsChestAndUpgradable(BlockState clickedBlock) {
    if (this.upgraderTier == null) {
      return clickedBlock.getBlock() instanceof ModChestBlock;
    }

    ChestTier clickedChestTier = ((ModChestBlock) clickedBlock.getBlock()).getTier();
    return clickedBlock.getBlock() instanceof ModChestBlock && clickedChestTier == this.upgraderTier;
  }

  public static void upgradeChest(PlayerEntity entity, BlockState clickedBlock, World world,
      ItemUseContext context, ItemStack stack) {

    Minecraft mc = Minecraft.getInstance();
    BlockPos clickedPos = context.getClickedPos();

    // Get Next Tier
    String nextTier = ((ModChestBlock) clickedBlock.getBlock()).getRegistryName().toString();
    // Get Direction
    Direction direction = clickedBlock.getValue(FACING);
    // Get Items
    ModChestTileEntity chestTileEntity = world.getBlockEntity(clickedPos);

    NonNullList<ItemStack> chestContents = chestTileEntity.getItems();

    if (nextTier == null) {
      mc.player.chat("This chest is either max tier or can't be upgraded.");
      return;
    }

    BlockState newChest = ExtendedModBlocks.MODBLOCK_BY_MAP.get(nextTier)
        .defaultBlockState().setValue(FACING, direction);

    // Remove existing chest
    world.removeBlockEntity(clickedPos);
    world.removeBlock(clickedPos, false);

    // Add Updated chest back to world
    world.setBlock(clickedPos, newChest, 1);

    chestTileEntity = (ExtendedModChestTileEntity) world.getBlockEntity(clickedPos);
    chestTileEntity.setItems(chestContents);

    world.blockEntityChanged(clickedPos, chestTileEntity);

    // Damage item
    stack.shrink(1);
  }

}