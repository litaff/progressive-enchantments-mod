package progressive_enchantments.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import progressive_enchantments.ProgressiveEnchantment;
import progressive_enchantments.config.Config;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract ItemEnchantmentsComponent getEnchantments();

    @Shadow public abstract ComponentMap getComponents();

    @Inject(method = "onDurabilityChange", at = @At(value = "HEAD"))
    public void onDurabilityChange(int damage, @Nullable ServerPlayerEntity player, Consumer<Item> breakCallback, CallbackInfo ci){
        var enchantmentComponent = getEnchantments();
        for (var entry : enchantmentComponent.getEnchantments()) {
            var key = entry.getKey();
            if (key.isEmpty()) continue;
            if (key.get() != Enchantments.UNBREAKING) continue;
            var progressiveEnchantment = new ProgressiveEnchantment(getThis(), entry, Config.get().unbreakingConfig);
            progressiveEnchantment.progressEnchantmentFrom();
            return;
        }
    }

    @Inject(method = "postMine", at = @At(value = "HEAD"))
    public void onPostMine(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci) {
        var enchantmentComponent = getEnchantments();
        for (var entry : enchantmentComponent.getEnchantments()) {
            if (progressEfficiency(entry, world, state, pos)) continue;
            if (progressFortune(entry)) continue;
        }
    }

    @Unique
    private ItemStack getThis(){
        // This cast tricks the compiler. It's a valid way of casting a mixin.
        return (ItemStack) (Object) this;
    }

    @Unique
    private boolean progressEfficiency(RegistryEntry<Enchantment> entry, World world, BlockState state, BlockPos pos) {
        if(entry.getKey().isEmpty() || entry.getKey().get() != Enchantments.EFFICIENCY) return false;

        var toolComponent = this.getComponents().get(DataComponentTypes.TOOL);
        if (toolComponent == null) return false;
        var effectiveRules = toolComponent.rules().stream().filter(rule -> rule.speed().isPresent()).toList();

        for (var rule : effectiveRules) {
            if (rule.speed().isEmpty() || !state.isIn(rule.blocks())) continue;
            var progressiveEnchantment = new ProgressiveEnchantment(getThis(), entry, Config.get().efficiencyConfig);
            var hardness = state.getHardness(world, pos);
            var level = progressiveEnchantment.getLevel();
            var multiplier = (float) (Math.pow(hardness, 1.5 - hardness / 10) / Math.pow(level, 2.5));
            progressiveEnchantment.progressEnchantmentFrom(multiplier, 0f);
            return true;
        }
        return false;
    }

    @Unique
    private boolean progressFortune(RegistryEntry<Enchantment> entry) {
        return false;
    }
}
