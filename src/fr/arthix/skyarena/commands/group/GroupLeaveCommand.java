package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GroupLeaveCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupLeaveCommand(SkyArena plugin) {
        setCommand("leave");
        setConsole(false);
        setPlayer(true);
        setLength(1);
        setUsage("/group leave");
        setDescription("Permet de quitter le groupe.");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (groupManager.hasGroup(p.getUniqueId())) {
            Group group = groupManager.getGroup(p.getUniqueId());
            if (group.isOwner(p.getUniqueId())) {
                p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous ne pouvez pas quitter votre propre groupe !");
            } else {
                group.removeMember(p.getUniqueId());
                group.sendMessage(ChatUtils.GROUP_PREFIX + p.getName() + " a quitté le groupe !");
                p.sendMessage(ChatUtils.GROUP_PREFIX + "Vous avez quitté le groupe de " + Bukkit.getPlayer(group.getOwner()));
            }
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez pas de groupe !");
        }
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
