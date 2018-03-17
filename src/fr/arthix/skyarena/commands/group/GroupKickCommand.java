package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupKickCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupKickCommand(SkyArena plugin) {
        setCommand("kick");
        setConsole(false);
        setPlayer(true);
        setLength(2);
        setUsage("/group kick <joueur>");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (!groupManager.hasGroup(p.getUniqueId())) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez pas de groupe !");
            return;
        }

        Group group = groupManager.getGroup(p.getUniqueId());

        if (group.isOwner(p.getUniqueId())) {
            if (args.length < 2) {
                p.sendMessage(ChatUtils.ERROR_PREFIX + "Merci de préciser un joueur !");
                return;
            }
            OfflinePlayer pOff = Bukkit.getOfflinePlayer(args[1]);
            if (pOff == null) {
                p.sendMessage(ChatUtils.ERROR_PREFIX + "Joueur introuvable !");
                return;
            }
            if (groupManager.hasGroup(pOff.getUniqueId())) {
                if (groupManager.getGroup(pOff.getUniqueId()) == group) {
                    if (pOff.getUniqueId() == p.getUniqueId()) {
                        p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous ne pouvez pas vous exclure !");
                        return;
                    }
                    group.removeMember(pOff.getUniqueId());
                    if (pOff.isOnline()) {
                        pOff.getPlayer().sendMessage(ChatUtils.GROUP_PREFIX + Bukkit.getPlayer(group.getOwner()).getName() + " vous a exclu du groupe !");
                    }
                    group.sendMessage(ChatUtils.GROUP_PREFIX + pOff.getName() + " a été exclu du groupe !");
                } else {
                    p.sendMessage(ChatUtils.ERROR_PREFIX + "Ce joueur n'est pas dans votre groupe !");
                }
            } else {
                p.sendMessage(ChatUtils.ERROR_PREFIX + "Ce joueur n'est pas dans votre groupe !");
            }
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'êtes pas le propriétaire du groupe !");
        }
    }
}
