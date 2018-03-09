package fr.arthix.skyarena.arena;

public enum ArenaDifficulty {
    EASY("Facile"),
    MEDIUM("Moyen"),
    HARD("Difficile"),
    EXTREME("Extrème");

    private String name;

    private ArenaDifficulty(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
