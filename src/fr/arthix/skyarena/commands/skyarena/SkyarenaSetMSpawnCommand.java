package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.config.ConfigManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SkyarenaSetMSpawnCommand extends CommandExecutor {

    private ArenaManager arenaManager;
    private ConfigManager configManager;

    public SkyarenaSetMSpawnCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("setmobspawn");
        setLength(1);
        setPermission("skyarena.admin.setmobspawn");
        setUsage("/skyarena setmobspawn");
        arenaManager = plugin.getArenaManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Arena arena = arenaManager.getArena(p.getLocation());
        if (arena != null) {
            arena.addMobsSpawn(p.getLocation());
            configManager.setConfig(arena, "mobs", arena.getMobsSpawn());
            p.sendMessage(ChatUtils.SKYARENA_PREFIX + "Point de spawn de mob défini avec succès !");
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous devez être dans une arène pour exécuter cette commande !");
        }
    }
}
