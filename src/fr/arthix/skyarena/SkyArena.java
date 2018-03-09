package fr.arthix.skyarena;

import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandHandler;
import fr.arthix.skyarena.events.CreateArenaPositionEvent;
import fr.arthix.skyarena.groups.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SkyArena extends JavaPlugin {

    // TODO: MYSQL

    private SkyArena instance;
    private GroupManager groupManager;
    private ArenaManager arenaManager;

    public void onEnable() {
        getLogger().log(Level.INFO, "Loading SkyArena...");
        instance = this;
        getLogger().log(Level.INFO, "Loading group manager...");
        groupManager = new GroupManager();
        getLogger().log(Level.INFO, "Loading arena manager...");
        arenaManager = new ArenaManager();

        getLogger().log(Level.INFO, "Loading commands...");
        getCommand("group").setExecutor(new CommandHandler("group", this));
        getCommand("skyarena").setExecutor(new CommandHandler("skyarena", this));

        getLogger().log(Level.INFO, "Loading events...");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CreateArenaPositionEvent(this), this);

        getLogger().log(Level.INFO, "SkyArena loaded !");
    }

    public void onDisable() {

    }

    public SkyArena getInstance() {
        return instance;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }
}
