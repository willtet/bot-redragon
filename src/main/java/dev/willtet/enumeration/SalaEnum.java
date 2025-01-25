package dev.willtet.enumeration;

public enum SalaEnum {

    MISSAOEXTRA1("1326618028720525353"),
    MISSAOEXTRA2("1326618055135985728"),
    MISSAOEXTRA3("1326618080561991744"),
    MISSAOEXTRA4("1326618189416632333"),
    MISSAOEXTRA5("1326618213827739669"),

    MISSAODIARIOEXRA("1332021455281455184"),

    MODNOTIFICACAO("1284207912473989200");



    private final String id;

    SalaEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
