package datamodel;

public enum Rating {
    G("G"),
    PG("PG"),
    PG_13("PG-13"),
    R("R"),
    NC_17("NC-17"),
    NULL(null);

    private final String value;
    // Valor por defecto
    private static final String DEFAULT_VALUE = null;

    // Constructor privado para asignar el valor
    private Rating(String value) {
        this.value = value;
    }
    // Método estático para obtener el valor por defecto
    public static String getDefaultValue() {
        return DEFAULT_VALUE;
    }

    // Método estático para obtener un objeto Rating desde un String
    public static Rating of(String input) {
        for (Rating rating : Rating.values()) {
            if ((input == null && rating.value == null) || (input != null && input.equals(rating.value))) {
                return rating;
            }
        }
        return NULL;  // Valor por defecto si no se encuentra ninguna coincidencia
    }

    // Método para obtener los valores de los atributos
    public String getValue() {
        return value;
    }


}
