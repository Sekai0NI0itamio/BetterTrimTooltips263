package itamio.bettertrimtooltips.mixin;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Port of Better Trim Tooltips by Andrew6rant (MIT) to Minecraft 1.20.1 (Forge).
 * In 1.20.1 armor trims are stored as NBT (Trim:{material:"...",pattern:"..."}),
 * not as data components, so the trim is read straight from the stack's tag.
 * Condenses the vanilla trim tooltip lines into a single "Pattern (Material)" line.
 *
 * The Fabric version used Fabric API ItemTooltipCallback; Forge has no such
 * callback, so this is a mixin on the vanilla tooltip generation entry point
 * ArmorTrim.appendUpgradeHoverText instead.
 */
@Mixin(ArmorTrim.class)
public class ArmorTrimMixin {

    @Inject(at = @At("TAIL"), method = "appendUpgradeHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/RegistryAccess;Ljava/util/List;)V")
    private static void bettertrimtooltips$appendTooltip(ItemStack stack, RegistryAccess registryAccess, List<Component> tooltip, CallbackInfo info) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return;
        }

        boolean isStackedTrimsEnabled;
        try {
            isStackedTrimsEnabled = ModList.get().isLoaded("stacked_trims");
        } catch (Exception e) {
            isStackedTrimsEnabled = false;
        }

        List<CompoundTag> trims = new ArrayList<>();
        if (isStackedTrimsEnabled) {
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

        tooltip.removeIf(text -> {
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
            tooltip.add(Component.literal("")
                    .append(pattern)
                    .append(Component.literal(" ("))
                    .append(material)
                    .append(Component.literal(")")));
        }
    }

    private static String displayName(String keyPrefix, String id) {
        String path = id.contains(":") ? id.substring(id.indexOf(':') + 1) : id;
        String key = keyPrefix + path;
        String translated = I18n.get(key);
        if (!key.equals(translated)) {
            return translated;
        }
        return path.isEmpty() ? path : Character.toUpperCase(path.charAt(0)) + path.substring(1);
    }
}
