package progressive_enchantments.config;

public class ConfigData {
    private final int absoluteCap = 255;

    public boolean affectAnvil = true;
    public boolean affectEnchantingTable = true;
    public boolean affectVillagerTrading = true;

    public EnchantmentConfigData unbreakingConfig = new EnchantmentConfigData(1f, absoluteCap);

    public void validate() {
        unbreakingConfig.validate(absoluteCap);
    }

    public class EnchantmentConfigData {
        public float progressionChance;
        public int progressionCap;

        public EnchantmentConfigData(float chance, int cap){
            progressionChance = chance;
            progressionCap = cap;
        }

        public void validate(int absoluteCap) {
            if (progressionCap > absoluteCap) progressionCap = absoluteCap;
        }
    }
}
