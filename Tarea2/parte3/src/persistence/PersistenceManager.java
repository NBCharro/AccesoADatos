package persistence;

import datamodel.Actor;
import datamodel.Film;

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

//    public List<Actor> getActor(String condition) {
//        List<Actor> actors = new ArrayList<>();
//        try {
//            // Abrir la conexión
//            open();
//            // Crear un objeto de tipo Statement
//            try (Statement statement = connection.createStatement()) {
//                // Consulta SQL con la condición
//                String sql = "SELECT actor_id, first_name, last_name FROM actor WHERE " + condition;
//                // Ejecutar la consulta SQL y obtener un ResultSet
//                ResultSet resultSet = statement.executeQuery(sql);
//                // Llenar la lista de actores con la información del ResultSet
//                while (resultSet.next()) {
//                    int actorId = resultSet.getInt("actor_id");
//                    String firstName = resultSet.getString("first_name");
//                    String lastName = resultSet.getString("last_name");
//                    Actor actor = new Actor(actorId, firstName, lastName);
//                    actors.add(actor);
//                }
//                resultSet.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // Cerrar la conexión
//            close();
//        }
//        return actors;
//    }
//
//    public List<Actor> getActorWithoutCondition() {
//        List<Actor> actors = new ArrayList<>();
//        try {
//            // Cree una conexión con la base de datos
//            open();
//            // Cree un objeto de tipo Statement
//            try (Statement statement = connection.createStatement()) {
//                // Reciba todos los actores de la base de datos en forma de ResultSet
//                ResultSet resultSet = statement.executeQuery("SELECT * FROM sakila.actor;");
//                // Llenar la lista de actores con la información del ResultSet
//                while (resultSet.next()) {
//                    int actorId = resultSet.getInt("actor_id");
//                    String firstName = resultSet.getString("first_name");
//                    String lastName = resultSet.getString("last_name");
//                    Actor actor = new Actor(actorId, firstName, lastName);
//                    actors.add(actor);
//                }
//                // Cierre el objeto ResultSet
//                resultSet.close();
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            // No lo pone el enunciado pero es necesario cerrar la conexion.
//            close();
//        }
//
//        return actors;
//    }

    public List<String> getFilmsForActor(int actor_id) {
        // Parte 3. Actovidad 5. Definir un metodo que obtenga las peliculas de un determinado actor.
        List<String> films = new ArrayList<>();

        // Utilizando un PreparedStatement para evitar SQL injection
        String sql = "SELECT film.title FROM film " +
                "JOIN film_actor ON film.film_id = film_actor.film_id " +
                "WHERE film_actor.actor_id = ?";

        try {
            open(); // Asegúrate de que la conexión esté abierta

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, actor_id);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Suponemos que Film tiene un método getTitle()
                        String title = resultSet.getString("title");
                        films.add(title);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Manejo básico de excepciones, adaptar según tus necesidades
        } finally {
            // Puedes cerrar la conexión aquí o en otro método, dependiendo de tu aplicación
            close();
        }

        return films;
    }
}
