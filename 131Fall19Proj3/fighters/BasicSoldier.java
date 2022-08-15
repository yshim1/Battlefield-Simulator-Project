package fighters;

import framework.BattleField;
import framework.Random131;

public class BasicSoldier {
	public final static int INITIAL_HEALTH = 10; 
	public final static int ARMOR = 10; 
	public final static int STRENGTH = 40; 
	public final static int SKILL = 40; 
	public final static int UP = 0;
	public final static int RIGHT = 1;
	public final static int DOWN = 2;
	public final static int LEFT = 3;
	public final static int UP_AND_RIGHT = 4;
	public final static int DOWN_AND_RIGHT = 5;
	public final static int DOWN_AND_LEFT = 6;
	public final static int UP_AND_LEFT = 7;
	public final static int NEUTRAL = -1;
	public final BattleField grid;
	public int row, col;
	public int health;
	public final int team;

	public BasicSoldier(BattleField gridIn, int teamIn, int rowIn, int colIn) {
		grid = gridIn;
		team = teamIn;
		row = rowIn;
		col= colIn;
		health = 10;
	}

	public boolean canMove() {
		int below = grid.get(row + 1, col);
		int above = grid.get(row - 1, col);
		int right = grid.get(row, col + 1);
		int left = grid.get(row, col - 1);
		if(below == BattleField.EMPTY || right == BattleField.EMPTY 
				|| left == BattleField.EMPTY || above == BattleField.EMPTY ) {
			return true;
		} else {
			return false;
		}
	}
	public int numberOfEnemiesRemaining() {
		int height = grid.getRows();
		int width = grid.getCols();
		int count = 0;
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				int content = grid.get(row, col);
				if((team == BattleField.BLUE_TEAM) && 
						(content == BattleField.RED_TEAM)){
					count += 1; 
				}
				else if((team == BattleField.RED_TEAM) &&
						(content == BattleField.BLUE_TEAM)) {
					count += 1;
				}
			}
		}
		return count;
	}
	public int getDistance(int destinationRow, int destinationCol) {
		int xDistance = Math.abs(row - destinationRow);
		int yDistance = Math.abs(col - destinationCol);
		int totalDistance = xDistance + yDistance;
		return totalDistance;
	}
	public int getDirection(int destinationRow, int destinationCol) {
		if((destinationRow > row) && (destinationCol == col)) {
			return DOWN;
		}
		else if((destinationRow < row) && (destinationCol == col)){
			return UP;
		}
		else if((destinationRow == row) && (destinationCol > col)) {
			return RIGHT;
		}
		else if((destinationRow == row) && (destinationCol < col)) {
			return LEFT;
		}
		else if((destinationRow > row) && (destinationCol < col)) {
			return DOWN_AND_LEFT;
		}
		else if((destinationRow > row) && (destinationCol > col)) {
			return DOWN_AND_RIGHT;
		}
		else if((destinationRow < row) && (destinationCol > col)) {
			return UP_AND_RIGHT;
		}
		else if((destinationRow < row) && (destinationCol < col)) {
			return UP_AND_LEFT;
		}
		else {
			return NEUTRAL;
		}
	}
	public int getDirectionOfNearestFriend() {
		int height = grid.getRows();
		int width = grid.getCols();
		int max = height + width;
		int direction = NEUTRAL;
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				int content = grid.get(row, col);
				if(team == BattleField.BLUE_TEAM && content == BattleField.BLUE_TEAM) {
					int distance = getDistance(row, col);
					if(distance < max && distance != 0 ) {
						max = getDistance(row,col);
						direction = getDirection(row,col);
					}
				}
				else if(team == BattleField.RED_TEAM && content == BattleField.RED_TEAM) {
					int distance = getDistance(row,col);
					if(distance < max && distance != 0) {
						max = getDistance(row,col);
						direction = getDirection(row,col);
					}
				}
			}
		}
		return direction;
	}
	public int countNearbyFriends(int radius) {
		int width = grid.getCols();
		int height = grid.getRows();
		int count = 0;
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				int content = grid.get(row, col);
				if(team == BattleField.BLUE_TEAM && content == BattleField.BLUE_TEAM) {
					int distance = getDistance(row,col);
					if(distance != 0  && distance <= radius) {
						count += 1;
					}
				}
				else if(team == BattleField.RED_TEAM && content == BattleField.RED_TEAM) {
					int distance = getDistance(row,col);
					if(distance != 0 && distance <= radius) {
						count += 1;
					}
				}
			}
		}
		return count;
	}
	public int getDirectionOfNearestEnemy(int radius) {
		int height = grid.getRows();
		int width = grid.getCols();
		int direction = NEUTRAL;
		int max = height + width;
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				int content = grid.get(row, col);
				if(team == BattleField.BLUE_TEAM && content == BattleField.RED_TEAM) {
					int distance = getDistance(row,col);
					if( distance < max && distance <= radius) {
						max = getDistance(row,col);
						direction = getDirection(row,col);
					}
				}
				else if(team == BattleField.RED_TEAM && content == BattleField.BLUE_TEAM) {
					int temp = getDistance(row,col);
					if( temp < max && temp <= radius) {
						max = getDistance(row,col);
						direction = getDirection(row,col);
					}
				}
			}
		}
		return direction;
	}

	public void performMyTurn() {
		int above = grid.get(row - 1, col);
		int below = grid.get(row + 1, col);
		int right = grid.get(row, col + 1);
		int left = grid.get(row, col - 1);
		if (team == BattleField.BLUE_TEAM && above == BattleField.RED_TEAM) {
			grid.attack(row - 1, col);
		}
		else if(team == BattleField.BLUE_TEAM && below == BattleField.RED_TEAM) {
			grid.attack(row + 1 , col);
		}
		else if(team == BattleField.BLUE_TEAM && right == BattleField.RED_TEAM) {
			grid.attack(row, col + 1);
		}
		else if(team == BattleField.BLUE_TEAM && left == BattleField.RED_TEAM) {
			grid.attack(row, col - 1);
		}
		else if(team == BattleField.RED_TEAM && above == BattleField.BLUE_TEAM) {
			grid.attack(row -1, col);
		}
		else if(team == BattleField.RED_TEAM && below == BattleField.BLUE_TEAM) {
			grid.attack(row + 1, col);
		}
		else if(team == BattleField.RED_TEAM && right == BattleField.BLUE_TEAM) {
			grid.attack(row, col + 1);
		}
		else if(team == BattleField.RED_TEAM && left == BattleField.BLUE_TEAM) {
			grid.attack(row, col -1);
		}
		else if (above == BattleField.EMPTY) {
			row = row - 1;
		}
		else if (below == BattleField.EMPTY) {
			row = row + 1;
		}
		else if (right == BattleField.EMPTY) {
			col = col + 1;
		}
		else if (left == BattleField.EMPTY) {
			col = col - 1;
		}
		else {
		}
	}
}


