package ru.console.chess;

import ru.console.chess.peice.*;

import java.io.IOException;

public class Board {

    public Piece[][] board = new Piece[8][8];

    public Board() {
        this.initialize();
    }

    /**
     * Размещаем фигуры на доске
     */
    private void initialize() {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                board[x][y] = null;
            }
        }

        // Белые пешки
        for (int x = 0; x < 8; x++) {
            board[1][x] = new Pawn("white");
        }

        // Черные пешки
        for (int x = 0; x < 8; x++) {
            board[6][x] = new Pawn("black");
        }

        //Ладья
        board[0][0] = new Rook("white");
        board[0][7] = new Rook("white");
        board[7][7] = new Rook("black");
        board[7][0] = new Rook("black");

        //Конь
        board[0][1] = new Knight("white");
        board[0][6] = new Knight("white");
        board[7][6] = new Knight("black");
        board[7][1] = new Knight("black");

        //слон
        board[0][2] = new Bishop("white");
        board[0][5] = new Bishop("white");
        board[7][2] = new Bishop("black");
        board[7][5] = new Bishop("black");

        //Королевы
        board[0][3] = new Queen("white");
        board[7][3] = new Queen("black");

        //Король
        board[0][4] = new King("white");
        board[7][4] = new King("black");

    }

    /**
     * Проверка находится ли король под шахом
     *
     * @param color цвет проверяемого короля
     * @return true - король указанного цвета под шахом, false - нет
     */
    public boolean isInCheck(String color) {
        Point kingPos = getKingPos(color);
        int row = kingPos.getX();
        int col = kingPos.getY();

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                Piece currectPeice = board[x][y];
                if (currectPeice != null) {
                    if (currectPeice.validateMove(board, x, y, row, col) && !currectPeice.getColor().equals(color)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void move(String move, String color, boolean actuallyMove) throws IOException {
        Move moveArray = parseInput(move);
        Piece from = BoardUtil.getPieceByPoint(board, moveArray.getFrom());
        Piece to = BoardUtil.getPieceByPoint(board, moveArray.getTo());

        checkMove(color, from, to, moveArray);


        if (actuallyMove) {
            board[moveArray.getTo().getY()][moveArray.getTo().getX()] = board[moveArray.getFrom().getY()][moveArray.getFrom().getX()];
            board[moveArray.getFrom().getY()][moveArray.getFrom().getX()] = null;
        }
        castle(actuallyMove, moveArray);

        //Отдельно для пешек
        if (actuallyMove) {
            Piece piece = from;
            piece.ep_able = false;
            if (piece != null) {

                if (piece.getClass().isInstance(new Pawn("white"))) {
                    piece.hasMoved = true;

                    // Пешка дошла до конца - через комманду мы могли передать
                    // N для замены на коня, B - для замены на слона
                    // И во всех остальных случаях будет замена на королеву
                    Piece replacement;
                    if (move.split(" ").length < 3) {
                        move += " s";
                    }
                    if (piece.getColor().equals("white")) {
                        if (moveArray.getTo().getY() == 7) {
                            switch (move.split(" ")[2].charAt(0)) {
                                case 'N':
                                    replacement = new Knight("white");
                                    break;
                                case 'B':
                                    replacement = new Bishop("white");
                                    break;
                                default:
                                    replacement = new Queen("white");
                                    break;
                            }
                            board[moveArray.getTo().getY()][moveArray.getTo().getX()] = replacement;
                        }
                    } else {
                        if (moveArray.getTo().getY() == 0) {
                            switch (move.split(" ")[2].charAt(0)) {
                                case 'N':
                                    replacement = new Knight("black");
                                    break;
                                case 'B':
                                    replacement = new Bishop("black");
                                    break;
                                default:
                                    replacement = new Queen("black");
                                    break;
                            }
                            board[moveArray.getTo().getY()][moveArray.getTo().getX()] = replacement;
                        }
                    }
                }
            }
        }
    }

    private void checkMove(String color, Piece from, Piece to, Move moveArray) throws IOException {
        if (from == null) {
            throw new IOException("From point is empty");
        }

        if (!from.getColor().equals(color)) {
            throw new IOException("Its not figure");
        }

        if (to != null) {
            if (to.getColor().equals(color)) {
                throw new IOException("There is your figure on the destination");
            }
        }

        if (!from.validateMove(board, moveArray.getFrom().getY(), moveArray.getFrom().getX(),
                moveArray.getTo().getY(), moveArray.getTo().getX())) {
            throw new IOException("Incorrect move");
        }
    }

    private void castle(boolean actuallyMove, Move moveArray) {
        Piece to;
        to = board[moveArray.getTo().getY()][moveArray.getTo().getX()];

        if (to != null) {
            if (to.getClass().isInstance(new King("white"))) {
                King king = (King) board[moveArray.getTo().getY()][moveArray.getTo().getX()];
                if (actuallyMove) {
                    king.hasMoved = true;
                }
                //ракировка
                if (king.castled) {
                    if (moveArray.getTo().getX() - moveArray.getFrom().getX() == 2) {
                        board[moveArray.getTo().getY()][moveArray.getTo().getX() - 1] = board[moveArray.getTo().getY()][moveArray.getTo().getX() + 1];
                        board[moveArray.getTo().getY()][moveArray.getTo().getX() + 1] = null;
                    } else {
                        board[moveArray.getTo().getY()][moveArray.getTo().getX() + 1] = board[moveArray.getTo().getY()][moveArray.getTo().getX() - 1];
                        board[moveArray.getTo().getY()][moveArray.getTo().getX() - 1] = null;
                    }
                    king.castled = false;
                }
            }
        }
    }

    /**
     * Получаем из консоли данные по движению и переводим это в команду
     *
     * @param move
     * @return
     */
    public static Move parseInput(String move) {
        String[] split = move.split(" ");
        int xFrom = charToInt(Character.toLowerCase(split[0].charAt(0)));
        int yFrom = Integer.parseInt(move.charAt(1) + "") - 1;

        int xTo = charToInt(Character.toLowerCase(split[1].charAt(0)));
        int yTo = Integer.parseInt(split[1].charAt(1) + "") - 1;
        return new Move(xFrom, yFrom, xTo, yTo);
    }

    /**
     * Переводим комманду в координату
     */
    public static int charToInt(char ch) {
        switch (ch) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            default:
                return 8;
        }
    }

    /**
     * Находим короля определенного цвета на доске
     *
     * @param color цвет короля, которого ищем
     * @return Местоположение короля
     */
    private Point getKingPos(String color) {
        int row = 0, col = 0;

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] != null) {
                    if (board[x][y].getClass().isInstance(new King("white")) && board[x][y].getColor().equals(color)) {
                        row = x;
                        col = y;
                    }
                }
            }
        }
        return new Point(row, col);
    }

    /**
     * Проверяет может ли какая-нибудь фигура сделать ход или у нас шах, пад, мат
     *
     * @return
     */
    public boolean canAnyPieceMakeAnyMove(String color) {

        Piece[][] oldBoard = board.clone();

        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                //Check this piece against every other piece...
                for (int w = 0; w < board.length; w++) {
                    for (int z = 0; z < board[0].length; z++) {
                        try {
                            if (board[x][y] != null) {
                                if (board[x][y].getColor().equals(color)) {
                                    //System.out.println(coordinatesToMoveString(x, y, w, z));
                                    move(coordinatesToMoveString(x, y, w, z), board[x][y].getColor(), false);
                                    board = oldBoard;
                                    return true;
                                }
                            }
                            board = oldBoard;
                        } catch (Exception e) {
                            board = oldBoard;
                        }
                    }
                }
            }
        }

        board = oldBoard;
        return false;
    }

    /**
     * Переводим из реальных координат в комманду для программы
     *
     * @param row
     * @param col
     * @param newRow
     * @param newCol
     * @return
     */
    private String coordinatesToMoveString(int row, int col, int newRow, int newCol) {

        String returnString = "";

        switch (col) {
            case 0:
                returnString += 'a';
                break;
            case 1:
                returnString += 'b';
                break;
            case 2:
                returnString += 'c';
                break;
            case 3:
                returnString += 'd';
                break;
            case 4:
                returnString += 'e';
                break;
            case 5:
                returnString += 'f';
                break;
            case 6:
                returnString += 'g';
                break;
            case 7:
                returnString += 'h';
                break;
            default:
                returnString += 'a';
                break;
        }

        int addInt = row + 1;

        returnString += addInt + "";

        returnString += " ";

        switch (newCol) {
            case 0:
                returnString += 'a';
                break;
            case 1:
                returnString += 'b';
                break;
            case 2:
                returnString += 'c';
                break;
            case 3:
                returnString += 'd';
                break;
            case 4:
                returnString += 'e';
                break;
            case 5:
                returnString += 'f';
                break;
            case 6:
                returnString += 'g';
                break;
            case 7:
                returnString += 'h';
                break;
            default:
                returnString += 'a';
                break;
        }

        addInt = newRow + 1;

        returnString += addInt + "";
        //System.out.println(row + " " + col + " " + newRow + " " + newCol + " " + returnString);
        return returnString;
    }

    /**
     * Возращаем строку для расспечатывания доски
     *
     * @return
     */
    public String toString() {
        String string = "";
        int fileCount = 0;
        for (Piece[] pieces : board) {
            int rankCount = 0;
            for (Piece piece : pieces) {
                if (piece == null) {
                    string += "[  ]";
                } else {
                    string += "[" + piece + "]";
                }
                string += " ";
                rankCount++;
            }
            fileCount++;
            string += "\n";
        }

        String reverseString = "";

        reverseString += "   A    B    C    D    E    F    G    H \n";
        String[] stringSplit = string.split("\n");
        for (int x = stringSplit.length - 1; x >= 0; x--) {
            reverseString += x + 1 + "|" + stringSplit[x] + "|\n";
        }

        return reverseString;
    }

}
