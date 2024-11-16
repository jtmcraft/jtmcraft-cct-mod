package net.fasilsmp.mods.jtmcraft_cct.peripheral;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.fasilsmp.mods.jtmcraft_cct.JtmcraftComputerCraftTweaked;
import net.fasilsmp.mods.jtmcraft_cct.lua.ServerAwareFunctions;
import org.jetbrains.annotations.Nullable;

public class ServerAwarePeripheral<T> extends ServerAwareFunctions<T> implements IPeripheral {
    protected final T target;

    protected ServerAwarePeripheral(T target) {
        super(target);
        this.target = target;
    }

    @Override
    public String getType() {
        return JtmcraftComputerCraftTweaked.SERVER_AWARE_ID;
    }

    @Override
    public boolean equals(@javax.annotation.Nullable IPeripheral other) {
        return other instanceof ServerAwarePeripheral;
    }

    @Nullable
    @Override
    public T getTarget() {
        return target;
    }
}
