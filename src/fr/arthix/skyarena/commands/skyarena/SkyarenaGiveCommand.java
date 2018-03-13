package fr.arthix.skyarena.commands.skyarena;


import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.ArenaDifficulty;
import fr.arthix.skyarena.commands.CommandExecutor;
import fr.arthix.skyarena.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkyarenaGiveCommand extends CommandExecutor {

    public SkyarenaGiveCommand(SkyArena plugin) {
        setConsole(true);
        setPlayer(true);
        setCommand("give");
        setLength(4);
        setPermission("skyarena.admin.give");
        setUsage("/skyarena give <player> <difficulty> <quantity>");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String difficultyStr = args[2];
        ArenaDifficulty difficulty;
        Player pI = Bukkit.getPlayer(args[1]);
        int quantity;
        try {
            quantity = Integer.valueOf(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(ChatUtils.ERROR_PREFIX + "La quantité doit être un chiffre !");
            return;
        }

        if (pI == null) {
            sender.sendMessage(ChatUtils.ERROR_PREFIX + "Joueur introuvable !");
            return;
        }

        switch (difficultyStr) {
            case "easy":
                difficulty = ArenaDifficulty.EASY;
                break;
            case "medium":
                difficulty = ArenaDifficulty.MEDIUM;
                break;
            case "hard":
                difficulty = ArenaDifficulty.HARD;
                break;
            case "extreme":
                difficulty = ArenaDifficulty.EXTREME;
                break;
            default:
                sender.sendMessage(ChatUtils.ERROR_PREFIX + "La difficulté est incorrecte ! Disponible : easy, medium, hard, extreme");
                return;
        }

        if (quantity <= 0) {
            sender.sendMessage(ChatUtils.ERROR_PREFIX + "La quantité doit être supérieure à 0 !");
            return;
        }

        List<ItemStack> matchItems = new ArrayList<>();
        ItemStack key = difficulty.getItemKey();
        for (ItemStack is : pI.getInventory().getStorageContents()) {
            if (is != null) {
                if (is.hasItemMeta()) {
                    if (is.getItemMeta().hasDisplayName()) {
                        if (is.getItemMeta().getDisplayName().equals(key.getItemMeta().getDisplayName()) && is.getType() == key.getType()) {
                            matchItems.add(is);
                        }
                    }
                }
            }
        }
        for (ItemStack is : matchItems) {
            if ((is.getAmount() + quantity) <= 64) {
                addKey(difficulty, quantity, pI);
                return;
            }
        }

        if (pI.getInventory().firstEmpty() != -1) {
            addKey(difficulty, quantity, pI);
            return;
        }

        sender.sendMessage(ChatUtils.ERROR_PREFIX + "Le joueur n'a pas assez de place dans son inventaire !");
    }

    private void addKey(ArenaDifficulty difficulty, int quantity, Player pI) {
        ItemStack is = difficulty.getItemKey();
        is.setAmount(quantity);
        pI.getInventory().addItem(difficulty.getItemKey());
        pI.sendMessage(ChatUtils.SKYARENA_PREFIX + "Vous avez reçu " + quantity + " clé" + (quantity == 1 ? "" : "s") + " " + difficulty.getItemKey().getItemMeta().getDisplayName() + " §7!");
    }
}
