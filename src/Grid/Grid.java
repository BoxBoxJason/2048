package Grid;

import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import Game.GameInterface;

public class Grid {
	
	private int[][] grid;
	private int size;
	private static int[] possibleValues = {2,2,2,2,2,2,2,2,2,4};
	private GameInterface game;
	
	public Grid(int size,GameInterface game) {
		this.grid = new int[size][size];
		this.size = size;
		this.game = game;
		this.fillRandomEmptySquare();
	}
	
	private void fillRandomEmptySquare() {
		if(! this.checkIfGridFull()) {
			int randI = ThreadLocalRandom.current().nextInt(0,this.size);
			int randJ = ThreadLocalRandom.current().nextInt(0,this.size);
			while(this.grid[randI][randJ] != 0) {
				randI = ThreadLocalRandom.current().nextInt(0,this.size);
				randJ = ThreadLocalRandom.current().nextInt(0,this.size);
			}
			Integer value = ThreadLocalRandom.current().nextInt(0,this.size);
			this.grid[randI][randJ] = Grid.possibleValues[value];
		}
	}
	
	
	public void shiftRight() {
		int[][] newGrid = new int[this.size][this.size];
		for(int i=0;i<this.size;i++){
			int[] gridLine = this.grid[i];
			ArrayList<Integer> line = new ArrayList<Integer>();
			ArrayList<Integer> newLine = new ArrayList<Integer>();
			for(int num : gridLine) {
				if(num != 0) {
					line.add(num);
				}
			}
			int p = line.size();
			while(p>0) {
				if(line.get(p-1) != 0) {
					if(p-2> -1 && line.get(p-1).equals(line.get(p-2))) {
						newLine.add(0,2 * line.get(p-1));
						this.game.addToScore(2 * line.get(p-1));
						p-=2;
					}
					else {
						newLine.add(0,line.get(p-1));
						p-=1;
					}
				}
			}
			int size = newLine.size();
			for(int m=0;m< this.size - size ;m++) {
				newLine.add(0,0);
			}
			for(int m=0;m<this.size;m++) {
				newGrid[i][m] = newLine.get(m);
			}
		}
		boolean noMoves = newGrid.equals(this.grid);
		this.grid = newGrid;
		if( ! noMoves) {
			this.fillRandomEmptySquare();
		}
	}
	
	public void shiftLeft() {
		int[][] newGrid = new int[this.size][this.size];
		for(int i=0;i<this.size;i++){
			int[] gridLine = this.grid[i];
			ArrayList<Integer> line = new ArrayList<Integer>();
			ArrayList<Integer> newLine = new ArrayList<Integer>();
			for(int num : gridLine) {
				if(num != 0) {
					line.add(num);
				}
			}
			int p=0;
			while(p<line.size()) {
				if(line.get(p) != 0) {
					if(p+1 < line.size() && line.get(p).equals(line.get(p+1))) {
						newLine.add(2 * line.get(p));
						this.game.addToScore(2 * line.get(p));
						p+=2;
					}
					else {
						newLine.add(line.get(p));
						p+=1;
					}
				}
			}
			int size = newLine.size();
			for(int m=0;m< this.size - size ;m++) {
				newLine.add(0);
			}
			for(int m=0;m<this.size;m++) {
				newGrid[i][m] = newLine.get(m);
			}
		}
		boolean noMoves = newGrid.equals(this.grid);
		this.grid = newGrid;
		if( ! noMoves) {
			this.fillRandomEmptySquare();
		}
	}
	
	
	public void shiftUp() {
		int[][] newGrid = new int[this.size][this.size];
		for(int j=0;j<this.size;j++){
			ArrayList<Integer> line = new ArrayList<Integer>();
			ArrayList<Integer> newLine = new ArrayList<Integer>();
			for(int i=0;i<this.size;i++) {
				if(this.grid[i][j] != 0) {
					line.add(this.grid[i][j]);
				}
			}
			int p=0;
			while(p<line.size()) {
				if(line.get(p) != 0) {
					if(p+1 < line.size() && line.get(p).equals(line.get(p+1))) {
						newLine.add(2 * line.get(p));
						this.game.addToScore(2 * line.get(p));
						p+=2;
					}
					else {
						newLine.add(line.get(p));
						p+=1;
					}
				}
			}
			int size = newLine.size();
			for(int m=0;m< this.size - size ;m++) {
				newLine.add(0);
			}
			for(int i=0;i<this.size;i++) {
				newGrid[i][j] = newLine.get(i);
			}
		}
		boolean noMoves = newGrid.equals(this.grid);
		this.grid = newGrid;
		if( ! noMoves) {
			this.fillRandomEmptySquare();
		}
	}
	
	
	
	public void shiftDown() {
		int[][] newGrid = new int[this.size][this.size];
		for(int j=0;j<this.size;j++){
			ArrayList<Integer> line = new ArrayList<Integer>();
			ArrayList<Integer> newLine = new ArrayList<Integer>();
			for(int i=0;i<this.size;i++) {
				if(this.grid[i][j] != 0) {
					line.add(this.grid[i][j]);
				}
			}
			int p = line.size();
			while(p>0) {
				if(line.get(p-1) != 0) {
					if(p-2> -1 && line.get(p-1).equals(line.get(p-2))) {
						newLine.add(0,2 * line.get(p-1));
						this.game.addToScore(2 * line.get(p-1));
						p-=2;
					}
					else {
						newLine.add(0,line.get(p-1));
						p-=1;
					}
				}
			}
			int size = newLine.size();
			for(int m=0;m< this.size - size ;m++) {
				newLine.add(0,0);
			}
			for(int i=0;i<this.size;i++) {
				newGrid[i][j] = newLine.get(i);
			}
		}
		boolean noMoves = newGrid.equals(this.grid);
		this.grid = newGrid;
		if( ! noMoves) {
			this.fillRandomEmptySquare();
		}
	}
	
	public String toString() {
		String str = "";
		for(int i=0;i<this.size;i++) {
			for(int j=0;j<this.size;j++) {
				str += String.valueOf(this.grid[i][j]);
			}
			str += "\n";
		}
		return(str);
	}
	
	private boolean checkIfGridFull() {
		boolean full = true;
		for(int i=0;i<this.size;i++) {
			for(int j=0;j<this.size;j++) {
				if(this.grid[i][j] == 0) {
					full = false;
					break;
				}
			}
		}
		return(full);
	}
	public int getSquare(int i,int j) {return(this.grid[i][j]);}
}
