package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkyarenaCreateCommand extends CommandExecutor {

    /*

    Etapes creation :
     - Définir la position 1 de l'arène -> /skyarena wand
     - Definir la position 2 de l'arène -> /skyarena wand
     - Définir le spawn des joueurs /skyarena setplayerspawn
     - Définir le spawn des mobs ? ou aléatoire ? /skyarena setmobspawn

     */

    // TODO: FAIRE LA COMMANDE /skyarena setplayerspawn & /skyarena setmobspawn

    private ArenaManager arenaManager;

    public SkyarenaCreateCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("create");
        setLength(5);
        setPermission("skyarena.admin.create");
        setUsage("/skyarena create <maxWaves> <bossname> <difficulty> <nom>");
        arenaManager = plugin.getArenaManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        int maxWaves = Integer.valueOf(args[1]);
        String bossname = args[2];
        String difficultyStr = args[3];
        ArenaDifficulty difficulty;
        String name = argsToString(args, 4);

        if (!arenaManager.arenaCreationLocation.containsKey(p.getUniqueId())) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous devez définir la zone de l'arène ! (/skyarena wand)");
            return;
        }

        if (arenaManager.arenaCreationLocation.get(p.getUniqueId()).size() != 2) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous devez définir la zone de l'arène ! (/skyarena wand)");
            return;
        }

        if (!(maxWaves > 0)) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Le nombre de vagues maximum est incorrect !");
            return;
        }

        // TODO: Check le bossname via mythicmobs

        switch (difficultyStr) {
            case "easy":
                difficulty = ArenaDifficulty.EASY;
                break;
            case "medium":
                difficulty = ArenaDifficulty.MEDIUM;
                break;
            case "hard":
                difficulty = ArenaDifficulty.HARD;
                break;
            case "extreme":
                difficulty = ArenaDifficulty.EXTREME;
                break;
            default:
                p.sendMessage(ChatUtils.ERROR_PREFIX + "La difficulté est incorrecte ! Disponible : easy, medium, hard, extreme");
                return;
        }

        //System.out.println(arenaManager.arenaCreationLocation.get(p.getUniqueId()).size() + " : " + arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(0) + ";" + arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(1));
        arenaManager.createArena(name, arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(0), arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(1), maxWaves, bossname, difficulty);
        arenaManager.arenaCreationLocation.remove(p.getUniqueId());
        p.sendMessage(ChatUtils.SKYARENA_PREFIX + "Arène créée avec succès ! N'oubliez pas de définir les spawn de joueurs et mobs avec §f/skyarena setplayerspawn §7& §f/skyarena setmobspawn §7!");

    }

    public static String argsToString(String[] args, int start) {
        String str = "";
        for (int i = start; i < args.length; i++) {
            String arg = "";
            if ((i + 1) != args.length) {
                arg = args[i] + " ";
            } else {
                arg = args[i];
            }
            str = str + arg;
        }
        return str;
    }
}
