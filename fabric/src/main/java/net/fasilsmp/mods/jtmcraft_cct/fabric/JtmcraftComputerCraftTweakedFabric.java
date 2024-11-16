package net.fasilsmp.mods.jtmcraft_cct.fabric;

import net.fabricmc.api.ModInitializer;
import net.fasilsmp.mods.jtmcraft_cct.JtmcraftComputerCraftTweaked;

public final class JtmcraftComputerCraftTweakedFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        JtmcraftComputerCraftTweaked.init();
    }
}
