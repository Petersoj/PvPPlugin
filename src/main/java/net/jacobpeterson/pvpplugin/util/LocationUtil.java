package net.jacobpeterson.pvpplugin.util;

import org.bukkit.Location;

public class LocationUtil {

    /**
     * Centers location in the middle of its block (on X and Z axis (floors the Y axis)).
     *
     * @param location the location
     * @return the block centered location
     */
    public static Location centerBlockLocation(Location location) {
        double pitch = location.getPitch();
        double yaw = location.getYaw();

        Location centeredLocation = new Location(location.getWorld(),
                location.getBlockX(), location.getBlockY(), location.getBlockZ())
                .add(0.5d, 0d, 0.5d);

        // Set the pitch and yaw again because they get reset in .getBlock().getLocation()
        centeredLocation.setPitch((float) pitch);
        centeredLocation.setYaw((float) yaw);

        return centeredLocation;
    }

}
