package datamodel;

import persistence.PersistenceManager;

import java.sql.SQLException;
import java.util.List;

public class DataModelManager {
    private PersistenceManager persistenceManager;

    public DataModelManager(String urlDB, String user, String pass) {
        persistenceManager = new PersistenceManager(urlDB, user, pass);
    }

    public List<String> getFilmsForActor(int actor_id) {
        // No se si es correcto pero he decidido abrir y cerrar la conexion en esta capa, ya que me daba error al intentar cerrarlo en cada metodo.
        // Asi, puedo abrir la conexion, utilizar los metodos que quiera y al final del cerrar la conexion.
        List<String> filmsForActor = null;
        try {
            persistenceManager.open();
            filmsForActor = persistenceManager.getFilmsForActor(actor_id);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filmsForActor;
    }

    public List<Actor> getActorByName(String first_name) {
        // No se si es correcto pero he decidido abrir y cerrar la conexion en esta capa, ya que me daba error al intentar cerrarlo en cada metodo.
        // Asi, puedo abrir la conexion, utilizar los metodos que quiera y al final del cerrar la conexion.
        List<Actor> actorByName = null;
        try {
            persistenceManager.open();
            actorByName = persistenceManager.getActorByName(first_name);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actorByName;
    }

    public List<Film> getFilmsByKeyword(String palabraTitulo) {
        List<Film> FilmsByKeyword = null;
        try {
            persistenceManager.open();
            FilmsByKeyword = persistenceManager.getFilmsByKeyword(palabraTitulo);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FilmsByKeyword;
    }

    public List<Film> getFilmsByRatingAndCost(Rating rating, double replacementCost) {
        List<Film> FilmsByKeyword = null;
        try {
            persistenceManager.open();
            FilmsByKeyword = persistenceManager.getFilmsByRatingAndCost(rating, replacementCost);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return FilmsByKeyword;
    }

    public List<Actor> getActorsByAverageRentalRate(double minAverageRentalRate) {
        List<Actor> actorByName = null;
        try {
            persistenceManager.open();
            actorByName = persistenceManager.getActorsByAverageRentalRate(minAverageRentalRate);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actorByName;
    }

    public Actor getActorWithHighestAverageRentalRate() {
        Actor actorByName = null;
        try {
            persistenceManager.open();
            actorByName = persistenceManager.getActorWithHighestAverageRentalRate();
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actorByName;
    }

    public boolean existsActor(int actorId) {
        boolean existsActor = false;
        try {
            persistenceManager.open();
            existsActor = persistenceManager.existsActor(actorId);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existsActor;
    }

    public boolean existsFilm(int filmId) {
        boolean existsActor = false;
        try {
            persistenceManager.open();
            existsActor = persistenceManager.existsFilm(filmId);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existsActor;
    }

    public boolean existsById( int objectId, String idColumnName, String tableName) {
        boolean existsActor = false;
        try {
            persistenceManager.open();
            existsActor = persistenceManager.existsById( objectId, idColumnName, tableName);
            persistenceManager.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existsActor;
    }
}
