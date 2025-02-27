package progressive_enchantments;

import progressive_enchantments.config.ConfigData;

public class ProgressiveEnchantment {
    private final int enchantmentCap;
    private final float progressionChance;

    public ProgressiveEnchantment(ConfigData.EnchantmentConfigData configData){
        enchantmentCap = configData.progressionCap;
        progressionChance = configData.progressionChance;
    }

    public int progressEnchantmentFrom(int level) {
        var target = level + 1;
        if (target >= enchantmentCap) return level;
        if (Math.random() * 100 > progressionChance) return level;
        return target;
    }
}
