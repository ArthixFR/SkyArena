package fr.arthix.skyarena.groups;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Group {

    private UUID owner;
    private List<UUID> members;

    public Group(UUID owner, UUID... members) {
        this.owner = owner;
        this.members = new ArrayList<>(Arrays.asList(members));
    }

    public UUID getOwner() {
        return owner;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void addMember(UUID member) {
        if (!isMember(member)) this.members.add(member);
    }

    public void removeMember(UUID member) {
        if (isMember(member)) this.members.remove(member);
    }

    public boolean isOwner(UUID player) {
        return player.equals(this.owner);
    }

    public boolean isMember(UUID player) {
        return this.members.contains(player) || owner.equals(player);
    }

    public void sendMessage(String message) {
        Player player;
        for (UUID uuid : members) {
            if ((player = Bukkit.getPlayer(uuid)) != null) {
                player.sendMessage(message);
            }
        }
        if ((player = Bukkit.getPlayer(owner)) != null) {
            player.sendMessage(message);
        }
    }
}
