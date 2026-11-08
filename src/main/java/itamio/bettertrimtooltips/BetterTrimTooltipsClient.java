package itamio.bettertrimtooltips;

import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;

/**
 * Port of Better Trim Tooltips by Andrew6rant (MIT).
 * Condenses armor trim tooltips into a single "<Pattern> (<Material>)" line.
 * Uses Fabric's ItemTooltipCallback instead of the old ArmorTrim mixin,
 * whose target class/method changed in 26.3.
 */
public class BetterTrimTooltipsClient implements ClientModInitializer {
    public static final Boolean isStackedTrimsEnabled = FabricLoader.getInstance().isModLoaded("stacked_trims");

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            ArmorTrim trim = stack.get(DataComponents.TRIM);
            if (trim == null) {
                return;
            }
            // Remove the vanilla trim lines (pattern line + smithing template upgrade line).
            lines.removeIf(text -> {
                String s = text.getString();
                return s.contains("smithing_template") || s.contains("trim");
            });
            Component pattern = trim.pattern().value().description();
            Component material = trim.material().value().description();
            lines.add(Component.literal("")
                    .append(pattern.copy().withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(" ("))
                    .append(material.copy())
                    .append(Component.literal(")")));
        });
    }
}
