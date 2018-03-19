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

public class SkyarenaSetBossSpawnCommand extends CommandExecutor {
    private ArenaManager arenaManager;
    private ConfigManager configManager;

    public SkyarenaSetBossSpawnCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("setbossspawn");
        setLength(1);
        setPermission("skyarena.admin.setbossspawn");
        setUsage("/skyarena setbossspawn");
        setDescription("Ajoute un point de spawn pour le boss.");
        arenaManager = plugin.getArenaManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Arena arena = arenaManager.getArena(p.getLocation());
        if (arena != null) {
            arena.addBossSpawn(p.getLocation());
            configManager.setArenaConfig(arena, "boss", arena.getBossSpawn());
            p.sendMessage(ChatUtils.SKYARENA_PREFIX + "Point de spawn du boss défini avec succès !");
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous devez être dans une arène pour exécuter cette commande !");
        }
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
