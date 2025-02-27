package progressive_enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import progressive_enchantments.config.Config;

import java.util.ArrayList;
import java.util.List;

// Locks resulting enchantments from the table to one.
@Mixin(EnchantmentScreenHandler.class)
public class EnchantmentScreenHandlerMixin {
    @ModifyReturnValue(method = "generateEnchantments", at = @At(value = "RETURN"))
    public List<EnchantmentLevelEntry> setLevelToOne(List<EnchantmentLevelEntry> original){
        if (!Config.get().affectEnchantingTable) return original;
        List<EnchantmentLevelEntry> entries = new ArrayList<>();
        for (var entry : original){
            entries.add(new EnchantmentLevelEntry(entry.enchantment, 1));
        }
        return entries;
    }
}
