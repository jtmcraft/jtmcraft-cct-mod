package net.fasilsmp.mods.jtmcraft_cct.lua;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.turtle.ITurtleAccess;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LocationAwareFunctions<T> {
    private final T target;

    public LocationAwareFunctions(T target) {
        this.target = target;

        if (this.target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        }
    }

    @LuaFunction
    public final @NotNull MethodResult direction() {
        if (target instanceof ITurtleAccess turtle) {
            return MethodResult.of(turtle.getDirection().name());
        }

        throw new InvalidTargetException();
    }

    @LuaFunction
    public final @NotNull MethodResult position() {
        if (target instanceof ITurtleAccess turtle) {
            Map<String, Integer> turtlePosition = new HashMap<>();
            BlockPos blockPos = turtle.getPosition();

            turtlePosition.put("x", blockPos.getX());
            turtlePosition.put("y", blockPos.getY());
            turtlePosition.put("z", blockPos.getZ());

            return MethodResult.of(turtlePosition);
        }

        throw new InvalidTargetException();
    }
}
