package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.utils.ChatUtils;
import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class GuiArenas extends GuiBase {

    private GuiManager guiManager;
    private ArenaManager arenaManager;
    private GroupManager groupManager;

    public GuiArenas(GuiManager guiManager, ArenaManager arenaManager, GroupManager groupManager) {
        this.guiManager = guiManager;
        this.arenaManager = arenaManager;
        this.groupManager = groupManager;
    }

    @Override
    public int size() {
        return 5 * 9;
    }

    @Override
    public boolean showReturnButton() {
        return true;
    }

    @Override
    public String title() {
        return "Arènes disponibles";
    }

    @Override
    public String name() {
        return "arenas";
    }

    @Override
    public String returnGui() {
        return "arena_select";
    }

    @Override
    public void setContent(Inventory inv, Object arg) {
        if (arg instanceof ArenaDifficulty) {
            ArenaDifficulty arenaDifficulty = (ArenaDifficulty) arg;
            List<Arena> arenaList = arenaManager.getArenasByDifficulty(arenaDifficulty);
            for (Arena arena : arenaList) {
                inv.addItem(ItemFormat.arenaItem(arena));
            }
        }
    }

    @Override
    public void interact(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack is = e.getCurrentItem();

        Arena arena = arenaManager.getArena(is.getItemMeta().getDisplayName());
        if (arena != null) {
            if (arena.getArenaState() == ArenaState.FREE) {
                Group group = groupManager.getGroup(p.getUniqueId());
                if (group != null) {
                    if (Bukkit.getPlayer(group.getOwner()) == null) {
                        p.sendMessage(ChatUtils.ERROR_PREFIX + Bukkit.getOfflinePlayer(group.getOwner()).getName() + " est déconnecté !");
                        return;
                    }

                    for (UUID uuid : group.getMembers()) {
                        if (Bukkit.getPlayer(uuid) == null) {
                            p.sendMessage(ChatUtils.ERROR_PREFIX + Bukkit.getOfflinePlayer(uuid).getName() + " est déconnecté !");
                            return;
                        }
                    }
                    List<UUID> noKey = group.hasKey(arena.getDifficulty());
                    if (noKey.isEmpty()) {
                        System.out.println("All users have key !");
                    } else {
                        String userNoKey = "";
                        for (UUID uuid : noKey) {
                            if (noKey.size() == 1) {
                                userNoKey = Bukkit.getOfflinePlayer(uuid).getName();
                            } else if (uuid.equals(noKey.get(noKey.size() - 1))) {
                                userNoKey = userNoKey + Bukkit.getOfflinePlayer(uuid).getName();
                            } else if (uuid.equals(noKey.get(noKey.size() - 2))) {
                                userNoKey = userNoKey + Bukkit.getOfflinePlayer(uuid).getName() + " et ";
                            } else {
                                userNoKey = userNoKey + Bukkit.getOfflinePlayer(uuid).getName() + ", ";
                            }
                        }
                        System.out.println(userNoKey + " ne possède" + (noKey.size() == 1 ? "" : "nt") + " pas la clé !");
                    }
                } else {
                    for (ItemStack i : p.getInventory().getStorageContents()) {
                        if (i != null && i.getType() != Material.AIR) {
                            if (i.hasItemMeta()) {
                                if (i.getItemMeta().getDisplayName().equals(arena.getDifficulty().getItemKey().getItemMeta().getDisplayName()) && i.getType() == arena.getDifficulty().getItemKey().getType()) {
                                    System.out.println("Player have key !");
                                    return;
                                }
                            }
                        }
                    }
                    System.out.println("player have no key");
                }
            }
        }
    }
}
