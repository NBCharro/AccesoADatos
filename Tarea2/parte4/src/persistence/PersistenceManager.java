package persistence;

import datamodel.Actor;
import datamodel.Film;
import datamodel.Rating;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {
    private String urlDB;
    private String user;
    private String pass;
    private Connection connection = null;
    private Long storedCode;

    public PersistenceManager() {
        MyConnection myConnection = new MyConnection();
        this.urlDB = myConnection.getUrl();
        this.user = myConnection.getUsername();
        this.pass = myConnection.getPassword();
        this.connection = myConnection.getConnection();
    }

    public PersistenceManager(Long storedCode) {
        MyConnection myConnection = new MyConnection();
        this.urlDB = myConnection.getUrl();
        this.user = myConnection.getUsername();
        this.pass = myConnection.getPassword();
        this.connection = myConnection.getConnection();
        this.storedCode = storedCode;
    }

    public Connection open() throws SQLException {
        return open(false, null);
    }

    public Connection open(boolean contextoTransaccional, Long providedCode) throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                // Verificar si el código proporcionado coincide con el código almacenado
                if (storedCode == null || storedCode.equals(providedCode)) {
                    connection = DriverManager.getConnection(urlDB, user, pass);
                    connection.setAutoCommit(contextoTransaccional);
                } else {
                    throw new SQLException("Código de apertura incorrecto");
                }
            }
            return connection;
        } catch (SQLException e) {
            throw new SQLException("Error al abrir la conexión a la base de datos", e);
        }
    }

    public void close(Long providedCode) {
        try {
            if (connection != null && !connection.isClosed()) {
                // Verificar si el código proporcionado coincide con el código almacenado
                if (storedCode == null || storedCode.equals(providedCode)) {
                    connection.close();
                    connection = null;
                } else {
                    System.out.println("Código de cierre incorrecto. No se cierra la conexión.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean newActor(Actor actor) {
        // Parte 4. Actividad 3. Implementa un método que te permita incluir un nuevo Actor en la base de datos
        String sql = "INSERT INTO actor (first_name, last_name) VALUES (?, ?)";

        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, actor.getFirst_name());
            preparedStatement.setString(2, actor.getLast_name());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int actorId = generatedKeys.getInt(1);
                        actor.setActor_id(actorId);
                    } else {
                        throw new SQLException("La inserción no ha devuelto ninguna clave generada.");
                    }
                }
                for (Film film : actor.getFilms()) {
                    List<Film> peliculas = getFilmsByKeyword(film.getTitle());
                    if (peliculas.size() == 0) {
                        newFilm(film);
                    }
                    // Parte 4. Actividad 6. Modifica el método newActor de forma que se introduzcan los valores del nuevo
                    //Actor y de todas sus Film en las que haya participado
                    String filmActorInsertSql = "INSERT INTO film_actor (actor_id, film_id) VALUES (?, ?)";
                    PreparedStatement filmActorPreparedStatement = connection.prepareStatement(filmActorInsertSql);
                    filmActorPreparedStatement.setInt(1, actor.getActor_id());
                    filmActorPreparedStatement.setInt(2, film.getFilmId());
                    filmActorPreparedStatement.executeUpdate();
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(storedCode);
        }
    }

    public boolean newFilm(Film film) {
        // Parte 4. Actividad 4. Implementa un método que te permita incluir una nueva pelicula en la base de datos
        String sql = "INSERT INTO film (title, description, release_year, language_id, " +
                "original_language_id, rental_duration, rental_rate, length, " +
                "replacement_cost, rating, special_features) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, film.getTitle());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setShort(3, film.getReleaseYear());
            preparedStatement.setByte(4, film.getLanguageId());

            if (film.getOriginalLanguageId() != null) {
                preparedStatement.setInt(5, film.getOriginalLanguageId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.INTEGER);
            }

            preparedStatement.setByte(6, film.getRentalDuration());
            preparedStatement.setDouble(7, film.getRentalRate());
            preparedStatement.setShort(8, film.getLength());
            preparedStatement.setDouble(9, film.getReplacementCost());
            preparedStatement.setString(10, film.getRating().toString());
            preparedStatement.setString(11, film.getSpecialFeatures());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        film.setFilmId(generatedKeys.getShort(1));
                    } else {
                        throw new SQLException("La inserción no ha devuelto ninguna clave generada.");
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(storedCode);
        }
    }

    public List<Actor> getActorByName(String first_name) {
        // Parte 3. Actividad 7. modificar el codigo del metodo getActor.
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT actor_id, first_name, last_name FROM actor WHERE first_name = ?";
        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, first_name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int actorId = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Actor actor = new Actor(actorId, firstName, lastName);
                List<Film> films = getFilmsForActor(actorId);
                actor.setFilms(films);

                actors.add(actor);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(storedCode);
        }
        return actors;
    }

    public List<Film> getFilmsForActor(int actor_id) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT film.* FROM film " +
                "JOIN film_actor ON film.film_id = film_actor.film_id " +
                "WHERE film_actor.actor_id = ?";
        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, actor_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Short film_id = resultSet.getShort("film_id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                Short release_year = resultSet.getShort("release_year");
                Byte language_id = resultSet.getByte("language_id");
                Integer original_language_id = resultSet.getInt("original_language_id");
                Byte rental_duration = resultSet.getByte("rental_duration");
                Double rental_rate = resultSet.getDouble("rental_rate");
                Short length = resultSet.getShort("length");
                Double replacement_cost = resultSet.getDouble("replacement_cost");
                String rating = resultSet.getString("rating");
                String special_features = resultSet.getString("special_features");
                Film film = new Film(film_id, title, description, release_year, language_id, original_language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features);

                films.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(storedCode);
        }
        return films;
    }

    public List<Film> getFilmsByKeyword(String palabraTitulo) {
        // Parte 3. Actividad 9. Metodo que permite recuperar todas las peliculas que contengan una palabra en su titulo.
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film WHERE title LIKE ?";
        try (Connection connection = open(true, storedCode)) {
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
        } finally {
            close(storedCode);
        }
        return films;
    }

    public List<Film> getFilmsByRatingAndCost(Rating rating, double replacementCost) {
        // Parte 3. Actividad 10. Metodo que permite recuperar todas las peliculas de un rating dado
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film WHERE rating = ? AND replacement_cost > ?";
        try (Connection connection = open(true, storedCode)) {
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
        } finally {
            close(storedCode);
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

        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDouble(1, minAverageRentalRate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int actorId = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Actor actor = new Actor(actorId, firstName, lastName);
                List<Film> films = getFilmsForActor(actorId);
                actor.setFilms(films);
                actors.add(actor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(storedCode);
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

        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int actorId = resultSet.getInt("actor_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                actor = new Actor(actorId, firstName, lastName);
                List<Film> films = getFilmsForActor(actorId);
                actor.setFilms(films);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            close(storedCode);
        }
        return actor;
    }

    public boolean existsActor(int actorId) {
        // Parte 3. Actividad 15. Metodo que permite comprobar si existe un actor mediante un ID
        String sql = "SELECT actor_id FROM actor WHERE actor_id = ?";
        try (Connection connection = open(true, storedCode)) {
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
        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, filmId);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Devuelve true si hay al menos un resultado
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(storedCode);
        }
        return false;
    }

    public boolean existsById(int objectId, String idColumnName, String tableName) {
        // Parte 3. Actividad 17. Implementa un método genérico que devuelva un booleano. El valor del booleano deberá ser true si existe al menos un objeto en una tabla dada
        String sql = "SELECT " + idColumnName + " FROM " + tableName + " WHERE " + idColumnName + " = ?";
        try (Connection connection = open(true, storedCode)) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, objectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(storedCode);
        }
        return false;
    }

}
