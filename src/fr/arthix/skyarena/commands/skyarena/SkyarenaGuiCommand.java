package fr.arthix.skyarena.commands.skyarena;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.gui.GuiArenas;
import fr.arthix.skyarena.gui.GuiManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SkyarenaGuiCommand extends CommandExecutor {

    GuiManager guiManager;

    public SkyarenaGuiCommand(SkyArena plugin) {
        setConsole(false);
        setPlayer(true);
        setCommand("gui");
        setLength(1);
        setPermission("skyarena.admin.gui");
        setUsage("/skyarena gui");
        setDescription("Ouvre le GUI.");
        guiManager = plugin.getGuiManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        guiManager.openGui(p, "main");
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
