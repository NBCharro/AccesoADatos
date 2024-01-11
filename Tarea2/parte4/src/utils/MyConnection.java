package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnection {
    private static Connection connection;
    private static Long code;
    private static String url;

    private static String username;
    private static String password;

    static {
        try {
            readProperties();
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static Long getCode() {
        return code;
    }

    public static void setCode(Long newCode) {
        code = newCode;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    private static void readProperties() {
        try (InputStream input = MyConnection.class.getClassLoader().getResourceAsStream("utils/connection.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Error, no se puede encontrar el archivo connection.properties");
                return;
            }

            // Cargamos las propiedades desde el fichero
            prop.load(input);

            // Asignamos las propiedades a los atributos de clase
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
