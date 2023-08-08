package io.deeplay.grandmastery;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
  private static final Logger logger = LoggerFactory.getLogger(Server.class);
  public static final int PORT = 8080;
  public static final ExecutorService START_GAME = Executors.newFixedThreadPool(8);
  public static final ExecutorService GAMES = Executors.newFixedThreadPool(8);

  /**
   * Метод запускает сервер на указанном порту.
   *
   * @throws RuntimeException В случае ошибки сервера
   */
  public static void run() {
    try (var serverSocket = new ServerSocket(PORT)) {
      logger.info("Сервер запущен!");

      while (!serverSocket.isClosed()) {
        var socket = serverSocket.accept();
        socket.setSoTimeout((int) TimeUnit.MINUTES.toMillis(5));

        START_GAME.execute(new CreateGame(socket));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      GAMES.shutdownNow();
      START_GAME.shutdownNow();

      logger.info("Сервер остановлен!");
    }
  }

  /** Метод запускает сервер. */
  public static void main(String[] args) {
    Server.run();
  }
}
