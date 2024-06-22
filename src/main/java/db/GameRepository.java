package db;

import db.entity.GameEntity;
import java.math.BigDecimal;
import java.util.List;
import db.entity.GameType;

public interface GameRepository {

  void add(GameEntity game);
  boolean delete(int id);
  List<GameEntity> findByName(String name);
  List<GameEntity> findByPrice(BigDecimal price);
  List<GameEntity> findByType(GameType type);
  List<GameEntity> findAllSortedByDate();
  List<GameEntity> findAll();
  int countAll();

}
