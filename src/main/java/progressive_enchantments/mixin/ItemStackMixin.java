package progressive_enchantments.mixin;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import progressive_enchantments.ProgressiveEnchantment;
import progressive_enchantments.config.Config;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract ItemEnchantmentsComponent getEnchantments();

    @Shadow public abstract void addEnchantment(RegistryEntry<Enchantment> enchantment, int level);

    @Inject(method = "onDurabilityChange", at = @At(value = "HEAD"))
    public void onDurabilityChange(int damage, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci){
        var enchantmentComponent = getEnchantments();
        for (var entry : enchantmentComponent.getEnchantments()) {
            var key = entry.getKey();
            if (key.isEmpty()) continue;
            if (key.get() != Enchantments.UNBREAKING) continue;
            var progressiveEnchantment = new ProgressiveEnchantment(Config.get().unbreakingConfig);
            addEnchantment(entry, progressiveEnchantment.progressEnchantmentFrom(enchantmentComponent.getLevel(entry)));
            return;
        }
    }

}
