package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupInviteCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupInviteCommand(SkyArena plugin) {
        setCommand("invite");
        setConsole(false);
        setPlayer(true);
        setLength(2);
        setUsage("/group invite <joueur>");
        setDescription("Permet d'inviter un joueur au groupe.");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (args.length < 2) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Merci de préciser un joueur !");
            return;
        }
        Player pI = Bukkit.getPlayer(args[1]);
        if (pI == null) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Joueur introuvable !");
            return;
        }

        Group group;
        if ((group = groupManager.getGroup(p.getUniqueId())) != null) {
            if (group.isOwner(p.getUniqueId())) {
                if (groupManager.hasGroup(pI.getUniqueId())) {
                    p.sendMessage(ChatUtils.ERROR_PREFIX + "Ce joueur est déjà dans un groupe !");
                    return;
                }
                groupManager.invitePlayer(pI, groupManager.getGroup(p.getUniqueId()));
            } else {
                p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'êtes pas le propriétaire du groupe !");
            }
        } else {
            p.sendMessage(ChatUtils.GROUP_PREFIX + "Groupe créé et joueur invité !");
            groupManager.invitePlayer(pI, groupManager.createGroup(p));
        }
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (groupManager.hasInvite(p.getUniqueId()) == null) {
                    if (args[1].isEmpty() || p.getName().startsWith(args[1])) {
                        players.add(p.getName());
                    }
                }
            }
            return players;
        }
        return Arrays.asList("");
    }
}
