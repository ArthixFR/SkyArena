package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.GroupManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GroupRefuseCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupRefuseCommand(SkyArena plugin) {
        setCommand("refuse");
        setConsole(false);
        setPlayer(true);
        setLength(1);
        setUsage("/group refuse");
        setDescription("Refuse une invitation à un groupe.");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        groupManager.refuseInvite((Player) sender);
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
