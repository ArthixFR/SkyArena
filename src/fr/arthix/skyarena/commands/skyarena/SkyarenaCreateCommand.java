package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.GroupManager;
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

    private GroupManager groupManager; // USELESS
    private ArenaManager arenaManager; // USELESS

    public SkyarenaCreateCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("create");
        setLength(5);
        setPermission("skyarena.admin.create");
        setUsage("/skyarena create <maxWaves> <bossname> <difficulty> <nom>");
        groupManager = plugin.getGroupManager(); // USELESS
        arenaManager = plugin.getArenaManager(); // USELESS
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        int maxWaves = Integer.valueOf(args[1]);
        String bossname = args[2];
        String difficultyStr = args[3];
        ArenaDifficulty difficulty;
        String name = argsToString(args, 5);

        if (SkyArena.arenaCreationLocation.size() != 2) {
            p.sendMessage("Vous devez définir la zone de l'arène ! (/skyarena wand)");
            return;
        }

        if (!(maxWaves > 0)) {
            p.sendMessage("Le nombre de vagues maximum est incorrect !");
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
                p.sendMessage("La difficulté est incorrecte ! Disponible : easy, medium, hard, extreme");
                return;
        }

        // TODO: CREER L'ARENE

    }

    public static String argsToString(String[] args, int start) {
        String str = "";
        for (int i = start; i < args.length; i++) {
            String arg = "";
            if ((i - 1) != args.length) {
                arg = args[i] + " ";
            } else {
                arg = args[i];
            }
            str = str + arg;
        }
        return str;
    }
}
