package db.impl;

import db.GameRepository;
import db.entity.GameEntity;
import db.entity.GameType;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GameRepositoryImpl implements GameRepository {

  private final Connection connection;

  public GameRepositoryImpl(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void add(GameEntity game) {
    final String query =
        """
                    INSERT INTO public.games(
                    name, type, release_date, created_at, rating, cost, description)
                    VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    try (PreparedStatement ps = connection.prepareStatement(query,
        Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, game.getName());
      ps.setString(2, game.getType().name());
      ps.setTimestamp(3, Timestamp.valueOf(game.getReleaseDate()));
      ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
      ps.setInt(5, game.getRating());
      ps.setBigDecimal(6, game.getCost());
      ps.setString(7, game.getDescription());
      ps.executeUpdate();

      try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
        if (generatedKeys.next()) {
          game.setId(generatedKeys.getInt(1));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to add game: " + e.getMessage(), e);
    }
  }

  @Override
  public boolean delete(int id) {
    final String query = "DELETE FROM public.games WHERE id = ?";
    try (PreparedStatement ps = connection.prepareStatement(query)) {
      ps.setInt(1, id);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete game: " + e.getMessage(), e);
    }
  }

  @Override
  public List<GameEntity> findByName(String name) {
    final String query = "SELECT * FROM public.games WHERE name = ?";
    return executeQuery(query, ps -> ps.setString(1, name));
  }

  @Override
  public List<GameEntity> findByPrice(BigDecimal price) {
    final String query = "SELECT * FROM public.games WHERE cost = ?";
    return executeQuery(query, ps -> ps.setBigDecimal(1, price));
  }

  @Override
  public List<GameEntity> findByType(GameType type) {
    final String query = "SELECT * FROM public.games WHERE type = ?";
    return executeQuery(query, ps -> ps.setString(1, type.name()));
  }

  @Override
  public List<GameEntity> findAllSortedByDate() {
    final String query = "SELECT * FROM public.games ORDER BY release_date";
    return executeQuery(query);
  }

  @Override
  public List<GameEntity> findAll() {
    final String query = "SELECT * FROM public.games";
    return executeQuery(query);
  }

  @Override
  public int countAll() {
    final String query = "SELECT COUNT(*) FROM public.games";
    try (PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery()) {
      rs.next();
      return rs.getInt(1);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private List<GameEntity> executeQuery(String query, StatementSetter setter) {
    List<GameEntity> games = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(query)) {
      setter.setValues(ps);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          games.add(mapResultToGame(rs));
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to execute query: " + e.getMessage(), e);
    }
    return games;
  }

  private List<GameEntity> executeQuery(String query) {
    List<GameEntity> games = new ArrayList<>();
    try (PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        games.add(mapResultToGame(rs));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to execute query: " + e.getMessage(), e);
    }
    return games;
  }

  private GameEntity mapResultToGame(ResultSet rs) throws SQLException {
    return new GameEntity()
        .withId(rs.getLong("id"))
        .withName(rs.getString("name"))
        .withType(GameType.valueOf(rs.getString("type")))
        .withCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
        .withReleaseDate(rs.getTimestamp("release_date").toLocalDateTime())
        .withRating(rs.getInt("rating"))
        .withCost(rs.getBigDecimal("cost"))
        .withDescription(rs.getString("description"));
  }

  @FunctionalInterface
  interface StatementSetter {
    void setValues(PreparedStatement ps) throws SQLException;
  }
}
