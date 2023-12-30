package persistence;

import datamodel.Actor;
import datamodel.Film;
import datamodel.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {
    private String urlDB;
    private String user;
    private String pass;
    private Connection connection = null;

    public PersistenceManager(String urlDB, String user, String pass) {
        this.urlDB = urlDB;
        this.user = user;
        this.pass = pass;
    }

    public Connection open() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(urlDB, user, pass);
            }
            return connection;
        } catch (SQLException e) {
            throw new SQLException("Error al abrir la conexión a la base de datos", e);
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection = null;
        }
    }

    public List<Actor> getActorByName(String first_name) {
        // Parte 3. Actividad 7. modificar el codigo del metodo getActor.
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT actor_id, first_name, last_name FROM actor WHERE first_name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, first_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int actorId = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Actor actor = new Actor(actorId, firstName, lastName);
                List<String> films = getFilmsForActor(actorId);
                actor.setFilms(films);

                actors.add(actor);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }

    public List<String> getFilmsForActor(int actor_id) {
        // Parte 3. Actividad 5. Definir un metodo que obtenga las peliculas de un determinado actor.
        List<String> films = new ArrayList<>();
        String sql = "SELECT film.title FROM film " +
                "JOIN film_actor ON film.film_id = film_actor.film_id " +
                "WHERE film_actor.actor_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, actor_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String title = resultSet.getString("title");
                films.add(title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }

    public List<Film> getFilmsByKeyword(String palabraTitulo) {
        // Parte 3. Actividad 9. Metodo que permite recuperar todas las peliculas que contengan una palabra en su titulo.
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film WHERE title LIKE ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + palabraTitulo + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                short filmId = resultSet.getShort("film_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                short releaseYear = resultSet.getShort("release_year");
                byte languageId = resultSet.getByte("language_id");
                // Parte 3. Actividad 19. Modifica el método getFilm de forma que el valor del atributo original_language sea null si así lo fue en la base de datos
                Integer originalLanguageId = resultSet.getInt("original_language_id");
                if (resultSet.wasNull()) {
                    // Setear a null si el valor es NULL en la base de datos
                    originalLanguageId = null;
                }
                byte rentalDuration = resultSet.getByte("rental_duration");
                double rentalRate = resultSet.getDouble("rental_rate");
                short length = resultSet.getShort("length");
                double replacementCost = resultSet.getDouble("replacement_cost");
                String rating = String.valueOf(Rating.of(resultSet.getString("rating")));
                String specialFeatures = resultSet.getString("special_features");
                Film film = new Film(filmId, title, description, releaseYear, languageId, originalLanguageId, rentalDuration, rentalRate, length, replacementCost, rating, specialFeatures);
                films.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }

    public List<Film> getFilmsByRatingAndCost(Rating rating, double replacementCost) {
        // Parte 3. Actividad 10. Metodo que permite recuperar todas las peliculas de un rating dado
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film WHERE rating = ? AND replacement_cost > ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, rating.getValue());
            preparedStatement.setDouble(2, replacementCost);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                short filmId = resultSet.getShort("film_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                short releaseYear = resultSet.getShort("release_year");
                byte languageId = resultSet.getByte("language_id");
                // Parte 3. Actividad 19. Modifica el método getFilm de forma que el valor del atributo original_language sea null si así lo fue en la base de datos
                Integer originalLanguageId = resultSet.getInt("original_language_id");
                if (resultSet.wasNull()) {
                    // Setear a null si el valor es NULL en la base de datos
                    originalLanguageId = null;
                }
                byte rentalDuration = resultSet.getByte("rental_duration");
                double rentalRate = resultSet.getDouble("rental_rate");
                short length = resultSet.getShort("length");
                double replacementCostDB = resultSet.getDouble("replacement_cost");
                String ratingDB = String.valueOf(Rating.of(resultSet.getString("rating")));
                String specialFeatures = resultSet.getString("special_features");
                Film film = new Film(filmId, title, description, releaseYear, languageId, originalLanguageId, rentalDuration, rentalRate, length, replacementCostDB, ratingDB, specialFeatures);
                films.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

    public List<Actor> getActorsByAverageRentalRate(double minAverageRentalRate) {
        // Parte 3. Actividad 11. Metodo que permite recuperar todos los actores con un rental_rate superior al valor dado
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT actor.actor_id, actor.first_name, actor.last_name " +
                "FROM actor JOIN film_actor ON actor.actor_id = film_actor.actor_id " +
                "JOIN film ON film_actor.film_id = film.film_id " +
                "GROUP BY actor.actor_id " +
                "HAVING AVG(film.rental_rate) > ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, minAverageRentalRate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int actorId = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Actor actor = new Actor(actorId, firstName, lastName);
                List<String> films = getFilmsForActor(actorId);
                actor.setFilms(films);
                actors.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return actors;
    }

    public Actor getActorWithHighestAverageRentalRate() {
        // Parte 3. Actividad 12. Metodo que permite recuperar el actor cuyas peliculas, de media, tengan un mayor rental_rate
        Actor actor = null;
        String sql = "SELECT actor.actor_id, actor.first_name, actor.last_name " +
                "FROM actor JOIN film_actor ON actor.actor_id = film_actor.actor_id " +
                "JOIN film ON film_actor.film_id = film.film_id " +
                "GROUP BY actor.actor_id " +
                "ORDER BY AVG(film.rental_rate) DESC " +
                "LIMIT 1";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int actorId = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                actor = new Actor(actorId, firstName, lastName);
                List<String> films = getFilmsForActor(actorId);
                actor.setFilms(films);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        return actor;
    }

    public boolean existsActor(int actorId) {
        // Parte 3. Actividad 15. Metodo que permite comprobar si existe un actor mediante un ID
        String sql = "SELECT actor_id FROM actor WHERE actor_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, actorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Devuelve true si hay al menos un resultado
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsFilm(int filmId) {
        // Parte 3. Actividad 16. Repite el ejercicio anterior, pero para comprobar la existencia de una película
        String sql = "SELECT film_id FROM film WHERE film_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, filmId);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Devuelve true si hay al menos un resultado
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsById(int objectId, String idColumnName, String tableName) {
        // Parte 3. Actividad 17. Implementa un método genérico que devuelva un booleano. El valor del booleano deberá ser true si existe al menos un objeto en una tabla dada
        String sql = "SELECT " + idColumnName + " FROM " + tableName + " WHERE " + idColumnName + " = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, objectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
