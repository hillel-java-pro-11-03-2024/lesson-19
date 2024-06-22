package service;

import db.GameRepository;
import db.entity.GameEntity;
import db.entity.GameType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TerminalService {

  private static final Scanner scanner = new Scanner(System.in);
  private String input = "";
  private GameRepository gameRepository;

  public TerminalService(GameRepository gameRepository) {
    this.gameRepository = gameRepository;
  }

  private static void clearConsole() {
    for (int i = 0; i < 20; i++) {
      System.out.println();
    }
  }

  private static void pause() {
    System.out.println("Press Enter to continue...");
    scanner.nextLine();
  }

  public void start() {
    while (!"0".equals(input)) {
      showMenu();
      input = scanner.nextLine();
      handleInput(input);
    }
  }

  private void showMenu() {
    clearConsole();
    System.out.println("Good day");
    int totalGames = gameRepository.countAll();
    System.out.println("Total games: " + totalGames);
    System.out.println("1. Add new game");
    System.out.println("2. Delete game");
    System.out.println("3. Search game by name");
    System.out.println("4. Filter games by price");
    System.out.println("5. Filter games by type");
    System.out.println("6. Show all games sorted by added date");
    System.out.println("7. Show all games");
    System.out.println("0. Exit from program");
  }

  private void handleInput(String input) {
    switch (input) {
      case "1" -> addGame();
      case "2" -> deleteGame();
      case "3" -> searchGameByName();
      case "4" -> filterGamesByPrice();
      case "5" -> filterGamesByType();
      case "6" -> showAllGamesSortedByDate();
      case "7" -> showAllGames();
      case "0" -> System.out.println("Exiting...");
      default -> System.out.println("Invalid option. Please try again.");
    }
    if (!"0".equals(input)) {
      pause();
    }
  }

  private void addGame() {
    System.out.println("Enter game name:");
    String name = scanner.nextLine();
    System.out.println("Enter game type:");
    GameType type = GameType.valueOf(scanner.nextLine().toUpperCase());
    System.out.println("Enter release date (yyyy-MM-ddTHH:mm):");
    LocalDateTime releaseDate = LocalDateTime.parse(scanner.nextLine());
    System.out.println("Enter rating:");
    int rating = Integer.parseInt(scanner.nextLine());
    System.out.println("Enter cost:");
    BigDecimal cost = new BigDecimal(scanner.nextLine());
    System.out.println("Enter description:");
    String description = scanner.nextLine();

    GameEntity game = new GameEntity(name, type, releaseDate, LocalDateTime.now(), rating, cost, description);
    gameRepository.add(game);
    System.out.println("Game added successfully!");
  }

  private void deleteGame() {
    System.out.println("Enter game ID to delete:");
    int id = Integer.parseInt(scanner.nextLine());
    boolean success = gameRepository.delete(id);
    if (success) {
      System.out.println("Game deleted successfully!");
    } else {
      System.out.println("Game not found.");
    }
  }

  private void searchGameByName() {
    System.out.println("Enter game name to search:");
    String name = scanner.nextLine();
    List<GameEntity> games = gameRepository.findByName(name);
    games.forEach(System.out::println);
  }

  private void filterGamesByPrice() {
    System.out.println("Enter price to filter games:");
    BigDecimal price = new BigDecimal(scanner.nextLine());
    List<GameEntity> games = gameRepository.findByPrice(price);
    games.forEach(System.out::println);
  }

  private void filterGamesByType() {
    System.out.println("Enter game type to filter games:");
    GameType type = GameType.valueOf(scanner.nextLine().toUpperCase());
    List<GameEntity> games = gameRepository.findByType(type);
    games.forEach(System.out::println);
  }

  private void showAllGamesSortedByDate() {
    List<GameEntity> games = gameRepository.findAllSortedByDate();
    games.forEach(System.out::println);
  }

  private void showAllGames() {
    List<GameEntity> games = gameRepository.findAll();
    games.forEach(System.out::println);
  }

}
