package net.fasilsmp.mods.jtmcraft_cct.turtle;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.AbstractTurtleUpgrade;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import net.fasilsmp.mods.jtmcraft_cct.JtmcraftComputerCraftTweaked;
import net.fasilsmp.mods.jtmcraft_cct.peripheral.LocationAwareTurtlePeripheral;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class LocationAwareTurtleUpgrade extends AbstractTurtleUpgrade {
    public LocationAwareTurtleUpgrade(ResourceLocation id, ItemStack stack) {
        super(id, TurtleUpgradeType.PERIPHERAL, JtmcraftComputerCraftTweaked.LOCATION_AWARE_ID, stack);
    }

    @Override
    public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
        return new LocationAwareTurtlePeripheral(turtle);
    }
}
