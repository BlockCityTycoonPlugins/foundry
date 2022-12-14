package me.darkmun.blockcitytycoonfoundry;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static net.minecraft.server.v1_12_R1.BlockFurnace.FACING;

public class FoundryFurnaceBlock {

    public enum State {
        EMPTY,
        PLACED,
        MELTS;
    }

    private int x;
    private int y;
    private int z;
    private Location loc;
    private State state = State.EMPTY;
    private final EnumDirection facing;

    public FoundryFurnaceBlock(World world, int x, int y, int z, EnumDirection direction) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.loc = new Location(world, x, y, z);
        this.facing = direction;
    }

    public FoundryFurnaceBlock(Location loc, EnumDirection direction) {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.loc = loc;
        this.facing = direction;
    }

    public void sendTo(Player player) {
        IBlockData blockData;
        if (state == State.PLACED) {
            blockData = Blocks.FURNACE.getBlockData().set(FACING, facing);
        } else if (state == State.MELTS) {
            blockData = Blocks.LIT_FURNACE.getBlockData().set(FACING, facing);

            /*NBTTagCompound compound = new NBTTagCompound();
            compound.setShort("BurnTime", (short) 400);

            WrapperPlayServerTileEntityData wrapper = new WrapperPlayServerTileEntityData();
            wrapper.setLocation(new BlockPosition(loc.toVector()));
            wrapper.setNbtData(NbtFactory.fromNMSCompound(compound));
            wrapper.sendPacket(player);*/
        }
        else {
            blockData = Blocks.AIR.getBlockData();
            player.sendMessage(ChatColor.RED + "Если ты это видишь, сообщи админу пж (косяк с печками в плавильне)");
        }
        player.sendBlockChange(loc, Block.getId(blockData.getBlock()), (byte)blockData.getBlock().toLegacyData(blockData));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
