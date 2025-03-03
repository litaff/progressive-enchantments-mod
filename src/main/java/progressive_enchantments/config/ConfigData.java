package progressive_enchantments.config;

public class ConfigData {
    private final int absoluteCap = 255;

    public boolean affectAnvil = true;
    public boolean affectEnchantingTable = true;
    public boolean affectVillagerTrading = true;

    public EnchantmentConfigData unbreakingConfig = new EnchantmentConfigData(1f, absoluteCap);
    public EnchantmentConfigData efficiencyConfig = new EnchantmentConfigData(1f, absoluteCap);

    public void validate() {
        unbreakingConfig.validate(absoluteCap);
        efficiencyConfig.validate(absoluteCap);
    }

    public class EnchantmentConfigData {
        public float initialProgressionChance;
        public int progressionCap;

        public EnchantmentConfigData(float chance, int cap){
            initialProgressionChance = chance;
            progressionCap = cap;
        }

        public void validate(int absoluteCap) {
            if (progressionCap > absoluteCap) progressionCap = absoluteCap;
        }
    }
}
