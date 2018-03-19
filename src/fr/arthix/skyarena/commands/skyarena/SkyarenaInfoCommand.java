package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SkyarenaInfoCommand extends CommandExecutor {

    private ArenaManager arenaManager;

    public SkyarenaInfoCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("info");
        setLength(1);
        setPermission("skyarena.admin.info");
        setUsage("/skyarena info (arena)");
        setDescription("Affiche les informations d'une arène.");
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        Arena arena = arenaManager.getArena(p.getLocation());

        if (arena != null || args.length >= 2) {
            if (arena == null) arena = arenaManager.getArena(ChatUtils.argsToString(args, 1));
            if (arena == null) {
                p.sendMessage(ChatUtils.ERROR_PREFIX + "Arène introuvable !");
                return;
            }
            List<UUID> playersInArena = arena.getPlayers();
            ArenaState state = arena.getArenaState();
            Location pos1 = arena.getBorder().get(0);
            Location pos2 = arena.getBorder().get(1);
            p.sendMessage("§7§m-------§7 §6§lSkyArena §7§m-------");
            p.sendMessage("§7- Nom : " + arena.getArenaName());
            p.sendMessage("§7- Position :");
            p.sendMessage("§7  - Position 1 : X:§f" + pos1.getBlockX() + "§7 Y:§f" + pos1.getBlockY() + "§7 Z:§f" + pos1.getBlockZ());
            p.sendMessage("§7  - Position 2 : X:§f" + pos2.getBlockX() + "§7 Y:§f" + pos2.getBlockY() + "§7 Z:§f" + pos2.getBlockZ());
            p.sendMessage("§7- Spawn joueurs : " + arena.getPlayersSpawn().size());
            p.sendMessage("§7- Spawn mobs : " + arena.getMobsSpawn().size());
            p.sendMessage("§7- Spawn boss : " + arena.getBossSpawn().size());
            p.sendMessage("§7- État : " + arena.getArenaState().getName());
            p.sendMessage("§7- Difficulté : " + arena.getDifficulty().getName());
            p.sendMessage("§7- Vagues max : " + arena.getMaxWaves());
            p.sendMessage("§7- Boss : " + arena.getBossName());
            if (state != ArenaState.FREE) {
                p.sendMessage("§7- Joueurs dans l'arène :");
                for (UUID uuid : playersInArena) {
                    p.sendMessage("§7  - " + Bukkit.getPlayer(uuid).getName());
                }
            }
            p.sendMessage("§7§m-------§7 §6§lSkyArena §7§m-------");
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous devez être dans une arène pour exécuter cette commande !");
        }
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            List<String> arenaNames = new ArrayList<>();
            for (Arena a : arenaManager.getArenas()) {
                if (args[1].isEmpty() || a.getArenaName().startsWith(args[1])) {
                    arenaNames.add(a.getArenaName());
                }
            }
            return arenaNames;
        }
        return Arrays.asList("");
    }
}
