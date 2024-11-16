package net.fasilsmp.mods.jtmcraft_cct.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.fasilsmp.mods.jtmcraft_cct.JtmcraftComputerCraftTweaked;
import net.fasilsmp.mods.jtmcraft_cct.lua.LocationAwareFunctions;
import org.jetbrains.annotations.Nullable;

public class LocationAwarePeripheral<T> extends LocationAwareFunctions<T> implements IPeripheral {
    protected final T target;

    protected LocationAwarePeripheral(T target) {
        super(target);
        this.target = target;
    }

    @Override
    public String getType() {
        return JtmcraftComputerCraftTweaked.LOCATION_AWARE_ID;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other instanceof LocationAwarePeripheral;
    }

    @Nullable
    @Override
    public T getTarget() {
        return target;
    }
}
