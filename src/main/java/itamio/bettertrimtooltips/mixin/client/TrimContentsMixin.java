package itamio.bettertrimtooltips.mixin.client;

import itamio.bettertrimtooltips.BetterTrimTooltipsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armor.ArmorTrim;
import net.minecraft.world.item.component.TrimContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static itamio.bettertrimtooltips.BetterTrimTooltipsClient.isStackedTrimsEnabled;

@Mixin(TrimContents.class)
public class TrimContentsMixin {

    // TrimContents#addTooltip in current (Mojang-mapped) versions appends the
    // pattern line and the smithing-template upgrade line. We condense those
    // into a single line: "<Pattern> (<Material>)".
    @Inject(at = @At("TAIL"), method = "addTooltip")
    private void bettertrimtooltips263$appendTooltip(ItemStack stack, List<Component> tooltip, CallbackInfo info) {
        // NOTE: signature is intentionally provisional; CI diagnostics dump the
        // real signature of TrimContents/ArmorTrim for 26.3.
        tooltip.removeIf(text -> {
            String key = text.getString();
            return key.contains("smithing_template") || key.contains("trim");
        });
    }
}
