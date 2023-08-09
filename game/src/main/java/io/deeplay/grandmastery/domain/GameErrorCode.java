package io.deeplay.grandmastery.domain;

import io.deeplay.grandmastery.exceptions.GameException;

public enum GameErrorCode {
  MOVE_NOT_FOUND("Ход не найден"),
  INCORRECT_MOVE_FORMAT("Неверный формат записи хода"),
  INCORRECT_FIGURE_CHARACTER("Неверный символ обозначения фигуры"),
  INCORRECT_POSITION_FORMAT("Неверный формат позиции"),
  NULL_POINTER_SOURCE_BOARD("Параметр sourceBoard не может быть равен null"),
  UNKNOWN_FIGURE_TYPE("Неизвестный тип фигуры"),
  UNDEFINED_BEHAVIOR_BOT("Неопределенное поведение, у бота отсутствуют возможные ходы"),
  IMPOSSIBLE_PAWN_REVIVE("Невозможное превращение пешки"),
  ERROR_START_GAME("Ошибка запуска игры"),
  ERROR_PLAYER_MAKE_MOVE("Ошибка при запросе хода у игрока, MoveData пустой");

  private final String description;

  GameErrorCode(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public GameException asException() {
    return new GameException(this);
  }
}