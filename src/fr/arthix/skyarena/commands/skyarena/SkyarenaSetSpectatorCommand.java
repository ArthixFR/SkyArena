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

public class SkyarenaSetSpectatorCommand extends CommandExecutor {

    private ArenaManager arenaManager;
    private ConfigManager configManager;

    public SkyarenaSetSpectatorCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("setspectator");
        setLength(3);
        setPermission("skyarena.admin.setspectator");
        setUsage("/skyarena setspectator");
        arenaManager = plugin.getArenaManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            return;
        }
        Arena arena = arenaManager.getArena(p);
        if (arena != null) {
            arena.setSpectate(p, Boolean.valueOf(args[2]));
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Le joueur n'est pas dans une arène !");
        }
    }
}
