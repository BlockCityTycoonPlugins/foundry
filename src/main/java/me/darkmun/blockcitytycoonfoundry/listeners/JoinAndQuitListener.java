package me.darkmun.blockcitytycoonfoundry.listeners;

import me.darkmun.blockcitytycoonfoundry.FoundryFurnaceBlock;
import me.darkmun.blockcitytycoonfoundry.commands.FoundryFurnaceCommand;
import net.minecraft.server.v1_12_R1.EnumDirection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

import static me.darkmun.blockcitytycoonfoundry.storages.Configs.mainConfig;
import static me.darkmun.blockcitytycoonfoundry.storages.Configs.playersFurnacesDataConfig;

public class JoinAndQuitListener implements Listener {

    private final Set<String> furnaces = mainConfig.getConfigurationSection("furnaces").getKeys(false);
    private static final int reservedEntityId = Integer.MAX_VALUE / 2;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) { // вместо того, чтобы делать это onJoin можно сделать команду, которая активируется при покупке плавильни
        Player player = event.getPlayer();
        UUID playerUID = player.getUniqueId();
        Map<UUID, List<FoundryFurnaceBlock>> playersFurnaces = FoundryFurnaceCommand.getPlayersFurnaces();

        if (!playersFurnaces.containsKey(playerUID)) {
            List<FoundryFurnaceBlock> furnaceBlocks = createFoundryFurnaceBlocks(player.getWorld(), playerUID);
            playersFurnaces.put(playerUID, furnaceBlocks);
        } else {
            List<FoundryFurnaceBlock> furnaces = playersFurnaces.get(playerUID);
            for (FoundryFurnaceBlock furnace : furnaces) {
                furnace.continueMeltingTask();
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUID = player.getUniqueId();
        Map<UUID, List<FoundryFurnaceBlock>> playersFurnaces = FoundryFurnaceCommand.getPlayersFurnaces();

        List<FoundryFurnaceBlock> furnaces = playersFurnaces.get(playerUID);
        for (FoundryFurnaceBlock furnace : furnaces) {
            furnace.pauseMeltingTask();
        }
    }

    private List<FoundryFurnaceBlock> createFoundryFurnaceBlocks(World world, UUID playerUID) {
        List<FoundryFurnaceBlock> furnaceBlocks = new ArrayList<>();
        int entityId = reservedEntityId;
        for (String furnace : furnaces) {
            int x = mainConfig.getInt(String.format("furnaces.%s.x", furnace));
            int y = mainConfig.getInt(String.format("furnaces.%s.y", furnace));
            int z = mainConfig.getInt(String.format("furnaces.%s.z", furnace));
            EnumDirection facing = EnumDirection.valueOf(mainConfig.getString(String.format("furnaces.%s.facing", furnace)).toUpperCase());
            FoundryFurnaceBlock furnaceBlock = new FoundryFurnaceBlock(world, x, y, z, entityId++, facing, furnace);
            String configState = playersFurnacesDataConfig.getConfig().getString(String.format("%s.furnaces.%s.state", playerUID, furnace));
            if (configState != null) {
                playersFurnacesDataConfig.getConfig().set(String.format("%s.furnaces.%s.state", playerUID, furnace), "PLACED_EMPTY");
                furnaceBlock.setState(FoundryFurnaceBlock.State.PLACED_EMPTY);
            }
            furnaceBlocks.add(furnaceBlock);
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
