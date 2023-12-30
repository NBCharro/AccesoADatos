package persistence;

import datamodel.Actor;

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

    public Connection getConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection(urlDB, user, pass);
            return connection;

        } catch (SQLException e) {
            throw new SQLException("Error al obtener la conexión a la base de datos", e);
        }
    }

    public void closeConnection() {
        // Método para cerrar la conexión con la base de datos.
        // Creado previamente al metodo close() del apartado Patrón singleton para la creación de nuevas conexiones.
        // Lo he creado antes de que se pidiesen metodos especificos open() y close(). No los borro porque los metodos del principio los usan.
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Actor> getActorWithoutCondition() {
        List<Actor> actors = new ArrayList<>();
        try {
            // Cree una conexión con la base de datos
            connection = getConnection();
            // Cree un objeto de tipo Statement
            try (Statement statement = connection.createStatement()) {
                // Reciba todos los actores de la base de datos en forma de ResultSet
                ResultSet resultSet = statement.executeQuery("SELECT * FROM sakila.actor;");
                // Llenar la lista de actores con la información del ResultSet
                while (resultSet.next()) {
                    int actorId = resultSet.getInt("actor_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Actor actor = new Actor(actorId, firstName, lastName);
                    actors.add(actor);
                }
                // Cierre el objeto ResultSet
                resultSet.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // No lo pone el enunciado pero es necesario cerrar la conexion.
            closeConnection();
        }

        return actors;
    }

    public List<Actor> getActorWithCondition(String condition) {
        List<Actor> actors = new ArrayList<>();
        try {
            // Cree una conexión con la base de datos
            connection = getConnection();
            // Cree un objeto de tipo Statement
            try (Statement statement = connection.createStatement()) {
                // Consulta SQL con la condición
                String sql = "SELECT actor_id, first_name, last_name FROM actor WHERE " + condition;
                // Ejecutar la consulta SQL y obtener un ResultSet
                ResultSet resultSet = statement.executeQuery(sql);
                // Llenar la lista de actores con la información del ResultSet
                while (resultSet.next()) {
                    int actorId = resultSet.getInt("actor_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Actor actor = new Actor(actorId, firstName, lastName);
                    actors.add(actor);
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // No lo pone el enunciado pero es necesario cerrar la conexion.
            closeConnection();
        }
        return actors;
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

    public List<Actor> getActor(String condition) {
        List<Actor> actors = new ArrayList<>();
        try {
            // Abrir la conexión
            open();
            // Crear un objeto de tipo Statement
            try (Statement statement = connection.createStatement()) {
                // Consulta SQL con la condición
                String sql = "SELECT actor_id, first_name, last_name FROM actor WHERE " + condition;
                // Ejecutar la consulta SQL y obtener un ResultSet
                ResultSet resultSet = statement.executeQuery(sql);
                // Llenar la lista de actores con la información del ResultSet
                while (resultSet.next()) {
                    int actorId = resultSet.getInt("actor_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    Actor actor = new Actor(actorId, firstName, lastName);
                    actors.add(actor);
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            close();
        }
        return actors;
    }

    // Parte 2. Actividad 9. Método para verificar que no se han creado dos objetos Connection diferentes
    public boolean areConnectionsEqual(PersistenceManager anotherManager) {
        return this.connection == anotherManager.connection;
    }
}
