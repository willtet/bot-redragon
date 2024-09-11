package dev.willtet.enumeration;

public enum RoleEnum {

    MOD("1231960719260651570"),
    MODASSISTENTE("1272986863417692252"),
    APRENDIZ("1231960763770605588"),
    ESCUDEIRO("1231960822226354236"),
    CAVALEIRO("1231960846058655855"),
    PALADINO("1231960892711899156"),
    MESTRE("1231960913922490390");

    private final String id;

    RoleEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
