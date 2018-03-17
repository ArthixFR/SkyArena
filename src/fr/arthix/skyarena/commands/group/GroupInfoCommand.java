package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
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
            p.sendMessage("§7§m-------§7 §b§lGroupe §7§m-------");
            p.sendMessage("§7- Leader : " + Bukkit.getOfflinePlayer(group.getOwner()).getName());
            List<UUID> members = group.getMembers();
            if (members.isEmpty()) {
                p.sendMessage("§7- Aucun membre !");
            } else {
                p.sendMessage("§7- Membres :");
                for (UUID m : members) {
                    p.sendMessage("§7  - " + Bukkit.getOfflinePlayer(m).getName());
                }
            }
            List<UUID> invites = groupManager.getInvitesByGroup(group);
            if (invites.isEmpty()) {
                p.sendMessage("§7- Aucune invitation !");
            } else {
                p.sendMessage("§7- Invitations :");
                for (UUID uuid : invites) {
                    p.sendMessage("§7  - " + Bukkit.getPlayer(uuid).getName());
                }
            }
        } else {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez aucun groupe !");
        }
    }
}
