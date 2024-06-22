package components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingle {

  private static Connection connection;
  private static final String url = "jdbc:postgresql://localhost:5432/game-shop";
  private static final String name = "postgres";
  private static final String password = "Qwerty12345";

  public static Connection getConnection() throws SQLException {

    if (connection == null || connection.isClosed()) {
      connection = DriverManager.getConnection(url, name, password);
    }

    return connection;
  }
}
