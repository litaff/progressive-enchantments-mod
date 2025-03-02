package progressive_enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import progressive_enchantments.config.ConfigData;

public class ProgressiveEnchantment {
    private final int enchantmentCap;
    private final float progressionChance;
    private final ItemStack itemStack;
    private final RegistryEntry<Enchantment> entry;

    public ProgressiveEnchantment(ItemStack stack, RegistryEntry<Enchantment> entry,
                                  ConfigData.EnchantmentConfigData configData){
        enchantmentCap = configData.progressionCap;
        progressionChance = configData.progressionChance;
        itemStack = stack;
        this.entry = entry;
    }

    public void progressEnchantmentFrom() {
        var level = itemStack.getEnchantments().getLevel(entry);
        var target = level + 1;
        if (target >= enchantmentCap || Math.random() * 100 > progressionChance) target = level;
        itemStack.addEnchantment(entry, target);
    }
}
