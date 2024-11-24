package ru.console.chess;

import ru.console.chess.peice.Piece;

import java.io.IOException;
import java.util.Scanner;
public class Chess {
    public static void main(String[] args) {

        Board gameBoard = new Board();
        String color = "white";

        boolean drawAvailable = false;

        while(true){

            System.out.println(gameBoard);

            System.out.println(color + " make a move: ");
            Scanner sc = new Scanner(System.in);
            String move = sc.nextLine();

            if(drawAvailable){
                if(move.contains("draw")){
                    System.out.println("The game is a draw.");
                    return;
                }else{
                    drawAvailable = false;
                }
            }

            if(move.contains("resign")){
                System.out.println(color + " resigns");
                System.out.println(colorToggle(color) + " wins the game!");
                return;
            }

            try {
                gameBoard.move(move, color, true);
            } catch (IOException e) {
                // сообщает повторить ввод
                System.out.println("Invalid input! " + e.getMessage());
                continue;
            }

            Piece[][] oldBoard = gameBoard.board.clone();

            if(!gameBoard.canAnyPieceMakeAnyMove(colorToggle(color))){
                if(gameBoard.isInCheck(colorToggle(color))){
                    System.out.println("Checkmate. " + color + " wins");
                    System.out.println("Game over!");
                }else{
                    System.out.println("Stalemate!");
                }
                return;
            }

            gameBoard.board = oldBoard;

            if(gameBoard.isInCheck(colorToggle(color))){
                System.out.println(colorToggle(color) + " is in check.");
            }

            if(move.contains("draw?")){
                drawAvailable = true;
            }



            color = colorToggle(color);

        }

    }
    public static String colorToggle(String color){
        if(color.equals("white")){
            return "black";
        }

        return "white";
    }
}
