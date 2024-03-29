package fr.arthix.skyarena.groups;

import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.titleapi.TitleAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

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

    public void sendTitle(String title, String subtitle) {
        Player player;
        for (UUID uuid : members) {
            if ((player = Bukkit.getPlayer(uuid)) != null) {
                TitleAPI.sendTitle(player, 10, 30, 10, title, subtitle);
            }
        }
        if ((player = Bukkit.getPlayer(owner)) != null) {
            TitleAPI.sendTitle(player, 10, 30, 10, title, subtitle);
        }
    }

    public List<UUID> hasKey(ArenaDifficulty arenaDifficulty) {
        LinkedHashSet<UUID> hashSet = new LinkedHashSet<>();
        List<UUID> noKey = new ArrayList<>();
        for (ItemStack is : Bukkit.getPlayer(owner).getInventory().getStorageContents()) {
            if (is != null && is.getType() != Material.AIR) {
                if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                    if (is.getItemMeta().getDisplayName().equals(arenaDifficulty.getItemKey().getItemMeta().getDisplayName()) && is.getType() == arenaDifficulty.getItemKey().getType()) {
                        if (noKey.contains(owner)) noKey.remove(owner); System.out.println("Owner have key");
                        break;
                    }
                }
            }
            System.out.println("add in list");
            if (hashSet.add(owner)) noKey.add(owner);
        }
        for (UUID uuid : members) {
            for (ItemStack is : Bukkit.getPlayer(uuid).getInventory().getStorageContents()) {
                if (is != null && is.getType() != Material.AIR) {
                    if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                        if (is.getItemMeta().getDisplayName().equals(arenaDifficulty.getItemKey().getItemMeta().getDisplayName()) && is.getType() == arenaDifficulty.getItemKey().getType()) {
                            if (noKey.contains(uuid)) noKey.remove(uuid); System.out.println("member have key");
                            break;
                        }
                    }
                }
                System.out.println("add in list");
                if (hashSet.add(uuid)) noKey.add(uuid);
            }

        }
        return noKey;
    }

    public void removeKey(ArenaDifficulty difficulty) {
        for (ItemStack is : Bukkit.getPlayer(owner).getInventory().getStorageContents()) {
            if (is != null && is.getType() != Material.AIR) {
                if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                    if (is.getItemMeta().getDisplayName().equals(difficulty.getItemKey().getItemMeta().getDisplayName()) && is.getType() == difficulty.getItemKey().getType()) {
                        is.setAmount(is.getAmount() - 1);
                        break;
                    }
                }
            }
        }
        for (UUID uuid : members) {
            for (ItemStack is : Bukkit.getPlayer(uuid).getInventory().getStorageContents()) {
                if (is != null && is.getType() != Material.AIR) {
                    if (is.hasItemMeta() && is.getItemMeta().hasDisplayName()) {
                        if (is.getItemMeta().getDisplayName().equals(difficulty.getItemKey().getItemMeta().getDisplayName()) && is.getType() == difficulty.getItemKey().getType()) {
                            is.setAmount(is.getAmount() - 1);
                            break;
                        }
                    }
                }
            }

        }
    }
}
