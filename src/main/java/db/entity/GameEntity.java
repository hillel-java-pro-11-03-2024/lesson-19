package db.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class GameEntity {

  private long id;
  private String name;
  private GameType type;
  private LocalDateTime releaseDate;
  private LocalDateTime createdAt;
  private int rating;
  private BigDecimal cost;
  private String description;

  public GameEntity(String name, GameType type, LocalDateTime releaseDate, LocalDateTime createdAt,
      int rating, BigDecimal cost, String description) {
    this.name = name;
    this.type = type;
    this.releaseDate = releaseDate;
    this.createdAt = createdAt;
    this.rating = rating;
    this.cost = cost;
    this.description = description;
  }

}
