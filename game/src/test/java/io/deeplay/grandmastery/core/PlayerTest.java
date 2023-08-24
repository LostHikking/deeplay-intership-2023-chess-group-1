package io.deeplay.grandmastery.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.deeplay.grandmastery.domain.Color;
import io.deeplay.grandmastery.domain.FigureType;
import io.deeplay.grandmastery.figures.King;
import io.deeplay.grandmastery.figures.Pawn;
import io.deeplay.grandmastery.figures.Rook;
import io.deeplay.grandmastery.utils.Boards;
import io.deeplay.grandmastery.utils.LongAlgebraicNotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
  private Player player;

  private Board board;

  @BeforeEach
  public void init() {
    board = new HashBoard();
    player = new AiPlayer(Color.WHITE);
  }

  @Test
  public void startupTest() {
    Boards.defaultChess().accept(board);
    player.startup(board);

    Assertions.assertAll(
        () -> assertFalse(player.isGameOver(), "Is game over"),
        () -> assertTrue(Boards.isEqualsBoards(board, player.getBoard()), "Equals boards"));
  }

  @Test
  public void makeMoveTest() {
    board.setPiece(Position.fromString("e2"), new Pawn(Color.WHITE));
    player.startup(board);

    Move move = LongAlgebraicNotation.getMoveFromString("e2e4");
    player.makeMove(move);

    Assertions.assertAll(
        () -> assertFalse(player.getBoard().hasPiece(move.from()), "New position"),
        () -> assertTrue(player.getBoard().hasPiece(move.to()), "Old position"));
  }

  @Test
  public void makeMoveRevivePawnTest() {
    board.setPiece(Position.fromString("e7"), new Pawn(Color.WHITE));
    player.startup(board);

    Move move = LongAlgebraicNotation.getMoveFromString("e7e8q");
    player.makeMove(move);

    Assertions.assertAll(
        () -> assertFalse(player.getBoard().hasPiece(move.from()), "Old position"),
        () ->
            assertEquals(
                FigureType.QUEEN,
                player.getBoard().getPiece(move.to()).getFigureType(),
                "Revive queen"));
  }

  @Test
  public void makeMoveKingNoCastlingTest() {
    board.setPiece(Position.fromString("e1"), new King(Color.WHITE));
    player.startup(board);

    Move move = LongAlgebraicNotation.getMoveFromString("e1e2");
    player.makeMove(move);

    Assertions.assertAll(
        () -> assertTrue(player.getBoard().hasPiece(move.to()), "New position"),
        () -> assertFalse(player.getBoard().hasPiece(move.from()), "Old position"));
  }

  @Test
  public void makeMoveKingCastlingTest() {
    Move move = LongAlgebraicNotation.getMoveFromString("e1c1");
    board.setPiece(move.from(), new King(Color.WHITE));
    board.setPiece(Position.fromString("a1"), new Rook(Color.WHITE));

    player.startup(board);
    player.makeMove(move);

    Assertions.assertAll(
        () ->
            assertEquals(
                FigureType.KING, player.getBoard().getPiece(move.to()).getFigureType(), "King"),
        () ->
            assertEquals(
                FigureType.ROOK,
                player.getBoard().getPiece(Position.fromString("d1")).getFigureType(),
                "Rook"),
        () ->
            assertFalse(player.getBoard().hasPiece(Position.fromString("e1")), "Old king position"),
        () ->
            assertFalse(
                player.getBoard().hasPiece(Position.fromString("a1")), "Old rook position"));
  }
}
