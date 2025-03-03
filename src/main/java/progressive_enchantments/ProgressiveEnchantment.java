package progressive_enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import progressive_enchantments.config.ConfigData;

public class ProgressiveEnchantment {
    private final int enchantmentCap;
    private final float initialProgressionChance;
    private final ItemStack itemStack;
    private final RegistryEntry<Enchantment> entry;

    public ProgressiveEnchantment(ItemStack stack, RegistryEntry<Enchantment> entry,
                                  ConfigData.EnchantmentConfigData configData){
        enchantmentCap = configData.progressionCap;
        initialProgressionChance = configData.initialProgressionChance;
        itemStack = stack;
        this.entry = entry;
    }

    public int getLevel() {
        return itemStack.getEnchantments().getLevel(entry);
    }

    public void progressEnchantmentFrom() {
        progressEnchantmentFrom(1f, 0f);
    }

    public void progressEnchantmentFrom(float multiplier, float addend) {
        var level = itemStack.getEnchantments().getLevel(entry);
        var target = level + 1;
        var chance = (initialProgressionChance + addend) * multiplier;
        if (target >= enchantmentCap || Math.random() * 100 > chance) target = level;
        itemStack.addEnchantment(entry, target);
    }
}
