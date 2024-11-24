package ru.console.chess;

import ru.console.chess.peice.Piece;

public class BoardUtil {

    public static Piece getPieceByPoint(Piece[][] board, Point point) {
        return board[point.getY()][point.getX()];
    }

}
