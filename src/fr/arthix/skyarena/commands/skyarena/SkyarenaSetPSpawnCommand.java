package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.config.ConfigManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SkyarenaSetPSpawnCommand extends CommandExecutor {

    private ArenaManager arenaManager;
    private ConfigManager configManager;

    public SkyarenaSetPSpawnCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("setplayerspawn");
        setLength(1);
        setPermission("skyarena.admin.setplayerspawn");
        setUsage("/skyarena setplayerspawn");
        setDescription("Ajoute un point de spawn pour les joueurs.");
        arenaManager = plugin.getArenaManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Arena arena = arenaManager.getArena(p.getLocation());
        if (arena != null) {
            arena.addPlayerSpawn(p.getLocation());
            configManager.setArenaConfig(arena, "players", arena.getPlayersSpawn());
            p.sendMessage(ChatUtils.SKYARENA_PREFIX + "Point de spawn de joueur défini avec succès !");
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous devez être dans une arène pour exécuter cette commande !");
        }
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
