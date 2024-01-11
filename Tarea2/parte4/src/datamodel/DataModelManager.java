package datamodel;

import persistence.PersistenceManager;

import java.sql.SQLException;
import java.util.List;

public class DataModelManager {
    private PersistenceManager persistenceManager;

    public DataModelManager() {
        persistenceManager = new PersistenceManager();
    }

    public boolean newActor(Actor actor) {
        try {
            Boolean actorGuardadoDB = persistenceManager.newActor(actor);
            return actorGuardadoDB;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean newFilm(Film film) {
        try {
            Boolean filmGuardadoDB = persistenceManager.newFilm(film);
            return filmGuardadoDB;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Film> getFilmsForActor(int actor_id) {
        // No se si es correcto pero he decidido abrir y cerrar la conexion en esta capa, ya que me daba error al intentar cerrarlo en cada metodo.
        // Asi, puedo abrir la conexion, utilizar los metodos que quiera y al final del cerrar la conexion.
        List<Film> filmsForActor = null;
        try {
            filmsForActor = persistenceManager.getFilmsForActor(actor_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filmsForActor;
    }

    public List<Actor> getActorByName(String first_name) {
        // No se si es correcto pero he decidido abrir y cerrar la conexion en esta capa, ya que me daba error al intentar cerrarlo en cada metodo.
        // Asi, puedo abrir la conexion, utilizar los metodos que quiera y al final del cerrar la conexion.
        List<Actor> actorByName = null;
        try {
            actorByName = persistenceManager.getActorByName(first_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actorByName;
    }

    public List<Film> getFilmsByKeyword(String palabraTitulo) {
        List<Film> FilmsByKeyword = null;
        try {
            FilmsByKeyword = persistenceManager.getFilmsByKeyword(palabraTitulo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FilmsByKeyword;
    }

    public List<Film> getFilmsByRatingAndCost(Rating rating, double replacementCost) {
        List<Film> FilmsByKeyword = null;
        try {
            FilmsByKeyword = persistenceManager.getFilmsByRatingAndCost(rating, replacementCost);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FilmsByKeyword;
    }

    public List<Actor> getActorsByAverageRentalRate(double minAverageRentalRate) {
        List<Actor> actorByName = null;
        try {
            actorByName = persistenceManager.getActorsByAverageRentalRate(minAverageRentalRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actorByName;
    }

    public Actor getActorWithHighestAverageRentalRate() {
        Actor actorByName = null;
        try {
            actorByName = persistenceManager.getActorWithHighestAverageRentalRate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actorByName;
    }

    public boolean existsActor(int actorId) {
        boolean existsActor = false;
        try {
            existsActor = persistenceManager.existsActor(actorId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existsActor;
    }

    public boolean existsFilm(int filmId) {
        boolean existsActor = false;
        try {
            existsActor = persistenceManager.existsFilm(filmId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existsActor;
    }

    public boolean existsById( int objectId, String idColumnName, String tableName) {
        boolean existsActor = false;
        try {
            existsActor = persistenceManager.existsById( objectId, idColumnName, tableName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existsActor;
    }
}
