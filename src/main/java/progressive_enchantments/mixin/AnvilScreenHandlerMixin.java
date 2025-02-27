package progressive_enchantments.mixin;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import progressive_enchantments.config.Config;

// Locks enchantment upgrading through the anvil.
// This is done by setting the resulting enchantment level to one.
// The downside being that when enchantment would be added or combined,
// the end product always has a level of one.
@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {
    @ModifyArg(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent$Builder;set(Lnet/minecraft/registry/entry/RegistryEntry;I)V"), index = 1)
    public int adjustResultingLvl_WhenCombiningEnchantments(int r){
        if (!Config.get().affectAnvil) return r;
        return 1;
    }
}
