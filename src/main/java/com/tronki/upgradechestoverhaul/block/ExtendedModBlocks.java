package com.tronki.upgradechestoverhaul.block;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.maxhenkel.storage.blocks.ModBlocks;
import de.maxhenkel.storage.blocks.ModChestBlock;

public class ExtendedModBlocks extends ModBlocks {

  public static final Map<String, ModChestBlock> MODBLOCK_BY_MAP;
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
    MODBLOCK_BY_MAP = Collections.unmodifiableMap(modblockByMap);
  }
}
