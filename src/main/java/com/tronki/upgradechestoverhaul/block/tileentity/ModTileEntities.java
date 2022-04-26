package com.tronki.upgradechestoverhaul.block.tileentity;

import com.tronki.upgradechestoverhaul.UpgradeChestOverhaul;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
  public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister
      .create(ForgeRegistries.TILE_ENTITIES, UpgradeChestOverhaul.MOD_ID);

  // public static DeferredRegister<TileEntityType<ExtendedModChestTileEntity>>
  // EXTENDED_MOD_CHESTTILE = TILE_ENTITIES.register("extended_mod_chest_tile", ()
  // -> TileEntityType::Builder<>);

  public static void register(IEventBus eventBus) {
    TILE_ENTITIES.register(eventBus);
  }
}
