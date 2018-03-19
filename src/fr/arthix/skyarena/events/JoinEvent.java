package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    private SkyArena plugin;
    private ArenaManager arenaManager;

    public JoinEvent(SkyArena plugin) {
        this.plugin = plugin;
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (arenaManager.isSpectate(p)) {
            arenaManager.setSpectator(p, arenaManager.getArena(p), false);
        }
        Arena a = arenaManager.getArena(p.getLocation());
        if (a != null) {
            if (!a.getPlayers().contains(p.getUniqueId())) {
                p.teleport(Bukkit.getWorld("world").getSpawnLocation());
            }
        }
    }
}
