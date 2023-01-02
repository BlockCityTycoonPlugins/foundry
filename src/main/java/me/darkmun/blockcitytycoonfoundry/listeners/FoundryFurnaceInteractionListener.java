package me.darkmun.blockcitytycoonfoundry.listeners;

import me.darkmun.blockcitytycoonfoundry.FoundryFurnaceBlock;
import me.darkmun.blockcitytycoonfoundry.commands.FoundryFurnaceCommand;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FoundryFurnaceInteractionListener implements Listener {


    private static final Set<Material> ignoreBlocks = Set.of(Material.AIR, Material.LONG_GRASS, Material.YELLOW_FLOWER, Material.RED_ROSE);

    @EventHandler
    public void onPlayerAnim(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        UUID playerUID = player.getUniqueId();

        List<FoundryFurnaceBlock> furnaces = FoundryFurnaceCommand.getPlayersFurnaces().get(playerUID);
        List<Block> blocksInLineOfSight = player.getLineOfSight(ignoreBlocks, 5);
        for (org.bukkit.block.Block block : blocksInLineOfSight) {
            for (FoundryFurnaceBlock furnace : furnaces) {
                if (block.getLocation().equals(furnace.getLocation())) {
                    if (furnace.getState() == FoundryFurnaceBlock.State.PLACED_EMPTY) {
                        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
                        if (FoundryFurnaceBlock.isMeltable(itemInMainHand.getType())) {
                            furnace.startMeltingTo(player, itemInMainHand);
                        }
                    } else if (furnace.getState() == FoundryFurnaceBlock.State.PLACED_MELTED) {
                        furnace.giveMeltedOutTo(player);
                    } else if (furnace.getState() == FoundryFurnaceBlock.State.NOT_PLACED) {
                        break;
                    }
                    return;
                }
            }
        }

    }

    /*private static boolean isMineBlock(Material material) {
        switch (material) {
            case IRON_ORE:
            case IRON_BLOCK:
            case GOLD_ORE:
            case GOLD_BLOCK:
            case DIAMOND_ORE:
            case DIAMOND_BLOCK:
            case EMERALD_ORE:
            case EMERALD_BLOCK:
                return true;
            default:
                return false;
        }
    }*/
}
