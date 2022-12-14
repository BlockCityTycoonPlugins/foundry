package me.darkmun.blockcitytycoonfoundry.commands;

import me.darkmun.blockcitytycoonfoundry.BlockCityTycoonFoundry;
import me.darkmun.blockcitytycoonfoundry.Config;
import me.darkmun.blockcitytycoonfoundry.FoundryFurnaceBlock;
import net.minecraft.server.v1_12_R1.EnumDirection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

import static me.darkmun.blockcitytycoonfoundry.storages.Configs.mainConfig;
import static me.darkmun.blockcitytycoonfoundry.storages.Configs.playersFurnacesDataConfig;

public class FoundryFurnaceCommand implements CommandExecutor {
    private static final Map<UUID, List<FoundryFurnaceBlock>> playersFurnaces = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender.hasPermission("bctfoundry.edit")) {
            if (args.length == 3) {
                String action = args[0];
                if (action.equals("add") || action.equals("remove")) { //remove только для дебага
                    Player player = Bukkit.getPlayerExact(args[1]);
                    if (player != null) {
                        String furnaceName = args[2];
                        UUID playerUID = player.getUniqueId();
                        if (mainConfig.contains(String.format("furnaces.%s", furnaceName))) {

                            if (!playersFurnaces.containsKey(playerUID)) {
                                if (action.equals("remove")) {
                                    sender.sendMessage(ChatColor.RED + "Ни одной печки еще не было добавлено");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "Блоки печки еще не объявлены");
                                }
                                return false;
                            }

                            int x = mainConfig.getInt(String.format("furnaces.%s.x", furnaceName));
                            int y = mainConfig.getInt(String.format("furnaces.%s.y", furnaceName));
                            int z = mainConfig.getInt(String.format("furnaces.%s.z", furnaceName));

                            FoundryFurnaceBlock furnace = playersFurnaces.get(playerUID).stream().filter(block -> block.getX() == x && block.getY() == y && block.getZ() == z).findAny().orElse(null);
                            assert furnace != null;
                            if (action.equals("add")) {
                                furnace.setState(FoundryFurnaceBlock.State.PLACED);
                            }
                            else {
                                furnace.setState(FoundryFurnaceBlock.State.EMPTY);
                            }
                            furnace.sendTo(player);

                            playersFurnacesDataConfig.getConfig().set(String.format("%s.name", playerUID), player.getName());
                            playersFurnacesDataConfig.getConfig().set(String.format("%s.furnaces.%s.state", playerUID, furnaceName), furnace.getState().name());
                            playersFurnacesDataConfig.saveConfig();
                        } else {
                            sender.sendMessage(ChatColor.RED + "В конфиге нет печки: " + furnaceName);
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Игрока с ником " + args[0] + " нет на сервере");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Первый аргумент команды введен не верно");
                    sendUsage(sender);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "У команды должно быть три аргумента");
                sendUsage(sender);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "У вас нет прав на использование этой команды");
        }

        return true;
    }



    public void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "Применение: /foundryfurnace [add | remove] [игрок] [название печки]");
    }

    public static Map<UUID, List<FoundryFurnaceBlock>> getPlayersFurnaces() {
        return playersFurnaces;
    }
}
