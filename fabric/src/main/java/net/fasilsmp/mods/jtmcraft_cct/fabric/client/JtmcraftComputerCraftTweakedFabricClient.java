package net.fasilsmp.mods.jtmcraft_cct.fabric.client;

import dan200.computercraft.api.client.FabricComputerCraftAPIClient;
import dan200.computercraft.api.client.turtle.TurtleUpgradeModeller;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleUpgradeSerialiser;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ClientModInitializer;
import net.fasilsmp.mods.jtmcraft_cct.JtmcraftComputerCraftTweaked;
import org.jetbrains.annotations.NotNull;

public final class JtmcraftComputerCraftTweakedFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        JtmcraftComputerCraftTweaked
                .turtleUpgradeRegistrySuppliers
                .forEach(this::registerModelForTurtleUpgrade);
    }

    private void registerModelForTurtleUpgrade(@NotNull RegistrySupplier<TurtleUpgradeSerialiser<? extends ITurtleUpgrade>> upgradeSupplier) {
        FabricComputerCraftAPIClient.registerTurtleUpgradeModeller(upgradeSupplier.get(), TurtleUpgradeModeller.flatItem());
    }
}
