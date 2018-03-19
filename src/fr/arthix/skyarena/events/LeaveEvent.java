package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    private ArenaManager arenaManager;

    public LeaveEvent(SkyArena plugin) {
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Arena a = arenaManager.getArena(p);
        if (a != null) {
            a.removePlayer(p.getUniqueId());
            if (a.getPlayers().isEmpty() && a.getArenaState() != ArenaState.FREE && a.getArenaState() != ArenaState.FINISH) {
                arenaManager.stopArena(a, false);
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player p = e.getPlayer();
        Arena a = arenaManager.getArena(p);
        if (a != null) {
            a.removePlayer(p.getUniqueId());
            if (a.getPlayers().isEmpty() && a.getArenaState() != ArenaState.FREE && a.getArenaState() != ArenaState.FINISH) {
                arenaManager.stopArena(a, false);
            }
        }
    }
}
