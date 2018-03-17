package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCreateCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupCreateCommand(SkyArena plugin) {
        setCommand("create");
        setConsole(false);
        setPlayer(true);
        setLength(1);
        setUsage("/group create");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (!groupManager.hasGroup(p.getUniqueId())) {
            groupManager.createGroup(p);
            p.sendMessage(ChatUtils.GROUP_PREFIX + "Groupe créée !");
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Tu fais déjà parti d'un groupe !");
        }
    }
}
