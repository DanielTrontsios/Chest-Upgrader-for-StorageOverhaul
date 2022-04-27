package com.tronki.upgradechestoverhaul.item;

import com.tronki.upgradechestoverhaul.UpgradeChestOverhaul;
import com.tronki.upgradechestoverhaul.item.custom.ChestUpgrader;

import de.maxhenkel.storage.ChestTier;
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
  public static final RegistryObject<Item> BASE_UPGRADER = ITEMS.register(
      "base_upgrader",
      () -> new ChestUpgrader(ChestTier.BASE_TIER));
  public static final RegistryObject<Item> UPGRADER_TIER_1 = ITEMS.register(
      "upgrader_tier_1",
      () -> new ChestUpgrader(ChestTier.TIER_1));
  public static final RegistryObject<Item> UPGRADER_TIER_2 = ITEMS.register(
      "upgrader_tier_2",
      () -> new ChestUpgrader(ChestTier.TIER_2));

  public static void register(IEventBus eventBus) {
    ITEMS.register(eventBus);
  }
}
