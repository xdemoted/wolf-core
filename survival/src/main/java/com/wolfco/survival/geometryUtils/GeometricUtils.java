package com.wolfco.survival.geometryUtils;

import org.bukkit.Location;
import org.bukkit.World;
import org.joml.Vector3d;

public class GeometricUtils {
    public static Vector3d[] createFibonacciDistribution(int samples, double radius) {
        Vector3d[] points = new Vector3d[samples];
        double phi = Math.PI * (Math.sqrt(5.) - 1.);

        double x, y, z, theta, radius2;
        for (int j = 0; j < samples; j++) {
            y = 1 - ((double) j / (samples - 1)) * 2; // y goes from 1 to -1
            radius2 = Math.sqrt(1 - y * y); // radius at y

            theta = phi * j; // golden angle increment
            x = Math.round(Math.cos(theta) * radius2 * 100.0) / 100.0;
            y = Math.round(y * 100.0) / 100.0;
            z = Math.round(Math.sin(theta) * radius2 * 100.0) / 100.0;

            points[j] = new Vector3d(x, y, z).mul(radius);
        }

        return points;
    }

    public static Location locationFromVector(Vector3d vector, World world) {
        return new Location(world, vector.x, vector.y, vector.z);
    }
}
