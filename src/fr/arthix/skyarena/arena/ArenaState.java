package fr.arthix.skyarena.arena;

public enum ArenaState {
    FREE("Libre"),
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
