package fr.arthix.skyarena;

import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandHandler;
import fr.arthix.skyarena.config.ConfigManager;
import fr.arthix.skyarena.events.CreateArenaPositionEvent;
import fr.arthix.skyarena.events.InventoryEvent;
import fr.arthix.skyarena.events.MobDeathEvent;
import fr.arthix.skyarena.events.MoveEvent;
import fr.arthix.skyarena.groups.GroupManager;
import fr.arthix.skyarena.gui.GuiManager;
import io.lumine.xikage.mythicmobs.MythicMobs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SkyArena extends JavaPlugin {

    // TODO: MYSQL
    // TODO: BLOQUER LES COMMANDES QUAND UN JOUEUR EST DANS L'ARENE

    private SkyArena instance;
    private GroupManager groupManager;
    private ArenaManager arenaManager;
    private GuiManager guiManager;
    private ConfigManager configManager;
    private MythicMobs mythicMobs;

    public void onEnable() {
        getLogger().log(Level.INFO, "Loading SkyArena...");
        instance = this;

        if (Bukkit.getPluginManager().getPlugin("MythicMobs") == null) {
            getLogger().log(Level.SEVERE, "MythicMobs not found, disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            getLogger().log(Level.INFO, "Loading MythicMobs API...");
            mythicMobs = MythicMobs.inst();
        }

        getLogger().log(Level.INFO, "Loading group manager...");
        groupManager = new GroupManager();
        getLogger().log(Level.INFO, "Loading arena manager...");
        arenaManager = new ArenaManager(this);
        getLogger().log(Level.INFO, "Loading config manager...");
        configManager = new ConfigManager(this);
        getLogger().log(Level.INFO, "Loading gui manager...");
        guiManager = new GuiManager(this);

        getLogger().log(Level.INFO, "Loading arenas...");
        configManager.loadArenas();

        getLogger().log(Level.INFO, "Loading commands...");
        getCommand("group").setExecutor(new CommandHandler("group", this));
        getCommand("skyarena").setExecutor(new CommandHandler("skyarena", this));

        getLogger().log(Level.INFO, "Loading events...");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new CreateArenaPositionEvent(this), this);
        pm.registerEvents(new InventoryEvent(this), this);
        pm.registerEvents(new MobDeathEvent(this), this);
        pm.registerEvents(new MoveEvent(this), this);

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

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MythicMobs getMythicMobs() {
        return mythicMobs;
    }
}
