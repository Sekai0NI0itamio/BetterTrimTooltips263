package itamio.bettertrimtooltips;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;

/**
 * Port of Better Trim Tooltips by Andrew6rant (MIT) to Minecraft 1.20.1 (Fabric).
 * In 1.20.1 armor trims are stored as NBT ({@code Trim:{material:"...",pattern:"..."}}),
 * not as data components, so the trim is read straight from the stack's tag.
 * Condenses the vanilla trim tooltip lines into a single "{@code <Pattern> (<Material>)}" line.
 */
public class BetterTrimTooltipsClient implements ClientModInitializer {
    public static final Boolean isStackedTrimsEnabled = FabricLoader.getInstance().isModLoaded("stacked_trims");

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            CompoundTag tag = stack.getTag();
            if (tag == null) {
                return;
            }

            List<CompoundTag> trims = new ArrayList<>();
            if (isStackedTrimsEnabled) {
                // StackedTrims stores a list of trim compounds under "Trim".
                ListTag list = tag.getList("Trim", Tag.TAG_COMPOUND);
                for (Tag element : list) {
                    if (element instanceof CompoundTag compound) {
                        trims.add(compound);
                    }
                }
            } else {
                CompoundTag single = tag.getCompound("Trim");
                if (!single.isEmpty()) {
                    trims.add(single);
                }
            }
            if (trims.isEmpty()) {
                return;
            }

            // Remove the vanilla trim tooltip lines (same checks as the original mixin).
            lines.removeIf(text -> {
                ComponentContents content = text.getContents();
                if (content instanceof TranslatableContents translatable) {
                    return "item.minecraft.smithing_template.upgrade".equals(translatable.getKey());
                }
                if (text.getSiblings().size() == 1) {
                    ComponentContents sibling = text.getSiblings().get(0).getContents();
                    if (sibling instanceof TranslatableContents translatable) {
                        return translatable.getKey().contains("trim");
                    }
                }
                return false;
            });

            for (CompoundTag trim : trims) {
                String materialId = trim.getString("material");
                String patternId = trim.getString("pattern");
                if (materialId.isEmpty() || patternId.isEmpty()) {
                    continue;
                }
                Component pattern = Component.literal(displayName("trim_pattern.", patternId))
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY));
                Component material = Component.literal(displayName("trim_material.", materialId));
                lines.add(Component.literal("")
                        .append(pattern)
                        .append(Component.literal(" ("))
                        .append(material)
                        .append(Component.literal(")")));
            }
        });
    }

    private static String displayName(String keyPrefix, String id) {
        String path = id.contains(":") ? id.substring(id.indexOf(':') + 1) : id;
        String key = keyPrefix + path;
        String translated = I18n.get(key);
        if (!key.equals(translated)) {
            return translated;
        }
        // Fallback for trims without a vanilla translation key: prettify the id path.
        return path.isEmpty() ? path : Character.toUpperCase(path.charAt(0)) + path.substring(1);
    }
}
