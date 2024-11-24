package ru.console.chess.peice;

// пешка
public class Pawn extends Piece {

    public String color;
    public boolean hasMoved;

    public boolean isEatAdvanced;

    public Pawn(String color){
        this.color = color;
        this.hasMoved = false;
    }

    public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {

        // Идем в обратную сторону - нельзя
        if(color.equals("white")){
            if(currentRow > newRow){
                return false;
            }
        }else{
            if(newRow > currentRow){
                return false;
            }
        }

        if(currentCol == newCol){
            // Клетка на которую идем - занята
            if(color.equals("white")){
                if(board[currentRow + 1][currentCol] != null){
                    return false;
                }
            }else{
                if(board[currentRow - 1][currentCol] != null){
                    return false;
                }
            }

            // двигаемся больше чем на 2 клетки
            if(Math.abs(newRow - currentRow) > 2){
                return false;
            }else if(Math.abs(newRow - currentRow) == 2){
                // движемся на 2 клетки

                // если уже был ход - то нельзя
                if(hasMoved){
                    return false;
                }

                //клетка занята - нельзя
                if(color.equals("white")){
                    if(board[currentRow + 2][currentCol] != null){
                        return false;
                    }
                }else{
                    if(board[currentRow - 2][currentCol] != null){
                        return false;
                    }
                }

                // кушаем фигуру по диагонали
                if(newCol + 1 < 8){
                    if(board[newRow][newCol + 1] != null){
                        if(board[newRow][newCol + 1].getClass().isInstance(new Pawn("white"))){
                            isEatAdvanced = true;
                        }
                    }
                }else if(newCol - 1 > 0){
                    if(board[newRow][newCol - 1] != null){
                        if(board[newRow][newCol - 1].getClass().isInstance(new Pawn("white"))){
                            isEatAdvanced = true;
                        }
                    }
                }

            }
        }else{
            if(Math.abs(newCol - currentCol) != 1 || Math.abs(newRow - currentRow) != 1){
                return false;
            }

            if(board[newRow][newCol] == null){
                return false;
            }
        }

        return true;
    }

    public String getColor(){
        return this.color;
    }

    public String toString(){
        return color.charAt(0) + "p";

    }

}
