package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommandEvent implements Listener {

    private List<String> commands = new ArrayList<>();
    private ArenaManager arenaManager;

    public PlayerCommandEvent(SkyArena plugin) { // TODO: AJOUTER UNE CONFIG POUR LES COMMANDES AUTORISÉES
        commands.add("/r");
        commands.add("/msg");

        arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String command = e.getMessage();
        Arena a = arenaManager.getArena(p);

        if (p.hasPermission("skyarena.bypass")) return;
        if (a == null) return;

        for (String s : commands) {
            if (s.equalsIgnoreCase(command)) {
                return;
            }
        }
        p.sendMessage(ChatUtils.ERROR_PREFIX + "Vous ne pouvez pas exécuter cette commande dans une arène !");
        e.setCancelled(true);
    }
}
