package io.deeplay.grandmastery.algorithms;

import io.deeplay.grandmastery.core.Board;
import io.deeplay.grandmastery.core.GameHistory;
import io.deeplay.grandmastery.core.Move;
import io.deeplay.grandmastery.core.PlayerInfo;
import io.deeplay.grandmastery.domain.Color;
import io.deeplay.grandmastery.utils.LongAlgebraicNotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.deeplay.grandmastery.utils.Algorithms.*;

public class PrevMinimax implements Algorithm {
	private final Color botColor;
	private final int deep;
	private final boolean isMax;
	private final Map<Move, Double> moveThree;
	private final Bonuses bonuses;

	private int moveCount;

	public PrevMinimax(PlayerInfo playerInfo, int deep) {
		this.botColor = playerInfo.getColor();
		this.isMax = true;
		this.deep = deep;
		this.bonuses = new Bonuses();
		this.moveThree = new HashMap<>();
		this.moveCount = 0;
	}

	@Override
	public Move findBestMove(Board board, GameHistory gameHistory) {
		moveThree.clear();
		moveCount++;
		return minmax(board, gameHistory, botColor, this.deep, MIN_EVAL, MAX_EVAL, this.isMax);
	}

	private Move minmax(
			Board board,
			GameHistory gameHistory,
			Color color,
			int deep,
			double alpha,
			double beta,
			boolean isMax) {
		if (deep == 0 || isGameOver(board, gameHistory)) {
			moveThree.put(
					board.getLastMove(), EvaluationPrev.evaluationFunc(board, gameHistory, botColor, bonuses));
			return board.getLastMove();
		}

		List<Move> allMoves = getPossibleMoves(board, color);
		Move bestMove = allMoves.get(0);
		moveThree.put(bestMove, isMax ? MIN_EVAL : MAX_EVAL);

		for (Move move : allMoves) {
			Board copyBoard = copyAndMove(move, board);
			GameHistory copyHistory = copyHistoryAndMove(copyBoard, gameHistory);
			double eval =
					moveThree.get(
							minmax(copyBoard, copyHistory, inversColor(color), deep - 1, alpha, beta, !isMax));

			if (isMax) {
				if (eval > alpha) {
					alpha = eval;
					moveThree.put(move, eval);
					bestMove = move;
				}
			} else {
				if (eval < beta) {
					beta = eval;
					moveThree.put(move, eval);
					bestMove = move;
				}
			}

			if (beta <= alpha) {
				break;
			}
		}

		return bestMove;
	}
}
