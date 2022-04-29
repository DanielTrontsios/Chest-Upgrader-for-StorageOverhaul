package com.tronki.upgradechestoverhaul.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.maxhenkel.storage.blocks.ModBarrelBlock;
import de.maxhenkel.storage.blocks.ModBlocks;
import de.maxhenkel.storage.blocks.ModChestBlock;

public class ExtendedModBlocks extends ModBlocks {

  public static final Map<String, ModChestBlock> CHESTBLOCK_BY_MAP;
  static {
    final Map<String, ModChestBlock> modblockByMap = new HashMap<>();
    // OAK
    modblockByMap.put("storage_overhaul:oak_chest", OAK_CHEST_TIER_1);
    modblockByMap.put("storage_overhaul:oak_chest_tier_1", OAK_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:oak_chest_tier_2", OAK_CHEST_TIER_3);
    // SPRUCE
    modblockByMap.put("storage_overhaul:spruce_chest", SPRUCE_CHEST_TIER_1);
    modblockByMap.put("storage_overhaul:spruce_chest_tier_1", SPRUCE_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:spruce_chest_tier_2", SPRUCE_CHEST_TIER_3);
    // BIRCH
    modblockByMap.put("storage_overhaul:birch_chest", BIRCH_CHEST);
    modblockByMap.put("storage_overhaul:birch_chest_tier_1", BIRCH_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:birch_chest_tier_2", BIRCH_CHEST_TIER_3);
    // ACACIA
    modblockByMap.put("storage_overhaul:acacia_chest", ACACIA_CHEST_TIER_1);
    modblockByMap.put("storage_overhaul:acacia_chest_tier_1", ACACIA_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:acacia_chest_tier_2", ACACIA_CHEST_TIER_3);
    // JUNGLE
    modblockByMap.put("storage_overhaul:jungle_chest", JUNGLE_CHEST_TIER_1);
    modblockByMap.put("storage_overhaul:jungle_chest_tier_1", JUNGLE_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:jungle_chest_tier_2", JUNGLE_CHEST_TIER_3);
    // DARK_OAK
    modblockByMap.put("storage_overhaul:dark_oak_chest", DARK_OAK_CHEST_TIER_1);
    modblockByMap.put("storage_overhaul:dark_oak_chest_tier_1", DARK_OAK_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:dark_oak_chest_tier_2", DARK_OAK_CHEST_TIER_3);
    // CRIMSON
    modblockByMap.put("storage_overhaul:crimson_chest", CRIMSON_CHEST_TIER_1);
    modblockByMap.put("storage_overhaul:crimson_chest_tier_1", CRIMSON_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:crimson_chest_tier_2", CRIMSON_CHEST_TIER_3);
    // WARPED
    modblockByMap.put("storage_overhaul:warped_chest", WARPED_CHEST_TIER_1);
    modblockByMap.put("storage_overhaul:warped_chest_tier_1", WARPED_CHEST_TIER_2);
    modblockByMap.put("storage_overhaul:warped_chest_tier_2", WARPED_CHEST_TIER_3);
    CHESTBLOCK_BY_MAP = Collections.unmodifiableMap(modblockByMap);
  }

  public static final Map<String, ModBarrelBlock> BARRELBLOCK_BY_MAP;
  static {
    final Map<String, ModBarrelBlock> modBarrelBlockByMap = new HashMap<>();
    // OAK
    modBarrelBlockByMap.put("storage_overhaul:oak_barrel", OAK_BARREL_TIER_1);
    modBarrelBlockByMap.put("storage_overhaul:oak_barrel_tier_1", OAK_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:oak_barrel_tier_2", OAK_BARREL_TIER_3);
    // SPRUCE
    modBarrelBlockByMap.put("storage_overhaul:spruce_barrel", SPRUCE_BARREL_TIER_1);
    modBarrelBlockByMap.put("storage_overhaul:spruce_barrel_tier_1", SPRUCE_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:spruce_barrel_tier_2", SPRUCE_BARREL_TIER_3);
    // BIRCH
    modBarrelBlockByMap.put("storage_overhaul:birch_barrel", BIRCH_BARREL);
    modBarrelBlockByMap.put("storage_overhaul:birch_barrel_tier_1", BIRCH_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:birch_barrel_tier_2", BIRCH_BARREL_TIER_3);
    // ACACIA
    modBarrelBlockByMap.put("storage_overhaul:acacia_barrel", ACACIA_BARREL_TIER_1);
    modBarrelBlockByMap.put("storage_overhaul:acacia_barrel_tier_1", ACACIA_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:acacia_barrel_tier_2", ACACIA_BARREL_TIER_3);
    // JUNGLE
    modBarrelBlockByMap.put("storage_overhaul:jungle_barrel", JUNGLE_BARREL_TIER_1);
    modBarrelBlockByMap.put("storage_overhaul:jungle_barrel_tier_1", JUNGLE_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:jungle_barrel_tier_2", JUNGLE_BARREL_TIER_3);
    // DARK_OAK
    modBarrelBlockByMap.put("storage_overhaul:dark_oak_barrel", DARK_OAK_BARREL_TIER_1);
    modBarrelBlockByMap.put("storage_overhaul:dark_oak_barrel_tier_1", DARK_OAK_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:dark_oak_barrel_tier_2", DARK_OAK_BARREL_TIER_3);
    // CRIMSON
    modBarrelBlockByMap.put("storage_overhaul:crimson_barrel", CRIMSON_BARREL_TIER_1);
    modBarrelBlockByMap.put("storage_overhaul:crimson_barrel_tier_1", CRIMSON_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:crimson_barrel_tier_2", CRIMSON_BARREL_TIER_3);
    // WARPED
    modBarrelBlockByMap.put("storage_overhaul:warped_barrel", WARPED_BARREL_TIER_1);
    modBarrelBlockByMap.put("storage_overhaul:warped_barrel_tier_1", WARPED_BARREL_TIER_2);
    modBarrelBlockByMap.put("storage_overhaul:warped_barrel_tier_2", WARPED_BARREL_TIER_3);
    BARRELBLOCK_BY_MAP = Collections.unmodifiableMap(modBarrelBlockByMap);
  }
}
