package me.darkmun.blockcitytycoonfoundry;

import com.comphenix.packetwrapper.*;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.darkmun.blockcitytycoonfoundry.storages.Configs;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static me.darkmun.blockcitytycoonfoundry.storages.Configs.mainConfig;
import static me.darkmun.blockcitytycoonfoundry.storages.Configs.playersFurnacesDataConfig;
import static net.minecraft.server.v1_12_R1.BlockFurnace.FACING;

public class FoundryFurnaceBlock {

    public enum State {
        NOT_PLACED,
        PLACED_EMPTY,
        PLACED_MELTING,
        PLACED_MELTED
    }
    public static final long TICKS_PER_SECONDS = 20;
    public static final long MELTING_TIME = Configs.mainConfig.getLong("melting-time");
    private final String name;
    private final int x;
    private final int y;
    private final int z;
    private final int messageMeltedEntityId;
    private final Location loc;
    private final EnumDirection facing;
    private State state = State.NOT_PLACED;
    private Material meltingBlock = null;
    private ItemStack meltedOut = null;
    private Task meltingTask;
    private boolean sending = false;

    public FoundryFurnaceBlock(World world, int x, int y, int z, int messageMeltedEntityId, EnumDirection direction, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.messageMeltedEntityId = messageMeltedEntityId;
        this.loc = new Location(world, x, y, z);
        this.facing = direction;
        this.name = name;
    }

    /*public FoundryFurnaceBlock(Location loc, EnumDirection direction) {
        this.x = loc.getBlockX();
        this.y = loc.getBlockY();
        this.z = loc.getBlockZ();
        this.loc = loc;
        this.facing = direction;
    }*/



    public void startMeltingTo(Player player, ItemStack item) {
        state = State.PLACED_MELTING;
        playersFurnacesDataConfig.getConfig().set(String.format("%s.furnaces.%s.state", player.getUniqueId(), name), state.name());
        playersFurnacesDataConfig.saveConfig();

        meltingBlock = item.getType();
        item.setAmount(item.getAmount() - 1);

        meltingTask = new Task(() -> {  // не нравится так конечно делать (каждый раз создавать новый таск, хотя он один и тот же), но в классе блока содержать поле с игроком, мне больше не нравится
            String messageAfterMelting = mainConfig.getString("melted-message");
            try {
                meltedOut = convertMeltingBlockToMelted();
            } catch (WrongBlockException e) {
                e.printStackTrace();
                meltedOut = null;
                messageAfterMelting = mainConfig.getString("melted-error-message");
            }
            meltingBlock = null;
            state = State.PLACED_MELTED;

            meltingTask.stop();
            Player pl = Bukkit.getPlayerExact(player.getName());
            if (pl != null) {  // по сути такого не может быть
                sendTo(pl);
                sendMeltedMessageToPlayer(pl, messageAfterMelting);
            }
            playersFurnacesDataConfig.getConfig().set(String.format("%s.furnaces.%s.state", player.getUniqueId(), name), state.name());
            playersFurnacesDataConfig.saveConfig();
        }, TICKS_PER_SECONDS * MELTING_TIME);

        sendTo(player);
        meltingTask.run();
    }

    public void giveMeltedOutTo(Player player) {
        boolean inventoryFilled = true;
        for (ItemStack itemStack : player.getInventory().getStorageContents()) {
            if (itemStack == null) {
                inventoryFilled = false;
                break;
            }
        }

        if (inventoryFilled) {
            player.sendMessage(ChatColor.GOLD + "Ваш инвентарь переполнен! Продайте блоки из шахты или слитки/гемы");
        } else {
            if (meltedOut != null) {
                if (!player.hasPermission("deluxemenus." + meltedOut.getType() + "_1")) {
                    BlockCityTycoonFoundry.permission.playerAdd(null, player, "deluxemenus." + meltedOut.getType() + "_1");
                    BlockCityTycoonFoundry.permission.playerAdd(null, player, "deluxemenus." + meltedOut.getType() + "_BUY_4");
                }
                player.getInventory().addItem(meltedOut);
                meltedOut = null;

            }
            state = State.PLACED_EMPTY;
            playersFurnacesDataConfig.getConfig().set(String.format("%s.furnaces.%s.state", player.getUniqueId(), name), state.name());
            playersFurnacesDataConfig.saveConfig();
            removeMessageMeltedTo(player);
        }
    }

