package ru.console.chess.peice;

public abstract class Piece {
	
	public String color;
	public boolean hasMoved;
	public boolean ep_able;

	public abstract String getColor();

	public abstract boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol);

}
