package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupDeleteCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupDeleteCommand(SkyArena plugin) {
        setCommand("delete");
        setConsole(false);
        setPlayer(true);
        setLength(1);
        setUsage("/group delete");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (groupManager.hasGroup(p.getUniqueId())) {
            if (groupManager.getGroup(p.getUniqueId()).isOwner(p.getUniqueId())) {
                groupManager.removeGroup(groupManager.getGroup(p.getUniqueId()));
            } else {
                p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'êtes pas le propriétaire du groupe !");
            }
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez aucun groupe !");
        }
        return;
    }
}
