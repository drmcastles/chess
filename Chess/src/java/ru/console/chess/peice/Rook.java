package ru.console.chess.peice;

public class Rook extends Piece{

	public String color;
	
	public Rook(String color){
		this.color = color;
	}
	
	public boolean validateMove(Piece[][] board, int currentRow, int currentCol, int newRow, int newCol) {
		if(currentRow != newRow && currentCol != newCol){
			return false;
		}

		int offset;
		
		if(currentRow != newRow){
			if(currentRow < newRow){
				offset = 1;
			}else{
				offset = -1;
			}
			
			for(int x = currentRow + offset; x != newRow; x += offset){
				if(board[x][currentCol] != null){
					return false;
				}
			}
		}

		if(currentCol != newCol){
			if(currentCol < newCol){
				offset = 1;
			}else{
				offset = -1;
			}
			
			for(int x = currentCol + offset; x != newCol; x += offset){
				if(board[currentRow][x] != null){
					return false;
				}
			}
		}
		return true;
	}
	
	public String getColor(){
		return this.color;
	}
	
	public String toString(){
		return color.charAt(0) + "R";
		
	}

}
