package io.deeplay.domain;

public enum GameErrorCode {
    MOVE_NOT_FOUND("Ход не найден"),
    INCORRECT_MOVE_FORMAT("Неверный формат записи хода"),
    INCORRECT_FIGURE_CHARACTER("Неверный символ обозначения фигуры"),
    INCORRECT_POSITION_FORMAT("Неверный формат позиции");

    private final String description;

    GameErrorCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
