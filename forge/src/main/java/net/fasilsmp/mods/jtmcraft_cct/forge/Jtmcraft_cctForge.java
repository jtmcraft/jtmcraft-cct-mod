package net.fasilsmp.mods.jtmcraft_cct.forge;

import net.fasilsmp.mods.jtmcraft_cct.JtmcraftComputerCraftTweaked;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(JtmcraftComputerCraftTweaked.MOD_ID)
public final class Jtmcraft_cctForge {
    public Jtmcraft_cctForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(JtmcraftComputerCraftTweaked.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        JtmcraftComputerCraftTweaked.init();
    }
}
