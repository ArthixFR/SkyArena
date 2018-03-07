package fr.arthix.skyarena;

import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandHandler;
import fr.arthix.skyarena.groups.GroupManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SkyArena extends JavaPlugin {

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

        getLogger().log(Level.INFO, "Adding command group...");
        getCommand("group").setExecutor(new CommandHandler("group", this));
        //getCommand("skyarena").setExecutor(new NazarenaCommand(this));

        //new GroupManager();
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
