package com.tronki.upgradechestoverhaul.block.tileentity;

import de.maxhenkel.storage.ChestTier;
import de.maxhenkel.storage.blocks.tileentity.ModChestTileEntity;
import net.minecraft.block.WoodType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ExtendedModChestTileEntity extends ModChestTileEntity {

  public ExtendedModChestTileEntity(WoodType woodType, ChestTier tier) {
    super(woodType, tier);
  }

  @Override
  public NonNullList<ItemStack> getItems() {
    if (chestContents == null) {
      chestContents = NonNullList.withSize(getTier().numSlots(), ItemStack.EMPTY);
    }
    return this.chestContents;
  }

  @Override
  public void setItems(NonNullList<ItemStack> itemsIn) {
    this.chestContents = NonNullList.<ItemStack>withSize(this.getTier().numSlots(), ItemStack.EMPTY);

    for (int i = 0; i < itemsIn.size(); i++) {
      if (i < this.chestContents.size()) {
        this.getItems().set(i, itemsIn.get(i));
      }
    }
  }

}
