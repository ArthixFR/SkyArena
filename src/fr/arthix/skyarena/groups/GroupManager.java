package fr.arthix.skyarena.groups;

import fr.arthix.skyarena.utils.ChatUtils;
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
            g.sendMessage(ChatUtils.GROUP_PREFIX + "Votre groupe a été supprimé!");
        }
    }

    public Group getGroup(UUID uuid) {
        for (Group g : groups) {
            if (g.isMember(uuid)) {
                return g;
            }
        }
        return null;
    }

    public Group hasInvite(UUID uuid) {
        for (Map.Entry<UUID, Group> invite : invites.entrySet()) {
            if (invite.getKey().equals(uuid)) {
                return invite.getValue();
            }
        }
        return null;
        //Map.Entry<UUID, Group> invite = invites.entrySet().stream().findFirst().filter((g) -> g.getKey().equals(uuid)).orElse(null);
        //return invite != null ? invite.getValue() : null;
    }

    public void invitePlayer(Player p, Group g) {
        Player owner = Bukkit.getPlayer(g.getOwner());
        if (hasInvite(p.getUniqueId()) != null) {
            owner.sendMessage(ChatUtils.ERROR_PREFIX + p.getName() + " a déjà été invité dans un groupe !");
        } else {
            invites.put(p.getUniqueId(), g);
            g.sendMessage(ChatUtils.GROUP_PREFIX + p.getName() + " a été invité dans le groupe !");
            p.sendMessage(ChatUtils.GROUP_PREFIX + Bukkit.getPlayer(g.getOwner()).getName() + " vous a invité a rejoindre son groupe !");
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
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez pas d'invitation !");
            return;
        }
        if (hasGroup(p.getUniqueId())) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous êtes déjà dans un groupe !");
            return;
        }
        invites.remove(p.getUniqueId());
        groupInvited.sendMessage(ChatUtils.GROUP_PREFIX + p.getName() + " a rejoint le groupe !");
        groupInvited.addMember(p.getUniqueId());
        p.sendMessage(ChatUtils.GROUP_PREFIX + "Vous avez rejoint le groupe de " + Bukkit.getPlayer(groupInvited.getOwner()).getName() + " !");
    }

    public void refuseInvite(Player p) {
        Group groupInvited = hasInvite(p.getUniqueId());
        if (groupInvited == null) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous n'avez pas d'invitation !");
            return;
        }
        if (hasGroup(p.getUniqueId())) {
            p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous êtes déjà dans un groupe !");
            return;
        }
        invites.remove(p.getUniqueId());
        Player owner = Bukkit.getPlayer(groupInvited.getOwner());
        owner.sendMessage(ChatUtils.GROUP_PREFIX + p.getName() + " a refusé votre invitation !");
        p.sendMessage(ChatUtils.GROUP_PREFIX + "Vous avez refusé l'invitation du groupe de " + owner.getName());
    }
}
