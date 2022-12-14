package me.darkmun.blockcitytycoonfoundry.commands;

import me.darkmun.blockcitytycoonfoundry.FoundryFurnaceBlock;
import net.minecraft.server.v1_12_R1.BlockFurnace;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EnumDirection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        Bukkit.getLogger().info(String.format("Args length: %s, sender instanceof player: %s", args.length, sender instanceof Player));
        if (sender instanceof Player && args.length == 4) {
            Bukkit.getLogger().info("Вошло");
            Player pl = (Player) sender;
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            String lit = args[3];

            FoundryFurnaceBlock block = new FoundryFurnaceBlock(pl.getWorld(), x, y, z, EnumDirection.NORTH);
            block.setState(FoundryFurnaceBlock.State.valueOf(lit.toUpperCase()));
            block.sendTo(pl);

            //Bukkit.getLogger().info(String.format("Boolean: %s; World name: %s; Block position: %s %s %s ", Boolean.parseBoolean(args[3]), ((CraftPlayer)pl).getHandle().getWorld().toString(), Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])));
            //BlockFurnace.a(lit, ((CraftPlayer)pl).getHandle().getWorld(), new BlockPosition(x, y, z));
        }

        return true;
    }
}
