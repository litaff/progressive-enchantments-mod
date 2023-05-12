package net.fabricmc.progressive_enchantments.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(TradeOffers.EnchantBookFactory.class)
public class EnchantBookFactoryMixin {

	List<Enchantment> progressiveEnchantments = Arrays.asList(
			Enchantments.BANE_OF_ARTHROPODS,
			Enchantments.BLAST_PROTECTION,
			Enchantments.DEPTH_STRIDER,
			Enchantments.EFFICIENCY,
			Enchantments.FEATHER_FALLING,
			Enchantments.FIRE_ASPECT,
			Enchantments.FIRE_PROTECTION,
			Enchantments.FORTUNE,
			Enchantments.IMPALING,
			Enchantments.LOOTING,
			Enchantments.LUCK_OF_THE_SEA,
			Enchantments.LURE,
			Enchantments.PIERCING,
			Enchantments.POWER,
			Enchantments.PROJECTILE_PROTECTION,
			Enchantments.PROTECTION,
			Enchantments.QUICK_CHARGE,
			Enchantments.RESPIRATION,
			Enchantments.RIPTIDE,
			Enchantments.SHARPNESS,
			Enchantments.SMITE,
			Enchantments.SWEEPING,
			Enchantments.THORNS,
			Enchantments.UNBREAKING);

	@Inject(at = @At("RETURN"), method = "create", cancellable = true)
	private void create(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir) {
		TradeOffer offer = cir.getReturnValue();
		ItemStack itemStack = offer.getSellItem();
		Map<Enchantment, Integer> enchantmentMap = EnchantmentHelper.get(itemStack);

		// will only work correctly if there is only one enchantmentMap on an item
		if (enchantmentMap.size() > 1) return;

		var experience = offer.getMerchantExperience();
		var enchantment = (Enchantment) enchantmentMap.keySet().toArray()[0];
		int level = enchantmentMap.get(enchantment);

		if (areEnchantmentProgressive(enchantment)) {
			if (level != enchantment.getMinLevel()) System.out.println("Lowered!");
			level = enchantment.getMinLevel();
			itemStack = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, level));
		}


		int j = 2 + random.nextInt(5 + level * 10) + 3 * level;
		if (enchantment.isTreasure()) {
			j *= 2;
		}
		if (j > 64) {
			j = 64;
		}

		offer = new TradeOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemStack, 12, experience, 0.2f);

		cir.setReturnValue(offer);
	}

	private boolean areEnchantmentProgressive(Enchantment enchantment) {
		// contains all work, because there is only one enchantment on a book
		if(progressiveEnchantments.contains(enchantment))
			return true;
		return false;
	}
}
