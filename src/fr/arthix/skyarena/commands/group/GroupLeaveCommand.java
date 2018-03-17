package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupLeaveCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupLeaveCommand(SkyArena plugin) {
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
        return;
    }
}
