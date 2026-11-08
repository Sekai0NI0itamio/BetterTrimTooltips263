package itamio.bettertrimtooltips;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/**
 * Unofficial NeoForge port of Better Trim Tooltips by Andrew6rant (MIT).
 * Uses NeoForge's ItemTooltipEvent instead of a mixin (no Fabric API
 * on NeoForge). See NOTICE for credit and licensing.
 */
@Mod("bettertrimtooltips")
public class BetterTrimTooltipsNeoForgeMod {
    public BetterTrimTooltipsNeoForgeMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.addListener(this::onItemTooltip);
    }

    private void onItemTooltip(ItemTooltipEvent event) {
        ArmorTrim trim = event.getItemStack().get(DataComponents.TRIM);
        if (trim == null) {
            return;
        }
        List<Component> lines = event.getToolTip();
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
    }
}
