package fr.arthix.skyarena.commands;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.group.*;
import fr.arthix.skyarena.commands.skyarena.*;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandHandler implements org.bukkit.command.CommandExecutor {

    private HashMap<String, CommandExecutor> commands = new HashMap<>();

    // TODO: AJOUTER LA COMMANDE "/skyarena setbossspawn"
    // TODO: AUTO-COMPLETE

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
            commands.put("info", new SkyarenaInfoCommand(plugin));
            commands.put("gui", new SkyarenaGuiCommand(plugin));
            commands.put("give", new SkyarenaGiveCommand(plugin));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1) {
            if (command.getName().equalsIgnoreCase("group")) {
                sender.sendMessage(ChatUtils.SKYARENA_PREFIX + "Utilisation : /group <create/delete/info/invite/join/kick/leave/refuse>");
            } else if (command.getName().equalsIgnoreCase("skyarena")) {
                sender.sendMessage(ChatUtils.SKYARENA_PREFIX + "Utilisation : /skyarena <create/wand/setplayerspawn/setmobspawn/info/give>");
            }
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
}
