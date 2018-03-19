package fr.arthix.skyarena.commands;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.group.*;
import fr.arthix.skyarena.commands.skyarena.*;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandHandler implements org.bukkit.command.CommandExecutor, TabCompleter {

    private HashMap<String, CommandExecutor> commands = new HashMap<>();

    public CommandHandler(String commandName, SkyArena plugin) {
        if (commandName.equalsIgnoreCase("group")) {
            commands.put("create", new GroupCreateCommand(plugin));
            commands.put("delete", new GroupDeleteCommand(plugin));
            commands.put("info", new GroupInfoCommand(plugin));
            commands.put("invite", new GroupInviteCommand(plugin));
            commands.put("join", new GroupJoinCommand(plugin));
            commands.put("kick", new GroupKickCommand(plugin));
            commands.put("leave", new GroupLeaveCommand(plugin));
            commands.put("refuse", new GroupRefuseCommand(plugin));
        } else if (commandName.equalsIgnoreCase("skyarena")) {
            commands.put("create", new SkyarenaCreateCommand(plugin));
            commands.put("wand", new SkyarenaWandCommand(plugin));
            commands.put("setplayerspawn", new SkyarenaSetPSpawnCommand(plugin));
            commands.put("setmobspawn", new SkyarenaSetMSpawnCommand(plugin));
            commands.put("setbossspawn", new SkyarenaSetBossSpawnCommand(plugin));
            commands.put("info", new SkyarenaInfoCommand(plugin));
            commands.put("gui", new SkyarenaGuiCommand(plugin));
            commands.put("give", new SkyarenaGiveCommand(plugin));
            commands.put("setspectator", new SkyarenaSetSpectatorCommand(plugin));
            commands.put("reload", new SkyarenaReloadCommand(plugin));
            commands.put("list", new SkyarenaListCommand(plugin));
            commands.put("stop", new SkyarenaStopCommand(plugin));
            commands.put("open", new SkyarenaOpenCommand(plugin));
            commands.put("close", new SkyarenaCloseCommand(plugin));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1) {
            if (!sender.hasPermission("skyarena.help") && command.getName().equalsIgnoreCase("skyarena")) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez pas la permission d'executer cette commande !");
                return true;
            }

            sender.sendMessage("§7§m-------§7 §a§lAide §7§m-------");
            for (Map.Entry<String, CommandExecutor> entry : commands.entrySet()) {
                if (entry.getValue().getPermission() == null) {
                    sender.sendMessage("§a" + entry.getValue().getUsage() + " §f: §7" + entry.getValue().getDescription());
                } else if (sender.hasPermission(entry.getValue().getPermission()) || sender instanceof ConsoleCommandSender) {
                    sender.sendMessage("§a" + entry.getValue().getUsage() + " §f: §7" + entry.getValue().getDescription());
                }
            }
            sender.sendMessage("§7§m-------§7 §a§lAide §7§m-------");

            return true;
        }
        String name = args[0];
        if (commands.containsKey(name)) {
            final CommandExecutor commandExecutor = commands.get(name);

            if (commandExecutor.getPermission() != null && !sender.hasPermission(commandExecutor.getPermission())) {
                sender.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez pas la permission d'executer cette commande !");
                return true;
            }

            if (!commandExecutor.isBoth()) {
                if (commandExecutor.isConsole() && sender instanceof Player) {
                    sender.sendMessage(ChatUtils.ERROR_PREFIX + "Cette commande ne s'execute que via la console !");
                    return true;
                }

                if (commandExecutor.isPlayer() && sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(ChatUtils.ERROR_PREFIX + "Cette commande ne s'execute que via un joueur !");
                    return true;
                }
            }

            if (commandExecutor.getLength() > args.length) {
                sender.sendMessage(ChatUtils.SKYARENA_PREFIX + "Utilisation : " + commandExecutor.getUsage());
                return true;
            }

            commandExecutor.execute(sender, args);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) {
            List<String> tabCommands = new ArrayList<>();
            for (Map.Entry<String, CommandExecutor> entry : commands.entrySet()) {
                if (entry.getValue().getPermission() == null || (sender.hasPermission(entry.getValue().getPermission()) || sender instanceof ConsoleCommandSender)) {
                    if (args[0].isEmpty() || entry.getKey().startsWith(args[0])) {
                        tabCommands.add(entry.getKey());
                    }
                }
            }
            return tabCommands;
        }

        String name = args[0];
        if (commands.containsKey(name)) {
            final CommandExecutor commandExecutor = commands.get(name);

            if (commandExecutor.getPermission() != null && !sender.hasPermission(commandExecutor.getPermission())) {
                return Arrays.asList("");
            }

            return commandExecutor.tabCompleter(sender, args);
        }
        return Arrays.asList("");
    }
}
