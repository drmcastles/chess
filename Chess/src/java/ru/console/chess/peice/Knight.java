package ru.console.chess.peice;

/**
 * Ладья
 */
public class Knight extends Piece {

	public String color;
	
	public Knight(String color){
		this.color = color;
	}
	
	@Override
	public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {

		/**
		 * По вертикали смещение на 2 клетки вверх или вниз, по горизонтали на 1 вверх или вниз
		 */
		if(Math.abs(newRow - currentRow) == 2 && Math.abs(newCol - currentCol) == 1){
			return true;
		}
		
		if(Math.abs(newRow - currentRow) == 1 && Math.abs(newCol - currentCol) == 2){
			return true;
		}
		
		return false;
	}
	
	public String getColor(){
		return this.color;
	}
	
	public String toString(){
		return color.charAt(0) + "N";
	}

}
