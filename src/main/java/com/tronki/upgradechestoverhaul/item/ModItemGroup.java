package com.tronki.upgradechestoverhaul.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
  public static final ItemGroup TAB_STORAGE_UPGRADES = new ItemGroup("upgradechestoverhaulModTab") {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(ModItems.UPGRADER_TIER_2.get());
    }
  };
}
