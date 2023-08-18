package io.deeplay.grandmastery;

import io.deeplay.grandmastery.core.Move;
import io.deeplay.grandmastery.core.Player;
import io.deeplay.grandmastery.domain.*;
import io.deeplay.grandmastery.dto.SendAnswerDraw;
import io.deeplay.grandmastery.dto.SendMove;
import io.deeplay.grandmastery.dto.WaitAnswerDraw;
import io.deeplay.grandmastery.dto.WaitMove;
import io.deeplay.grandmastery.exceptions.GameException;
import io.deeplay.grandmastery.service.ConversationService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ServerPlayer extends Player {
  private final Socket socket;
  private final BufferedReader in;
  private final BufferedWriter out;
  private final GameMode gameMode;
  private final ChessType chessType;

  /**
   * Конструктор.
   *
   * @param socket Socket
   * @param in Input stream
   * @param out Output stream
   * @param name Имя
   * @param color Цвет
   * @param gameMode Режим игры
   * @param chessType Тип шахмат
   */
  public ServerPlayer(
      Socket socket,
      BufferedReader in,
      BufferedWriter out,
      String name,
      Color color,
      GameMode gameMode,
      ChessType chessType) {
    super(name, color);
    this.socket = socket;
    this.in = in;
    this.out = out;
    this.gameMode = gameMode;
    this.chessType = chessType;
  }

  @Override
  public Move createMove() throws GameException {
    try {
      if (this.isGameOver()) {
        throw GameErrorCode.GAME_ALREADY_OVER.asException();
      }

      var json = ConversationService.serialize(new WaitMove());
      ServerDao.send(out, json);

      log.info("Отправили клиенту запрос о ходе");

      var stringQuery = ServerDao.getJsonFromClient(in);
      var move = ConversationService.deserialize(stringQuery, SendMove.class).getMove();

      log.info("Получили ход от клиента - " + move);

      if (move.moveType() == MoveType.DEFAULT) {
        setLastMove(move);
      }
      return move;
    } catch (Exception e) {
      throw GameErrorCode.ERROR_PLAYER_MAKE_MOVE.asException();
    }
  }

  @Override
  public boolean answerDraw() {
    try {
      if (this.isGameOver()) {
        throw GameErrorCode.GAME_ALREADY_OVER.asException();
      }

      String json = ConversationService.serialize(new WaitAnswerDraw());
      ServerDao.send(out, json);

      log.info("Отправили клиенту запрос на ничью");

      String stringQuery = ServerDao.getJsonFromClient(in);
      boolean answer = ConversationService.deserialize(stringQuery, SendAnswerDraw.class).isAgree();

      log.info("Получили ответ на ничью от клиента - " + answer);

      return answer;
    } catch (Exception e) {
      throw GameErrorCode.ERROR_PLAYER_MAKE_MOVE.asException();
    }
  }
}