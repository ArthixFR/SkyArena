package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.config.ConfigManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SkyarenaCreateCommand extends CommandExecutor {

    private ArenaManager arenaManager;
    private ConfigManager configManager;

    public SkyarenaCreateCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("create");
        setLength(5);
        setPermission("skyarena.admin.create");
        setUsage("/skyarena create <maxWaves> <bossname> <difficulty> <nom>");
        setDescription("Permet de créer une arène.");
        arenaManager = plugin.getArenaManager();
        configManager = plugin.getConfigManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        int maxWaves = Integer.valueOf(args[1]);
        String bossname = args[2];
        String difficultyStr = args[3];
        ArenaDifficulty difficulty = null;
        String name = ChatUtils.argsToString(args, 4);

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

        for (String str : ArenaDifficulty.getDifficulties()) {
            if (difficultyStr.equals(str)) {
                difficulty = ArenaDifficulty.getDifficultyByName(str);
            }
        }

        if (difficulty == null) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "La difficulté est incorrecte ! Disponible : " + ArenaDifficulty.getDifficulties().toString().replaceAll("\\[", "").replaceAll("]",""));
            return;
        }

        /*switch (difficultyStr) {
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
        }*/

        if (arenaManager.getArena(name) != null) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Ce nom est déjà utilisé !");
            return;
        }

        //System.out.println(arenaManager.arenaCreationLocation.get(p.getUniqueId()).size() + " : " + arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(0) + ";" + arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(1));
        configManager.createArenaConfig(name, arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(0), arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(1), maxWaves, bossname, difficulty);
        arenaManager.createArena(name, arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(0), arenaManager.arenaCreationLocation.get(p.getUniqueId()).get(1), maxWaves, bossname, difficulty);
        arenaManager.arenaCreationLocation.remove(p.getUniqueId());
        p.sendMessage(ChatUtils.SKYARENA_PREFIX + "Arène créée avec succès ! N'oubliez pas de définir les spawn de joueurs et mobs avec §f/skyarena setplayerspawn §7& §f/skyarena setmobspawn §7!");
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if (args.length == 4) {
            return ArenaDifficulty.getDifficulties();
        }
        return Arrays.asList("");
    }
}
