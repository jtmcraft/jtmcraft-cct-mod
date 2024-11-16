package net.fasilsmp.mods.jtmcraft_cct.turtle;

import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fasilsmp.mods.jtmcraft_cct.JtmcraftComputerCraftTweaked;
import net.fasilsmp.mods.jtmcraft_cct.turtle.LocationAwareTurtleUpgrade;
import net.fasilsmp.mods.jtmcraft_cct.turtle.ServerAwareTurtleUpgrade;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class JtmcraftTurtleUpgrades {
    public static void register() {
        JtmcraftComputerCraftTweaked.LOGGER.info("Registering jtmcraft turtle upgrades");
        DeferredRegister<TurtleUpgradeSerialiser<?>> turtleUpgrades = DeferredRegister.create(JtmcraftComputerCraftTweaked.MOD_ID,TurtleUpgradeSerialiser.registryId());

        registerTurtleUpgrade(
                turtleUpgrades,
                JtmcraftComputerCraftTweaked.SERVER_AWARE_ID,
                () -> TurtleUpgradeSerialiser.simpleWithCustomItem(ServerAwareTurtleUpgrade::new)
        );

        registerTurtleUpgrade(
                turtleUpgrades,
                JtmcraftComputerCraftTweaked.LOCATION_AWARE_ID,
                () -> TurtleUpgradeSerialiser.simpleWithCustomItem(LocationAwareTurtleUpgrade::new)
        );

        turtleUpgrades.register();
    }

    private static void registerTurtleUpgrade(@NotNull DeferredRegister<TurtleUpgradeSerialiser<?>> turtleUpgrades,
                                              String upgradeId,
                                              Supplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>> supplier) {
        RegistrySupplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>> upgradeRegistrySupplier =
                turtleUpgrades.register(upgradeId, supplier);
        JtmcraftComputerCraftTweaked.turtleUpgradeRegistrySuppliers.add(upgradeRegistrySupplier);
    }
}
