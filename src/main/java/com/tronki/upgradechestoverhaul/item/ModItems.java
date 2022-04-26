package com.tronki.upgradechestoverhaul.item;

import com.tronki.upgradechestoverhaul.UpgradeChestOverhaul;
import com.tronki.upgradechestoverhaul.item.custom.ChestUpgrader;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
      ForgeRegistries.ITEMS,
      UpgradeChestOverhaul.MOD_ID);

  public static final RegistryObject<Item> UPGRADER = ITEMS.register(
      "upgrader",
      () -> new ChestUpgrader());

  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
  }
}
