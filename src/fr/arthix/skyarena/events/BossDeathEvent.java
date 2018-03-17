package fr.arthix.skyarena.events;

import fr.arthix.skyarena.SkyArena;
import fr.arthix.skyarena.arena.Arena;
import fr.arthix.skyarena.arena.ArenaManager;
import fr.arthix.skyarena.arena.ArenaState;
import fr.arthix.skyarena.rewards.Rewards;
import fr.arthix.skyarena.rewards.RewardsManager;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.itembox.main.ItemBox;

import java.util.UUID;

public class BossDeathEvent implements Listener {

    private MobManager mobManager;
    private ArenaManager arenaManager;
    private ItemBox itemBox;

    public BossDeathEvent(SkyArena plugin) {
        mobManager = plugin.getMythicMobs().getMobManager();
        arenaManager = plugin.getArenaManager();
        itemBox = plugin.getItemBox();
    }

    @EventHandler
    public void onBossDeath(MythicMobDeathEvent e) {
        AbstractLocation abstractLocation = e.getMob().getLocation();
        Location loc = new Location(Bukkit.getWorld(abstractLocation.getWorld().getName()), abstractLocation.getX(), abstractLocation.getY(), abstractLocation.getZ());
        Arena a = arenaManager.getArena(loc);
        if (a != null) {
            if (e.getMob().getType() == mobManager.getMythicMob(a.getBossName())) {
                a.setArenaState(ArenaState.FINISH);
                System.out.println("LE BOSS EST MORT");
                for (UUID uuid : a.getPlayers()) {
                    for (Rewards reward : a.getRewards(uuid)) {
                        System.out.println(Bukkit.getOfflinePlayer(uuid).getName() + " a gagné " + reward.getAmount() + "x" + reward.getName() + " (" + reward.getRarity().getName() + ")");
                        ItemStack is = new ItemStack(reward.getMaterial(), reward.getAmount(), reward.getMetadata());
                        itemBox.getPlayerDataManager().getOrLoadPlayerInfo(Bukkit.getOfflinePlayer(uuid)).addItem(is);
                    }
                }
                //arenaManager.stopArena();
            }
        }

    }
}