    @SuppressWarnings("deprecation")
    public void sendTo(Player player) {
        IBlockData blockData;
        if (state == State.PLACED_EMPTY || state == State.PLACED_MELTED) {
            blockData = Blocks.FURNACE.getBlockData().set(FACING, facing);
        } else if (state == State.PLACED_MELTING) {
            blockData = Blocks.LIT_FURNACE.getBlockData().set(FACING, facing);
        } else {
            blockData = Blocks.AIR.getBlockData();
            player.sendMessage(ChatColor.RED + "Если ты это видишь, сообщи админу пж (косяк с печками в плавильне)");
        }
        sending = true;
        player.sendBlockChange(loc, Block.getId(blockData.getBlock()), (byte)blockData.getBlock().toLegacyData(blockData));
        sending = false;
    }

    private void sendMeltedMessageToPlayer(Player player, String message) { //destroy entity нужно



        WrapperPlayServerSpawnEntityLiving wrapper = new WrapperPlayServerSpawnEntityLiving();
        wrapper.setEntityID(messageMeltedEntityId);
        wrapper.setX(getMessageMeltedX());
        wrapper.setY(getMessageMeltedY());
        wrapper.setZ(getMessageMeltedZ());
        wrapper.setType(EntityType.ARMOR_STAND);

        byte invisibleBitMask = 1 << 5;
        //byte smallBitMask = 1;
        WrappedDataWatcher dataWatcher = new WrappedDataWatcher();
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(0, WrappedDataWatcher.Registry.get(Byte.class)), invisibleBitMask);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.get(String.class)), message);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(3, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(4, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(5, WrappedDataWatcher.Registry.get(Boolean.class)), true);
        //dataWatcher.setObject(new WrappedDataWatcher.WrappedDataWatcherObject(11, WrappedDataWatcher.Registry.get(Byte.class)), smallBitMask);

        wrapper.setMetadata(dataWatcher);
        wrapper.sendPacket(player);
    }

    private void removeMessageMeltedTo(Player player) {
        WrapperPlayServerEntityDestroy wrapperDestroyEntity = new WrapperPlayServerEntityDestroy();
        wrapperDestroyEntity.setEntityIds(new int[] {messageMeltedEntityId}); // можно создать поле в классе, а не создавать массив каждый раз
        wrapperDestroyEntity.sendPacket(player);
    }

    private double getMessageMeltedX() {
        if (facing == EnumDirection.SOUTH || facing == EnumDirection.NORTH) {
            return x + 0.5;
        } else if (facing == EnumDirection.WEST) {
            return x - 0.5;
        } else if (facing == EnumDirection.EAST) {
            return x + 1.5;
        } else {
            return x;
        }
    }

    private double getMessageMeltedY() {
        return y - 1.7;
    }

    private double getMessageMeltedZ() {
        if (facing == EnumDirection.EAST || facing == EnumDirection.WEST) {
            return z + 0.5;
        } else if (facing == EnumDirection.SOUTH){
            return z + 1.5;
        } else if (facing == EnumDirection.NORTH){
            return z - 0.5;
        } else {
            return z;
        }
    }

    public ItemStack convertMeltingBlockToMelted() throws WrongBlockException {
        switch (meltingBlock) {
            case IRON_ORE:
                return new ItemStack(Material.IRON_INGOT);
            case IRON_BLOCK:
                return new ItemStack(Material.IRON_INGOT, 9);
            case GOLD_ORE:
                return new ItemStack(Material.GOLD_INGOT);
            case GOLD_BLOCK:
                return new ItemStack(Material.GOLD_INGOT, 9);
            case DIAMOND_ORE:
                return new ItemStack(Material.DIAMOND);
            case DIAMOND_BLOCK:
                return new ItemStack(Material.DIAMOND, 9);
            case EMERALD_ORE:
                return new ItemStack(Material.EMERALD);
            case EMERALD_BLOCK:
                return new ItemStack(Material.EMERALD, 9);
            default:
                throw new WrongBlockException("Block " + meltingBlock.name() + " can't be melted");
        }
    }

    public static boolean isMeltable(Material material) {
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
    }

    public void pauseMeltingTask() {
        if (meltingTask != null) {
            meltingTask.pause();
        }
    }

    public void continueMeltingTask() {
        if (meltingTask != null) {
            meltingTask.continueTask();
        }
    }

    public boolean isSending() {
        return sending;
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

    public Location getLocation() {
        return loc;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

}
