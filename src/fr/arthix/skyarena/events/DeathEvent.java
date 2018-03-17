package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import net.minecraft.server.v1_12_R1.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DeathEvent implements Listener {

    private ArenaManager arenaManager;
    private SkyArena plugin;
    private Map<UUID, List<ItemStack>> restoreKey = new HashMap<>();
    private Map<UUID, Arena> deathList = new HashMap<>();

    public DeathEvent(SkyArena plugin) {
        this.plugin = plugin;
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Arena a = arenaManager.getArena(p);
        if (a != null) {
            deathList.put(p.getUniqueId(), a);
            Iterator<ItemStack> drops = e.getDrops().iterator();
            List<ItemStack> restoreTemp = new ArrayList<>();
            ItemStack key = a.getDifficulty().getItemKey();
            if (!restoreKey.containsKey(p.getUniqueId())) {
                restoreKey.put(p.getUniqueId(), new ArrayList<>());
            }
            while (drops.hasNext()) {
                ItemStack is = drops.next();
                if (is.getType() == key.getType()) {
                    if (is.hasItemMeta()) {
                        if (is.getItemMeta().getDisplayName().equals(key.getItemMeta().getDisplayName())) {
                            drops.remove();
                            restoreTemp.add(is);
                        }
                    }
                }
            }
            restoreKey.replace(p.getUniqueId(), restoreTemp);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                PacketPlayInClientCommand packet = new PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN);
                CraftPlayer craftPlayer = (CraftPlayer) p;
                craftPlayer.getHandle().playerConnection.a(packet);
            }, 2L);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (restoreKey.containsKey(p.getUniqueId())) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (ItemStack is : restoreKey.get(p.getUniqueId())) {
                    p.getInventory().addItem(is);
                    p.updateInventory();
                }
                restoreKey.remove(p.getUniqueId());
            }, 1L);
        }
        if (deathList.containsKey(p.getUniqueId())) {
            Arena a = deathList.get(p.getUniqueId());
            a.setSpectate(p, true);
            List<Player> alive = a.getAlivePlayers();
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (alive.isEmpty()) {
                    a.setArenaState(ArenaState.FINISH);
                    System.out.println("TOUT LE MONDE EST MORT !");
                } else {
                    p.teleport(alive.get(0), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
            }, 2L);
            deathList.remove(p.getUniqueId());
        }
    }
}
