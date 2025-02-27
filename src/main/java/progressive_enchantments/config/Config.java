package progressive_enchantments.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import progressive_enchantments.ProgressiveEnchantments;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
    private static final Path CONFIG_PATH = CONFIG_DIR.resolve("progressive-enchantments.json");

    private static ConfigData configCurrent;

    public static ConfigData get() {
        if (configCurrent == null) load();
        return configCurrent;
    }

    public static void load() {
        var file = CONFIG_PATH.toFile();

        try {
            Files.createDirectories(CONFIG_DIR);

            if (file.exists()) {
                configCurrent = new Gson().fromJson(new FileReader(file), ConfigData.class);
            } else {
                configCurrent = new ConfigData();
            }
        } catch (Exception e) {
            configCurrent = new ConfigData();
            ProgressiveEnchantments.LOGGER.error("Failed to load config file", e);
        }

        save();
    }

    public static void save() {
        try (FileWriter file = new FileWriter(CONFIG_PATH.toFile())) {
            var json = new Gson().newBuilder().setPrettyPrinting().create().toJson(configCurrent);
            file.write(json);
            file.flush();
        } catch (Exception e) {
            ProgressiveEnchantments.LOGGER.error("Failed to save config file", e);
        }
    }
}
