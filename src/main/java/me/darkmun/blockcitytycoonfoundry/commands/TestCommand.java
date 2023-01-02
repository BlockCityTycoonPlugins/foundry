package me.darkmun.blockcitytycoonfoundry.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        Bukkit.getLogger().info(String.format("Args length: %s, sender instanceof player: %s", args.length, sender instanceof Player));
        if (sender instanceof Player) {
            Player player = (Player) sender;
            for (Player pl : player.getWorld().getEntitiesByClass(Player.class)) {
                if (pl.getName().equals("§e§lВудс")) {
                    Bukkit.getLogger().info("Name: " + pl.getName());
                    GameProfile skin02da38dc = new GameProfile(UUID.fromString("a78b31eb-d84e-4ce8-b26f-e5b4fc447fa7"), "skin02da38dc");
                    skin02da38dc.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY1MDM5MTI0MjgzNCwKICAicHJvZmlsZUlkIiA6ICJlMmVkYTM1YjMzZGU0M2UxOTVhZmRkNDgxNzQ4ZDlhOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJDaGFsa19SaWNlR0kiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiOTBhZmZlNWIzOWMzZDRlMTk1MmViMzVkZGVkN2FiZTQxMTAzZTBmZTdlOTgyMmVlYTEwNzVmYWQ0YWVmOSIKICAgIH0KICB9Cn0=", "wfUJhhyVvx+O2EJjVjYjvh966oI5ykhZDqJhXgc7sIa3tGW653g87d7RKnME7+U2QYhbXL90rE3juq0v0JAYdSfgWHTzLMarES2SVQIFngQ2xqymNSoXvDcvzWE0ZvEY+EaRamwEh28azPVkpVwY0EEowCqbJwb3y5Z9Q2i9j0P0JIfxiEclzOptaRyZY32tVKRSjrGLtQuMdH9kBNBP3PKNeyWSHC32R43nvrxZyIgJHWuN6iTX+KlBeTdpMscP1IsDIaJROmsQ9p3R6TbymDNRrtgb8zgi4aulKx2oeTrStPEgh1E7POySg89sffLir8dS/8DxWVfgTZiym55Tue2o2h46CSb0vGyWxFL31vKGuAOCsKqrWfPEQmWkPww32y4QjxY4dMjM+MBmx6u8KrZdUOTk+QyTAE2m6uJxeN9pF8k0H/I19EEiMI8D7mv2DXUtkJKYz6ZZ7YSL48bveAj4eYRXI8Eszy67O21WSC/W1f0tquIy3piK5d6xV302GuMMRZ+wDVpVVnlpzlh+xNnx/mBklITA75jf5fqW3rgtUUeB0ek5jpaHSn9Sqrbu5iCQ+mb9Hd+qj+H1Z3Lfjdtk7f3B3mQx/xNkiXbFnP+wnLfK2AJGE94dceh67bfNMcMe1pygUcKbNiemqnRcRN5vy0cr9wFXyzeICKOotpc="));

                    //SkinUtil.changeSkin(pl, skin02da38dc);
                }
            }
            //((CraftPlayer)player).getHandle().getProfile().getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY1MDM5MTI0MjgzNCwKICAicHJvZmlsZUlkIiA6ICJlMmVkYTM1YjMzZGU0M2UxOTVhZmRkNDgxNzQ4ZDlhOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJDaGFsa19SaWNlR0kiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiOTBhZmZlNWIzOWMzZDRlMTk1MmViMzVkZGVkN2FiZTQxMTAzZTBmZTdlOTgyMmVlYTEwNzVmYWQ0YWVmOSIKICAgIH0KICB9Cn0=", "wfUJhhyVvx+O2EJjVjYjvh966oI5ykhZDqJhXgc7sIa3tGW653g87d7RKnME7+U2QYhbXL90rE3juq0v0JAYdSfgWHTzLMarES2SVQIFngQ2xqymNSoXvDcvzWE0ZvEY+EaRamwEh28azPVkpVwY0EEowCqbJwb3y5Z9Q2i9j0P0JIfxiEclzOptaRyZY32tVKRSjrGLtQuMdH9kBNBP3PKNeyWSHC32R43nvrxZyIgJHWuN6iTX+KlBeTdpMscP1IsDIaJROmsQ9p3R6TbymDNRrtgb8zgi4aulKx2oeTrStPEgh1E7POySg89sffLir8dS/8DxWVfgTZiym55Tue2o2h46CSb0vGyWxFL31vKGuAOCsKqrWfPEQmWkPww32y4QjxY4dMjM+MBmx6u8KrZdUOTk+QyTAE2m6uJxeN9pF8k0H/I19EEiMI8D7mv2DXUtkJKYz6ZZ7YSL48bveAj4eYRXI8Eszy67O21WSC/W1f0tquIy3piK5d6xV302GuMMRZ+wDVpVVnlpzlh+xNnx/mBklITA75jf5fqW3rgtUUeB0ek5jpaHSn9Sqrbu5iCQ+mb9Hd+qj+H1Z3Lfjdtk7f3B3mQx/xNkiXbFnP+wnLfK2AJGE94dceh67bfNMcMe1pygUcKbNiemqnRcRN5vy0cr9wFXyzeICKOotpc="));

            //GameProfile skin02da38dc = new GameProfile(UUID.fromString("a78b31eb-d84e-4ce8-b26f-e5b4fc447fa7"), "skin02da38dc");
            //skin02da38dc.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTY1MDM5MTI0MjgzNCwKICAicHJvZmlsZUlkIiA6ICJlMmVkYTM1YjMzZGU0M2UxOTVhZmRkNDgxNzQ4ZDlhOSIsCiAgInByb2ZpbGVOYW1lIiA6ICJDaGFsa19SaWNlR0kiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNiOTBhZmZlNWIzOWMzZDRlMTk1MmViMzVkZGVkN2FiZTQxMTAzZTBmZTdlOTgyMmVlYTEwNzVmYWQ0YWVmOSIKICAgIH0KICB9Cn0=", "wfUJhhyVvx+O2EJjVjYjvh966oI5ykhZDqJhXgc7sIa3tGW653g87d7RKnME7+U2QYhbXL90rE3juq0v0JAYdSfgWHTzLMarES2SVQIFngQ2xqymNSoXvDcvzWE0ZvEY+EaRamwEh28azPVkpVwY0EEowCqbJwb3y5Z9Q2i9j0P0JIfxiEclzOptaRyZY32tVKRSjrGLtQuMdH9kBNBP3PKNeyWSHC32R43nvrxZyIgJHWuN6iTX+KlBeTdpMscP1IsDIaJROmsQ9p3R6TbymDNRrtgb8zgi4aulKx2oeTrStPEgh1E7POySg89sffLir8dS/8DxWVfgTZiym55Tue2o2h46CSb0vGyWxFL31vKGuAOCsKqrWfPEQmWkPww32y4QjxY4dMjM+MBmx6u8KrZdUOTk+QyTAE2m6uJxeN9pF8k0H/I19EEiMI8D7mv2DXUtkJKYz6ZZ7YSL48bveAj4eYRXI8Eszy67O21WSC/W1f0tquIy3piK5d6xV302GuMMRZ+wDVpVVnlpzlh+xNnx/mBklITA75jf5fqW3rgtUUeB0ek5jpaHSn9Sqrbu5iCQ+mb9Hd+qj+H1Z3Lfjdtk7f3B3mQx/xNkiXbFnP+wnLfK2AJGE94dceh67bfNMcMe1pygUcKbNiemqnRcRN5vy0cr9wFXyzeICKOotpc="));

            //Object object;

            /*if (((CraftServer)player.getServer()).getServer().R()) {
                object = new DemoPlayerInteractManager(((CraftServer)player.getServer()).getServer().getWorldServer(0));
            } else {
                object = new PlayerInteractManager(((CraftServer)player.getServer()).getServer().getWorldServer(0));
            }*/

            //EntityPlayer entityplayer1 = new EntityPlayer(((CraftServer)player.getServer()).getServer(), ((CraftServer)player.getServer()).getServer().worldServer[0], skin02da38dc, (PlayerInteractManager) object);
            //ntityplayer1.setPosition(-69, 40, 132);
            //((CraftServer)getServer()).getServer().worldServer[0].addEntity(entityplayer1);
        }

        return true;
    }
}
