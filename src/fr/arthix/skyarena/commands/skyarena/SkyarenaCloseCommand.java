package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkyarenaCloseCommand extends CommandExecutor {

    private ArenaManager arenaManager;

    public SkyarenaCloseCommand(SkyArena plugin) {
        setBoth();
        setCommand("stop");
        setLength(2);
        setPermission("skyarena.admin.close");
        setUsage("/skyarena close <arena/all> (difficulty)");
        setDescription("Ferme une ou plusieurs arènes.");
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args[1].equalsIgnoreCase("all")) {
            int i = 0;
            for (Arena a : arenaManager.getArenas()) {
                if (args.length == 3) {
                    if (a.getDifficulty() == ArenaDifficulty.getDifficultyByName(args[2]) && a.getArenaState() != ArenaState.CLOSE) {
                        arenaManager.resetArena(a);
                        a.setArenaState(ArenaState.CLOSE);
                        i++;
                    }
                } else {
                    if (a.getArenaState() != ArenaState.CLOSE) {
                        arenaManager.resetArena(a);
                        a.setArenaState(ArenaState.CLOSE);
                        i++;
                    }
                }
            }
            sender.sendMessage(i != 0 ? ChatUtils.SKYARENA_PREFIX + i + " arène" + (i == 1 ? "" : "s") + " fermée" + (i == 1 ? "" : "s") + " avec succès !" : ChatUtils.ERROR_PREFIX + "Tout les arènes sont déjà fermés !");
        } else {
            String arenaName = ChatUtils.argsToString(args, 1);
            Arena arena = arenaManager.getArena(arenaName);
            if (arena != null) {
                if (arena.getArenaState() != ArenaState.CLOSE) {
                    arenaManager.resetArena(arena);
                    arena.setArenaState(ArenaState.CLOSE);
                } else {
                    sender.sendMessage(ChatUtils.ERROR_PREFIX + "Arène déjà fermée !");
                    return;
                }
            } else {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + "Arène introuvable !");
                return;
            }
            sender.sendMessage(ChatUtils.SKYARENA_PREFIX + "Arène arrêtée avec succès !");
        }
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if (args.length >= 2 && !args[1].equalsIgnoreCase("all")) {
            List<String> arenaNames = new ArrayList<>();
            arenaNames.add("all");
            for (Arena a : arenaManager.getArenas()) {
                if (args[1].isEmpty() || a.getArenaName().startsWith(args[1])) {
                    if (a.getArenaState() != ArenaState.CLOSE) {
                        arenaNames.add(a.getArenaName());
                    }
                }
            }
            return arenaNames;
        }
        if (args.length == 3 && args[1].equalsIgnoreCase("all")) {
            return ArenaDifficulty.getDifficulties();
        }
        return Arrays.asList("");
    }
}
