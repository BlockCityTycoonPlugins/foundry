package me.darkmun.blockcitytycoonfoundry.listeners;

import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.darkmun.blockcitytycoonfoundry.FoundryFurnaceBlock;
import me.darkmun.blockcitytycoonfoundry.commands.FoundryFurnaceCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class BlockChangeListener extends PacketAdapter {

    public BlockChangeListener(Plugin plugin) {
        super(plugin, PacketType.Play.Server.BLOCK_CHANGE);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        UUID playerUID = player.getUniqueId();

        WrapperPlayServerBlockChange wrapper = new WrapperPlayServerBlockChange(event.getPacket());
        int x = wrapper.getLocation().getX();
        int y = wrapper.getLocation().getY();
        int z = wrapper.getLocation().getZ();

        FoundryFurnaceBlock furnace = FoundryFurnaceCommand.getPlayersFurnaces().get(playerUID).stream().filter(block ->
                (block.getX() == x) && (block.getY() == y) && (block.getZ() == z)).findAny().orElse(null);

        if (furnace != null) {
            if (!furnace.isSending()) {
                //Bukkit.getLogger().info("NOT SENDING");
                event.setCancelled(true);
            }
        }
    }


}
