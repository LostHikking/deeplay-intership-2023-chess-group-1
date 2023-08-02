package io.deeplay.grandmastery.figures;

import io.deeplay.grandmastery.core.Board;
import io.deeplay.grandmastery.core.Move;
import io.deeplay.grandmastery.core.Position;
import io.deeplay.grandmastery.domain.Color;
import io.deeplay.grandmastery.domain.FigureType;
import java.util.List;

public class Bishop extends Piece {
  /**
   * Конструктор для слона.
   *
   * @param color Цвет фигуры
   */
  public Bishop(Color color) {
    super(color);
    this.figureType = FigureType.BISHOP;
    if (color == Color.WHITE) {
      this.symbol = '♗';
    } else {
      this.symbol = '♝';
    }
  }

  @Override
  public void move(Position from, Position to, Board board) {}

  @Override
  public boolean canMove(Position from, Position to, Board board) {
    return false;
  }

  @Override
  public List<Move> getAllMoves(Board board) {
    return null;
  }

  @Override
  public void revive(Position position, Board board, Piece piece) {}

  @Override
  public boolean canRevive(Position position, Board board) {
    return false;
  }
}
