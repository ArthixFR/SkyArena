package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import fr.arthix.skyarena.groups.Group;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.titleapi.TitleAPI;
import fr.arthix.skyarena.utils.ChatUtils;
import fr.arthix.skyarena.utils.ItemFormat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

public class GuiArenas extends GuiBase {

    private SkyArena plugin;
    private GuiManager guiManager;
    private ArenaManager arenaManager;
    private GroupManager groupManager;
    private ItemFormat itemFormat;

    public GuiArenas(SkyArena plugin) {
        super(plugin);
        this.plugin = plugin;
        this.guiManager = plugin.getGuiManager();
        this.arenaManager = plugin.getArenaManager();
        this.groupManager = plugin.getGroupManager();
        this.itemFormat = new ItemFormat(plugin);
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
    public boolean showLeaveButton() {
        return true;
    }

    @Override
    public boolean refreshGui() {
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
    public void setContent(Inventory inv, Object... arg) {
        if (arg[0] instanceof ArenaDifficulty) {
            ArenaDifficulty arenaDifficulty = (ArenaDifficulty) arg[0];
            List<Arena> arenaList = arenaManager.getArenasByDifficulty(arenaDifficulty);
            int i = 10;
            for (Arena arena : arenaList) {
                if (((i + 1) % 9) == 0) {
                    i = i + 2;
                }
                ItemStack arenaItem = itemFormat.arenaItem(arena, inv, i);
                ItemStack item = inv.getItem(i);
                if (item == null) {
                    inv.setItem(i, itemFormat.arenaItem(arena, inv, i));
                } else if (!(arenaItem.getDurability() == item.getDurability() && arenaItem.getType() == item.getType() && item.getItemMeta().getLore().equals(arenaItem.getItemMeta().getLore()))) {
                    inv.setItem(i, itemFormat.arenaItem(arena, inv, i));
                }
                i++;
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
                if (group == null) {
                    group = groupManager.createGroup(p);
                }
                final Group groupF = group;
                //if (group != null) {
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
                    ArenaDifficulty difficulty = arena.getDifficulty();
                    List<UUID> noKey = group.hasKey(difficulty);
                    System.out.println(noKey);
                    if (noKey.isEmpty()) {
                        group.removeKey(difficulty);
                        arena.setArenaState(ArenaState.TELEPORTING);
                        p.closeInventory();
                        group.sendTitle("Téléportation dans", "5 secondes");
                        Bukkit.getScheduler().runTaskLater(plugin, () -> groupF.sendTitle("Téléportation dans", "4 secondes"), 20L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> groupF.sendTitle("Téléportation dans", "3 secondes"), 40L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> groupF.sendTitle("Téléportation dans", "2 secondes"), 60L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> groupF.sendTitle("Téléportation dans", "1 seconde"), 80L);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            groupF.sendTitle("Téléportation dans l'arène...", "");
                            arenaManager.addPlayerToArena(groupF.getOwner(), arena);
                            arenaManager.addPlayerToArena(groupF.getMembers(), arena);
                            arenaManager.startArena(arena);
                        }, 100L);
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
                        p.sendMessage(ChatUtils.ERROR_PREFIX + userNoKey + " ne possède" + (noKey.size() == 1 ? "" : "nt") + " pas la clé !");
                    }
            }
        }
    }
}
