package fr.arthix.skyarena.gui;

import fr.arthix.skyarena.SkyArena;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class GuiManager {

    public List<GuiBase> guis = new ArrayList<>();

    public GuiManager(SkyArena plugin) {
        guis.add(new GuiMain(this));
        guis.add(new GuiSelectArena(this));
        guis.add(new GuiArenas(plugin));
    }

    public void openGui(Player p, String name, Object arg) {
        for (GuiBase guiBase : guis) {
            if (guiBase.name().equalsIgnoreCase(name)) {
                guiBase.openGui(p, arg);
                return;
            }
        }
    }

    public GuiBase getGui(String title) {
        for (GuiBase guiBase : guis) {
            if (guiBase.title().equalsIgnoreCase(title)) {
                return guiBase;
            }
        }
        return null;
    }

    public GuiBase getGuiByName(String name) {
        for (GuiBase guiBase : guis) {
            if (guiBase.name().equalsIgnoreCase(name)) {
                return guiBase;
            }
        }
        return null;
    }
}
