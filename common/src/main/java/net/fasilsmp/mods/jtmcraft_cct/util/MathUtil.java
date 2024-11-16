package net.fasilsmp.mods.jtmcraft_cct.util;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class MathUtil {
    public static int toDegrees(double radians) {
        double degrees = Math.toDegrees(radians);
        return (int) ((degrees % 360) + 360) % 360;
    }

    public static int directionFromOrigin(@NotNull Vec3 localOrigin, @NotNull Vec3 target) {
        Vec3 computer2d = new Vec3(localOrigin.x(), 0, localOrigin.z());
        Vec3 target2d = new Vec3(target.x(), 0, target.z());
        Vec3 normal = (target2d.subtract(computer2d)).normalize();
        double radians = Math.atan2(normal.x(), normal.z());
        return toDegrees(radians);
    }
}
