package fr.arthix.skyarena.utils;

public class ChatUtils {
    public static String ERROR_PREFIX = "§c§lErreur §8§l>> §c";
    public static String SKYARENA_PREFIX = "§6§lSkyArena §8§l>> §7";
    public static String GROUP_PREFIX = "§b§lGroupe §8§l>> §7";

    public static String argsToString(String[] args, int start) {
        String str = "";
        for (int i = start; i < args.length; i++) {
            String arg = "";
            if ((i + 1) != args.length) {
                arg = args[i] + " ";
            } else {
                arg = args[i];
            }
            str = str + arg;
        }
        return str;
    }

    //TODO: FAIRE CHAT CLIQUABLE

    /*
            Refuse text

            TextComponent refuse = new TextComponent("[\u2718]");
            refuse.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skyarena group refuse"));
            refuse.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c§lRefuser").create()));
     */

    /*
            Accept text

            TextComponent accept = new TextComponent("[\u2713]");
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skyarena group join"));
            accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a§lAccepter").create()));

     */

    /*
            p.spigot().sendMessage(accept);
     */
}
