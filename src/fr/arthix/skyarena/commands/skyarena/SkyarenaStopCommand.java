package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkyarenaStopCommand extends CommandExecutor {

    private ArenaManager arenaManager;

    public SkyarenaStopCommand(SkyArena plugin) {
        setBoth();
        setCommand("stop");
        setLength(2);
        setPermission("skyarena.admin.stop");
        setUsage("/skyarena stop <arena>");
        setDescription("Stop une arène.");
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String arenaName = ChatUtils.argsToString(args, 1);
        Arena arena = arenaManager.getArena(arenaName);

        if (arena != null) {
            arenaManager.stopArena(arena, false);
            sender.sendMessage(ChatUtils.SKYARENA_PREFIX + "Arène arrêtée avec succès !");
        } else {
            sender.sendMessage(ChatUtils.ERROR_PREFIX + "Arène introuvable !");
        }
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            List<String> arenaNames = new ArrayList<>();
            for (Arena a : arenaManager.getArenas()) {
                arenaNames.add(a.getArenaName());
            }
            return arenaNames;
        }
        return Arrays.asList("");
    }
}
