package datamodel;

import java.util.List;

public class Actor {
    private Integer actor_id;
    private String first_name;
    private String last_name;
    private List<Film> films;

    public Actor(Integer actor_id, String first_name, String last_name) {
        this.actor_id = actor_id;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    public Actor(String first_name, String last_name) {
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

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    @Override
    public String toString() {
        // TODO: al cambiar el atributo films por una lista de Film hay que arreglar este metodo

        return "TODO: al cambiar el atributo films por una lista de Film hay que arreglar este metodo";
    }
}
