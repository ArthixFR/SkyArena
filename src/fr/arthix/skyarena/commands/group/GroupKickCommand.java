package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import org.bukkit.Bukkit;
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
        if (!groupManager.hasGroup(p)) {
            p.sendMessage("Vous n'avez pas de groupe !");
            return;
        }

        Group group = groupManager.getGroup(p);

        if (group.isOwner(p.getUniqueId())) {
            if (args.length < 2) {
                p.sendMessage("Merci de préciser un joueur !");
                return;
            }
            Player pI = Bukkit.getPlayer(args[1]);
            if (pI == null) {
                p.sendMessage("Joueur introuvable !");
                return;
            }
            if (groupManager.hasGroup(pI)) {
                if (groupManager.getGroup(pI) == group) {
                    if (pI.getUniqueId() == p.getUniqueId()) {
                        p.sendMessage("Vous ne pouvez pas vous exclure !");
                        return;
                    }
                    group.removeMember(pI.getUniqueId());
                    pI.sendMessage(Bukkit.getPlayer(group.getOwner()).getName() + " vous a exclu du groupe !");
                    group.sendMessage(pI.getName() + " a été exclu du groupe !");
                } else {
                    p.sendMessage("Ce joueur n'est pas dans votre groupe !");
                }
            } else {
                p.sendMessage("Ce joueur n'est pas dans votre groupe !");
            }
        } else {
            p.sendMessage("Vous n'êtes pas le propriétaire du groupe !");
        }
        return;
    }
}
