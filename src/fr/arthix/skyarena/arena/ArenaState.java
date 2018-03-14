package fr.arthix.skyarena.arena;

public enum ArenaState {
    CLOSE("Fermé"),
    FREE("Libre"),
    STARTING("Lancement..."),
    IN_PROGRESS("En cours"),
    BOSS("Boss"),
    FINISH("Terminé");

    private String name;

    private ArenaState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
