package datamodel;

public enum Rating {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17"),
    NULL(null);

    private static final String DEFAULT_VALUE = null;

    private final String value;

    private Rating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // Parte 3. Actividad 6. Implementa el metodo of
    public static Rating of(String input) {
        for (Rating rating : Rating.values()) {
            if ((input == null && rating.value == null) || (input != null && input.equals(rating.value))) {
                return rating;
            }
        }
        return NULL;
    }
}
