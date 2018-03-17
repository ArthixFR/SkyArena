package fr.arthix.skyarena.arena;

public enum ArenaState {
    CLOSE("Fermé"),
    FREE("Libre"),
    TELEPORTING("Téléportation des joueurs..."),
    STARTING("Lancement..."),
    IN_PROGRESS("En cours"),
    SELECTING_REWARD("Sélection de la récompense"),
    BOSS("Boss"),
    FINISH("Terminé");

    private String name;

    ArenaState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
