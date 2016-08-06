package org.royalmc.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {
    /**
     * This is a simple method for parsing strings to locations. This would be useful for retrieving
     * location data that was stored in a database or .yml file. A use of this could be to retrieve locations for
     * the spawn points of a minigame from strings.
     *
     * @param s
     *            The string value of a serialised location which is to be converted back into a location object.
     * @return The parsed location if parsing was successful or null if it was not.
     */
    public Location parseLocation(String s) {
        String[] split = s.split(",");
        Location loc = null;

        try {
            World world = Bukkit.getWorld(split[0]);
            if (split.length == 6) {
                double x = Double.parseDouble(split[1]);
                double y = Double.parseDouble(split[2]);
                double z = Double.parseDouble(split[3]);

                float yaw = Float.parseFloat(split[4]);
                float pitch = Float.parseFloat(split[5]);
                loc = new Location(world, x, y, z, yaw, pitch);
            } else if (split.length == 4) {
                int x = Integer.parseInt(split[1]);
                int y = Integer.parseInt(split[2]);
                int z = Integer.parseInt(split[3]);

                loc = new Location(world, x, y, z);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("\u001B[30;1m[\u001B[32;1mRoyalMC Core\u001B[30;1m] \u001B[31;1mCannot parse location from string: " + s + " - Maybe this is the reason: " + e.toString() + "\u001B[0m");
        }

        return loc;
    }

    /**
     * This is a simple method for serialising locations to strings. This would be useful for something such as storing spawn points for a MiniGame.
     *
     * @param l
     *            The location object that is to be serialised.
     * @param exact
     *            Whether or not to return an exact serialisation of the string (meaning that it includes pitch, yaw and the exact x, y, z of the location [this is the case if exact is true]). If exact is false then a string containing only the world and the block x, y, z values will be returned.
     * @return A serialised string for the location provided or null if no location was provided.
     */
    public String serializeLocation(Location l, boolean exact) {
        if (l != null) {
            String key = l.getWorld().getName() + ",";
            if (exact) {
                key += l.getX() + "," + l.getY() + "," + l.getZ() + "," + l.getYaw() + "," + l.getPitch();
            } else {
                key += l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ();
            }

            return key;
        }
        return null;
    }
}
