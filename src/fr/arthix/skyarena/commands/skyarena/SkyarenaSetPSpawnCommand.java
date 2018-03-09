package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkyarenaSetPSpawnCommand extends CommandExecutor {

    private ArenaManager arenaManager;

    public SkyarenaSetPSpawnCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("setplayerspawn");
        setLength(1);
        setPermission("skyarena.admin.setplayerspawn");
        setUsage("/skyarena setplayerspawn");
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Arena arena = arenaManager.getArena(p.getLocation());
        if (arena != null) {
            arena.addPlayerSpawn(p.getLocation());
            p.sendMessage(ChatUtils.PLUGIN_PREFIX + "Point de spawn de joueur défini avec succès !");
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous devez être dans une arène pour exécuter cette commande !");
        }
    }
}
