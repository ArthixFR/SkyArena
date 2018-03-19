package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.config.ConfigManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkyarenaSetSpectatorCommand extends CommandExecutor {

    private ArenaManager arenaManager;

    public SkyarenaSetSpectatorCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("setspectator");
        setLength(3);
        setPermission("skyarena.admin.setspectator");
        setUsage("/skyarena setspectator <player> <true/false>");
        setDescription("Met un joueur en spectateur ou non.");
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            sender.sendMessage(ChatUtils.ERROR_PREFIX + "Joueur introuvable !");
            return;
        }
        arenaManager.setSpectator(p, arenaManager.getArena(p), Boolean.valueOf(args[2]));
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> players = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach((p) -> players.add(p.getName()));
            return players;
        } else if (args.length == 3) {
            return Arrays.asList("true", "false");
        }
        return Arrays.asList("");
    }
}
