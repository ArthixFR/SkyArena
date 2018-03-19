package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class SkyarenaListCommand extends CommandExecutor {

    private ArenaManager arenaManager;

    public SkyarenaListCommand(SkyArena plugin) {
        setBoth();
        setCommand("list");
        setLength(1);
        setPermission("skyarena.admin.list");
        setUsage("/skyarena list");
        setDescription("Affiche toutes les arènes.");
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§7§m-------§7 §c§lArènes §7§m-------");
        for (Arena arena : arenaManager.getArenas()) {
            sender.sendMessage("- " + arena.getArenaName() + " §7-> " + arena.getArenaState().getName());
        }
        sender.sendMessage("§7§m-------§7 §c§lArènes §7§m-------");
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
