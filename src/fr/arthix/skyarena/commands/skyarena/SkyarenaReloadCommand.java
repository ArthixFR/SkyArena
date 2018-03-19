package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.config.ConfigManager;
import fr.arthix.skyarena.gui.GuiManager;
import fr.arthix.skyarena.rewards.RewardsManager;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkyarenaReloadCommand extends CommandExecutor {

    private ArenaManager arenaManager;
    private ConfigManager configManager;
    private GuiManager guiManager;
    private RewardsManager rewardsManager;

    public SkyarenaReloadCommand(SkyArena plugin) {
        setBoth();
        setCommand("reload");
        setLength(1);
        setPermission("skyarena.admin.reload");
        setUsage("/skyarena reload");
        setDescription("Reload les configurations du plugin.");
        arenaManager = plugin.getArenaManager();
        configManager = plugin.getConfigManager();
        guiManager = plugin.getGuiManager();
        rewardsManager = plugin.getRewardsManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Inventory inv = p.getOpenInventory().getTopInventory();
            if (inv != null) {
                if (guiManager.getGui(inv.getTitle()) != null) {
                    p.closeInventory();
                }
            }
        }
        arenaManager.setArenas(new ArrayList<>());
        rewardsManager.setRewardsList(new ArrayList<>());
        configManager.setArenaConfigs(new ArrayList<>());
        configManager.setRewardConfigs(new ArrayList<>());
        configManager.loadArenas();
        configManager.loadRewards();
        sender.sendMessage(ChatUtils.SKYARENA_PREFIX + "Rechargement des configurations !");
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
