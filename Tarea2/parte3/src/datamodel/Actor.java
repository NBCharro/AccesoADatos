package datamodel;

import java.util.List;

public class Actor {
    private Integer actor_id;
    private String first_name;
    private String last_name;

    private List<String> films;

    public Actor(Integer actor_id, String first_name, String last_name) {
        this.actor_id = actor_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public Integer getActor_id() {
        return actor_id;
    }

    public void setActor_id(Integer actor_id) {
        this.actor_id = actor_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }

    @Override
    public String toString() {
        // Parte 3. Actividad 8. Modificar el metodo toString para ver las peliculas del actor
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Actor #").append(actor_id).append(": ").append(first_name).append(" ").append(last_name).append(" [");

        if (films != null && !films.isEmpty()) {
            for (String film : films) {
                stringBuilder.append("\n\"").append(film).append("\",");
            }
            // Eliminar la coma adicional al final
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append("\n]");

        return stringBuilder.toString();
    }
}
