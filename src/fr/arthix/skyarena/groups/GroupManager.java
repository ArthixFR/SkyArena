package fr.arthix.skyarena.groups;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;

public final class GroupManager {

    private List<Group> groups = new ArrayList<>();
    private Map<UUID, Group> invites = new HashMap<>();

    public boolean hasGroup(UUID uuid) {
        return groups.stream().findFirst().filter((g) -> g.isMember(uuid)).isPresent();
    }

    public Group createGroup(Player p) {
        Group group = new Group(p.getUniqueId());
        groups.add(group);
        return group;
    }

    public void removeGroup(Group g) {
        if (groups.contains(g)) {
            groups.remove(g);
            for (UUID uuid : getInvitesByGroup(g)) {
                invites.remove(uuid);
            }
            g.sendMessage("Votre groupe a été supprimé!");
        }
    }

    public Group getGroup(UUID uuid) {
        return groups.stream().findFirst().filter((g) -> g.isMember(uuid)).orElse(null);
    }

    public Group hasInvite(UUID uuid) {
        Map.Entry<UUID, Group> invite = invites.entrySet().stream().findFirst().filter((g) -> g.getKey().equals(uuid)).orElse(null);
        return invite != null ? invite.getValue() : null;
    }

    public void invitePlayer(Player p, Group g) {
        Player owner = Bukkit.getPlayer(g.getOwner());
        if (hasInvite(p.getUniqueId()) != null) {
            owner.sendMessage(p.getName() + " a déjà été invité dans un groupe !");
        } else {
            invites.put(p.getUniqueId(), g);
            g.sendMessage(p.getName() + " a été invité dans le groupe !");
            p.sendMessage(Bukkit.getPlayer(g.getOwner()).getName() + " vous a invité a rejoindre son groupe !");
        }
    }

    public List<UUID> getInvitesByGroup(Group g) {
        List<UUID> invitesByGroup = new ArrayList<>();

        Iterator it = invites.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<UUID, Group> pair = (Map.Entry<UUID, Group>) it.next();
            if (pair.getValue().equals(g)) {
                invitesByGroup.add(pair.getKey());
            }
        }

        return invitesByGroup;
    }

    public void acceptInvite(Player p) {
        Group groupInvited = hasInvite(p.getUniqueId());
        if (groupInvited == null) {
            p.sendMessage("Vous n'avez pas d'invitation !");
            return;
        }
        if (hasGroup(p.getUniqueId())) {
            p.sendMessage("Vous êtes déjà dans un groupe !");
            return;
        }
        invites.remove(p.getUniqueId());
        groupInvited.sendMessage(p.getName() + " a rejoint le groupe !");
        groupInvited.addMember(p.getUniqueId());
        p.sendMessage("Vous avez rejoint le groupe de " + Bukkit.getPlayer(groupInvited.getOwner()).getName() + " !");
    }

    public void refuseInvite(Player p) {
        Group groupInvited = hasInvite(p.getUniqueId());
        if (groupInvited == null) {
            p.sendMessage("Vous n'avez pas d'invitation !");
            return;
        }
        if (hasGroup(p.getUniqueId())) {
            p.sendMessage("Vous êtes déjà dans un groupe !");
            return;
        }
        invites.remove(p.getUniqueId());
        Player owner = Bukkit.getPlayer(groupInvited.getOwner());
        owner.sendMessage(p.getName() + " a refusé votre invitation !");
        p.sendMessage("Vous avez refusé l'invitation du groupe de " + owner.getName());
    }
}
