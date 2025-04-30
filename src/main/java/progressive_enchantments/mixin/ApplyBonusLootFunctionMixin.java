package progressive_enchantments.mixin;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import progressive_enchantments.ProgressiveEnchantment;
import progressive_enchantments.config.Config;

@Mixin(ApplyBonusLootFunction.class)
public class ApplyBonusLootFunctionMixin {
    @Unique
    private int initialStackAmount;

    @Inject(method = "process", at = @At(value = "HEAD"))
    private void beforeProcess(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        initialStackAmount = stack.getCount();
    }

    @Inject(method = "process", at = @At(value = "RETURN"))
    public void afterProcess(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        if (initialStackAmount != stack.getCount()) return;
        ItemStack itemStack = context.get(LootContextParameters.TOOL);
        if (itemStack == null) return;
        for (var entry : itemStack.getEnchantments().getEnchantments()) {
            if (!entry.matchesKey(Enchantments.FORTUNE)) continue;
            var progressiveEnchantment = new ProgressiveEnchantment(itemStack, entry, Config.get().fortuneConfig);
            progressiveEnchantment.progress();
        }
    }
}
