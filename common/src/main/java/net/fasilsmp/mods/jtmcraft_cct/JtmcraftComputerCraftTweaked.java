package net.fasilsmp.mods.jtmcraft_cct;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fasilsmp.mods.jtmcraft_cct.turtle.JtmcraftTurtleUpgrades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public final class JtmcraftComputerCraftTweaked {
    public static final String SERVER_AWARE_ID = "server_aware";
    public static final String LOCATION_AWARE_ID = "location_aware";

    public static final String MOD_ID = "jtmcraft_cct";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final List<RegistrySupplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>>> turtleUpgradeRegistrySuppliers = new ArrayList<>();

    public static void init() {
        JtmcraftTurtleUpgrades.register();
    }
}
