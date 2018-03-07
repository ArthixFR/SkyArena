package fr.arthix.skyarena;

import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class NazarenaCommand implements CommandExecutor {

    private GroupManager groupManager;

    public NazarenaCommand(SkyArena plugin) {
        plugin.getCommand("skyarena").setExecutor(this);
        groupManager = plugin.getGroupManager();
    }

    // TODO: MESSAGE CLIQUABLE POUR ACCEPTER / REFUSER
    // TODO: DESIGN DES MESSAGES
    // TODO: CHANGER LE NOM DES COMMANDES
    // TODO: AJOUTER UNE LIMITE PAR GROUPE

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args[0].equalsIgnoreCase("group")) {
                if (args[1].equalsIgnoreCase("create")) {
                    return groupCreate(p);
                } else if (args[1].equalsIgnoreCase("info")) {
                    return groupInfo(p);
                } else if (args[1].equalsIgnoreCase("delete")) {
                    return groupDelete(p);
                } else if (args[1].equalsIgnoreCase("invite")) {
                    return groupInvite(p, args);
                } else if (args[1].equalsIgnoreCase("join")) {
                    return groupJoin(p);
                } else if (args[1].equalsIgnoreCase("kick")) {
                    return groupKick(p, args);
                } else if (args[1].equalsIgnoreCase("leave")) {
                    return groupLeave(p);
                } else if (args[1].equalsIgnoreCase("refuse")) {
                    return groupRefuse(p);
                }
            }
        }
        return true;
    }

    private boolean groupCreate(Player p) {
        if (!groupManager.hasGroup(p)) {
            groupManager.createGroup(p);
            p.sendMessage("Groupe créée !");
        } else {
            p.sendMessage("Tu fais déjà parti d'un groupe !");
        }
        return true;
    }

    private boolean groupInfo(Player p) {
        if (groupManager.hasGroup(p)) {
            Group group = groupManager.getGroup(p);
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
        return true;
    }

    private boolean groupInvite(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage("Merci de préciser un joueur !");
            return true;
        }
        Player pI = Bukkit.getPlayer(args[2]);
        if (pI == null) {
            p.sendMessage("Joueur introuvable !");
            return true;
        }

        Group group;
        if ((group = groupManager.getGroup(p)) != null) {
            if (group.isOwner(p.getUniqueId())) {
                if (groupManager.hasGroup(pI)) {
                    p.sendMessage("Ce joueur est déjà dans un groupe !");
                    return true;
                }
                groupManager.invitePlayer(pI, groupManager.getGroup(p));
                return true;
            } else {
                p.sendMessage("Vous n'êtes pas le propriétaire du groupe !");
                return true;
            }
        } else {
            p.sendMessage("Groupe créée et joueur invité !");
            groupManager.invitePlayer(pI, groupManager.createGroup(p));
            return true;
        }
    }

    private boolean groupDelete(Player p) {
        if (groupManager.hasGroup(p)) {
            if (groupManager.getGroup(p).isOwner(p.getUniqueId())) {
                groupManager.removeGroup(groupManager.getGroup(p));
            } else {
                p.sendMessage("Vous n'êtes pas le propriétaire du groupe !");
            }
        } else {
            p.sendMessage("Vous n'avez aucun groupe !");
        }
        return true;
    }

    private boolean groupJoin(Player p) {
        groupManager.acceptInvite(p);
        return true;
    }

    private boolean groupRefuse(Player p) {
        groupManager.refuseInvite(p);
        return true;
    }

    private boolean groupLeave(Player p) {
        if (groupManager.hasGroup(p)) {
            Group group = groupManager.getGroup(p);
            if (group.isOwner(p.getUniqueId())) {
                p.sendMessage("Vous ne pouvez pas quitter votre propre groupe !");
            } else {
                group.removeMember(p.getUniqueId());
                group.sendMessage(p.getName() + " a quitté le groupe !");
                p.sendMessage("Vous avez quitté le groupe de " + Bukkit.getPlayer(group.getOwner()));
            }
        } else {
            p.sendMessage("Vous n'avez pas de groupe !");
        }
        return true;
    }

    private boolean groupKick(Player p, String[] args) {
        if (!groupManager.hasGroup(p)) {
            p.sendMessage("Vous n'avez pas de groupe !");
            return true;
        }

        Group group = groupManager.getGroup(p);

        if (group.isOwner(p.getUniqueId())) {
            if (args.length < 3) {
                p.sendMessage("Merci de préciser un joueur !");
                return true;
            }
            Player pI = Bukkit.getPlayer(args[2]);
            if (pI == null) {
                p.sendMessage("Joueur introuvable !");
                return true;
            }
            if (groupManager.hasGroup(pI)) {
                if (groupManager.getGroup(pI) == group) {
                    if (pI.getUniqueId() == p.getUniqueId()) {
                        p.sendMessage("Vous ne pouvez pas vous exclure !");
                        return true;
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
        return true;
    }
}
