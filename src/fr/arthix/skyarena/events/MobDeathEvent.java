package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobDeathEvent implements Listener {

    private SkyArena plugin;
    private ArenaManager arenaManager;

    public MobDeathEvent(SkyArena plugin) {
        this.plugin = plugin;
        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            Arena arena = arenaManager.getArena(e.getEntity().getLocation());
            if (arena != null) {
                arena.setMobWave(arena.getMobWave() - 1);
                if (arena.getMobWave() == 0) {
                    arenaManager.startWave(arena);
                }
            }
        }
    }
}
