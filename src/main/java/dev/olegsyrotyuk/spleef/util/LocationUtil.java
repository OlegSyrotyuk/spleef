package dev.olegsyrotyuk.spleef.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

@UtilityClass
public class LocationUtil {

    public Location strToLoc(String loc) {
        String[] split = loc.split(" ");
        World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            Bukkit.createWorld(new WorldCreator(split[0]));
        }
        world = Bukkit.getWorld(split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);
        Location location = new Location(world, x, y, z);
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }

}
