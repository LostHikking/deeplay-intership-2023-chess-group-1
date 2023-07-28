package io.deeplay.grandmastery.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.deeplay.grandmastery.domain.Color;
import io.deeplay.grandmastery.exceptions.GameException;
import io.deeplay.grandmastery.figures.King;
import io.deeplay.grandmastery.figures.Pawn;
import io.deeplay.grandmastery.figures.Piece;
import io.deeplay.grandmastery.utils.BoardUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HashBoardTest {
  private Board board;

  @BeforeEach
  void init() {
    board = new HashBoard();
  }

  @Test
  public void setAndGetPieceTest() {
    Position position = new Position(new Column(1), new Row(2));
    Piece pawn = new Pawn(Color.WHITE);

    board.setPiece(position, pawn);
    Position searchPosition = new Position(new Column(1), new Row(2));
    Piece retrievedPiece = board.getPiece(searchPosition);

    assertSame(pawn, retrievedPiece);
  }

  @Test
  public void autoSetBlackAndWhiteKingsTest() {
    Position whiteKingPosition = new Position(new Column(1), new Row(2));
    Piece whiteKing = new King(Color.WHITE);
    board.setPiece(whiteKingPosition, whiteKing);

    Position blackKingPosition = new Position(new Column(1), new Row(7));
    Piece blackKing = new King(Color.BLACK);
    board.setPiece(blackKingPosition, blackKing);

    Position retrievedWhiteKingPosition = board.getWhiteKingPosition();
    Position retrievedBlackKingPosition = board.getBlackKingPosition();

    Assertions.assertAll(
        () -> assertEquals(retrievedWhiteKingPosition, whiteKingPosition, "White king check"),
        () -> assertEquals(retrievedBlackKingPosition, blackKingPosition, "Black king check"));
  }

  @Test
  public void getPieceByRowAndColTest() {
    Position position = new Position(new Column(1), new Row(2));
    Piece pawn = new Pawn(Color.WHITE);

    board.setPiece(position, pawn);
    Piece retrievedPiece = board.getPiece(1, 2);

    assertSame(pawn, retrievedPiece);
  }

  @Test
  public void removePieceTest() {
    Position position = new Position(new Column(1), new Row(4));
    Piece pawn = new Pawn(Color.WHITE);

    board.setPiece(position, pawn);
    board.removePiece(position);
    Piece retrievedPiece = board.getPiece(position);

    assertNull(retrievedPiece);
  }

  @Test
  public void copyBoardTest() {
    Position position = new Position(new Column(1), new Row(4));
    Piece pawn = new Pawn(Color.WHITE);
    board.setPiece(position, pawn);

    Position whiteKingPosition = new Position(new Column(1), new Row(2));
    Piece whiteKing = new King(Color.WHITE);
    board.setPiece(whiteKingPosition, whiteKing);

    Position blackKingPosition = new Position(new Column(1), new Row(7));
    Piece blackKing = new King(Color.BLACK);
    board.setPiece(blackKingPosition, blackKing);

    Position from = new Position(new Column(1), new Row(2));
    Position to = new Position(new Column(2), new Row(3));
    Move lastMove = new Move(from, to, null);
    board.setLastMove(lastMove);

    Board copyBoard = new HashBoard();
    BoardUtils.copyBoard(board).accept(copyBoard);

    Assertions.assertAll(
        () -> assertNotSame(board, copyBoard, "Refer to different objects"),
        () -> assertEquals(board.getLastMove(), copyBoard.getLastMove(), "Compare last move"),
        () ->
            assertEquals(
                board.getPiece(position), copyBoard.getPiece(position), "Compare piece pawn"),
        () ->
            assertEquals(
                board.getBlackKingPosition(),
                copyBoard.getBlackKingPosition(),
                "Compare black king position"),
        () ->
            assertEquals(
                board.getWhiteKingPosition(),
                copyBoard.getWhiteKingPosition(),
                "Compare white king position"),
        () -> {
          board.removePiece(position);
          assertNotNull(copyBoard.getPiece(position), "After removing the pawn in the hashBoard");
        });
  }

  @Test
  public void copyEmptyBoardTest() {
    Board copyBoard = new HashBoard();
    BoardUtils.copyBoard(board).accept(copyBoard);

    Assertions.assertAll(
        () -> assertNotSame(board, copyBoard, "Refer to different objects"),
        () -> assertNull(copyBoard.getBlackKingPosition(), "Black king position is null"),
        () -> assertNull(copyBoard.getWhiteKingPosition(), "White king position is null"),
        () -> assertNull(copyBoard.getLastMove(), "Last move is null"));
  }

  @Test
  public void copyNullSourceBoardTest() {
    Board copyBoard = new HashBoard();
    assertThrows(GameException.class, () -> BoardUtils.copyBoard(null).accept(copyBoard));
  }
}
