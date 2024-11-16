package net.fasilsmp.mods.jtmcraft_cct.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;

import javax.annotation.Nullable;

public class LocationAwareTurtlePeripheral extends LocationAwarePeripheral<ITurtleAccess> {
    public LocationAwareTurtlePeripheral(ITurtleAccess turtle) {
        super(turtle);
    }

    @Override
    public boolean equals(@Nullable IPeripheral that) {
        return that instanceof LocationAwarePeripheral;
    }
}
