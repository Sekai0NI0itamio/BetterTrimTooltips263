package itamio.bettertrimtooltips;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class BetterTrimTooltipsClient implements ClientModInitializer {
    public static final Boolean isStackedTrimsEnabled = FabricLoader.getInstance().isModLoaded("stacked_trims");

    @Override
    public void onInitializeClient() {
    }
}
