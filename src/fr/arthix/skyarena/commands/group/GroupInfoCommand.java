package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class GroupInfoCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupInfoCommand(SkyArena plugin) {
        setCommand("info");
        setConsole(false);
        setPlayer(true);
        setLength(1);
        setUsage("/group info");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (groupManager.hasGroup(p.getUniqueId())) {
            Group group = groupManager.getGroup(p.getUniqueId());
            p.sendMessage("Owner : " + Bukkit.getOfflinePlayer(group.getOwner()).getName());
            List<UUID> members = group.getMembers();
            if (members.isEmpty()) {
                p.sendMessage("Aucun membre !");
            } else {
                p.sendMessage("Membres :");
                for (UUID m : members) {
                    p.sendMessage("- " + Bukkit.getOfflinePlayer(m).getName());
                }
            }
            List<UUID> invites = groupManager.getInvitesByGroup(group);
            if (invites.isEmpty()) {
                p.sendMessage("Aucune invitation dans le groupe !");
            } else {
                p.sendMessage("Invitations :");
                for (UUID uuid : invites) {
                    p.sendMessage("- " + Bukkit.getPlayer(uuid).getName());
                }
            }
        } else {
            p.sendMessage("Vous n'avez aucun groupe !");
        }
    }
}
