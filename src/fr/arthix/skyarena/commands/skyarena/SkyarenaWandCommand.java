package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SkyarenaWandCommand extends CommandExecutor {

    public SkyarenaWandCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("wand");
        setLength(1);
        setPermission("skyarena.admin.wand");
        setUsage("/skyarena wand");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        ItemStack is = new ItemStack(Material.IRON_AXE, 1, (byte)0);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§b§lSkyArena §7| §e§lHache de sélection");
        is.setItemMeta(im);
        p.getInventory().addItem(is);
    }
}
