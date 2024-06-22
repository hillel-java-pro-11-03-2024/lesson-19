import components.ConnectionSingle;
import db.impl.GameRepositoryImpl;
import java.sql.Connection;
import service.TerminalService;

public static void main(String[] args) {
  try {
    Connection connection = ConnectionSingle.getConnection();
    GameRepositoryImpl gameRepository = new GameRepositoryImpl(connection);
    TerminalService terminal = new TerminalService(gameRepository);
    terminal.start();
  } catch (Exception e) {
    e.printStackTrace();
  }
}
