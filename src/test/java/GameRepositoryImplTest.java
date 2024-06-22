import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import db.entity.GameEntity;
import db.entity.GameType;
import db.impl.GameRepositoryImpl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GameRepositoryImplTest {

  @Mock
  private Connection mockConnection;

  @Mock
  private PreparedStatement mockPreparedStatement;

  @Mock
  private ResultSet mockResultSet;

  private GameRepositoryImpl gameRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    gameRepository = new GameRepositoryImpl(mockConnection);
  }

  @Test
  void testAddGame() throws Exception {
    when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
        .thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(1);
    when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
    when(mockResultSet.next()).thenReturn(true);
    when(mockResultSet.getInt(1)).thenReturn(1);

    GameEntity game = new GameEntity(0, "Test Game", GameType.ACTION, LocalDateTime.now(),
        LocalDateTime.now(), 5, new BigDecimal("29.99"), "Test Description");

    gameRepository.add(game);

    verify(mockPreparedStatement).setString(1, "Test Game");
    verify(mockPreparedStatement).setString(2, GameType.ACTION.name());
    verify(mockPreparedStatement).setTimestamp(3, Timestamp.valueOf(game.getReleaseDate()));
    verify(mockPreparedStatement).setInt(5, 5);
    verify(mockPreparedStatement).setBigDecimal(6, new BigDecimal("29.99"));
    verify(mockPreparedStatement).setString(7, "Test Description");
    verify(mockPreparedStatement).executeUpdate();

    assertEquals(1, game.getId());
  }

  @Test
  void testDeleteGame() throws Exception {
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(1);

    boolean result = gameRepository.delete(1);

    verify(mockPreparedStatement).setInt(1, 1);
    verify(mockPreparedStatement).executeUpdate();

    assertTrue(result);
  }

  @Test
  void testDeleteNonExistingGame() throws Exception {
    when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    when(mockPreparedStatement.executeUpdate()).thenReturn(0);

    boolean result = gameRepository.delete(1);

    verify(mockPreparedStatement).setInt(1, 1);
    verify(mockPreparedStatement).executeUpdate();

    assertFalse(result);
  }


}
