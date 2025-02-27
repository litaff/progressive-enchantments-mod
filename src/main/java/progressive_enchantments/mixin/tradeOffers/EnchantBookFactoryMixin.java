package progressive_enchantments.mixin.tradeOffers;

import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import progressive_enchantments.config.Config;

// Locks villager enchanted books to level 1.
@Mixin(TradeOffers.EnchantBookFactory.class)
public class EnchantBookFactoryMixin {
    @ModifyArg(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"), index = 1)
    private int adjustMinLevel(int i){
        if (!Config.get().affectVillagerTrading) return i;
        return 1;
    }

    @ModifyArg(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"), index = 2)
    private int adjustMaxLevel(int j){
        if (!Config.get().affectVillagerTrading) return j;
        return 1;
    }
}
