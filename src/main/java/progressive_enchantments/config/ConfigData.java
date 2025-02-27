package progressive_enchantments.config;

public class ConfigData {
    public boolean affectAnvil = true;
    public boolean affectEnchantingTable = true;
    public boolean affectVillagerTrading = true;

    public EnchantmentConfigData unbreakingConfig = new EnchantmentConfigData(1f, 255);

    public class EnchantmentConfigData {
        public float progressionChance;
        public int progressionCap;

        public EnchantmentConfigData(float chance, int cap){
            progressionChance = chance;
            progressionCap = cap;
        }
    }
}
