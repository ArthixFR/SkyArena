package fr.arthix.skyarena.commands.group;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.groups.GroupManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class GroupJoinCommand extends CommandExecutor {

    private GroupManager groupManager;

    public GroupJoinCommand(SkyArena plugin) {
        setCommand("join");
        setConsole(false);
        setPlayer(true);
        setLength(1);
        setUsage("/group join");
        setDescription("Accepter l'invitation à un groupe.");
        groupManager = plugin.getGroupManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        groupManager.acceptInvite((Player) sender);
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, String[] args) {
        return Arrays.asList("");
    }
}
