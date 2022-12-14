package me.darkmun.blockcitytycoonfoundry.listeners;

import me.darkmun.blockcitytycoonfoundry.BlockCityTycoonFoundry;
import me.darkmun.blockcitytycoonfoundry.Config;
import me.darkmun.blockcitytycoonfoundry.FoundryFurnaceBlock;
import me.darkmun.blockcitytycoonfoundry.commands.FoundryFurnaceCommand;
import net.minecraft.server.v1_12_R1.EnumDirection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

import static me.darkmun.blockcitytycoonfoundry.storages.Configs.mainConfig;

public class JoinListener implements Listener {

    private final Set<String> furnaces = mainConfig.getConfigurationSection("furnaces").getKeys(false);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUID = player.getUniqueId();
        Map<UUID, List<FoundryFurnaceBlock>> playersFurnaces = FoundryFurnaceCommand.getPlayersFurnaces();

        if (!playersFurnaces.containsKey(playerUID)) {
            Bukkit.getLogger().info("Creating furnace blocks...");
            List<FoundryFurnaceBlock> furnaceBlocks = createFoundryFurnaceBlocks(player.getWorld());
            playersFurnaces.put(playerUID, furnaceBlocks);
            Bukkit.getLogger().info("Created");
        }
    }

    private List<FoundryFurnaceBlock> createFoundryFurnaceBlocks(World world) {
        List<FoundryFurnaceBlock> furnaceBlocks = new ArrayList<>();
        for (String furnace : furnaces) {
            int x = mainConfig.getInt(String.format("furnaces.%s.x", furnace));
            int y = mainConfig.getInt(String.format("furnaces.%s.y", furnace));
            int z = mainConfig.getInt(String.format("furnaces.%s.z", furnace));
            EnumDirection facing = EnumDirection.valueOf(mainConfig.getString(String.format("furnaces.%s.facing", furnace)).toUpperCase());
            furnaceBlocks.add(new FoundryFurnaceBlock(world, x, y, z, facing));
        }

        for (FoundryFurnaceBlock furnaceBlock : furnaceBlocks) {
            if (furnaceBlocks.stream().anyMatch(block -> {
                if (furnaceBlock != block) {
                    return block.getX() == furnaceBlock.getX() && block.getY() == furnaceBlock.getY() && block.getZ() == furnaceBlock.getZ();
                }
                return false;
            })) {
                throw new RuntimeException("Two or more blocks in same coordinates (check config file)");
            }
        }
        return furnaceBlocks;
    }
}
