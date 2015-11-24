package maze;

/**
 * MazeSolverEngine1.java
 *
 * @author Abhiroop Tandon (20061667)
 *
 * The program uses backtracking to find the way out of the maze
 * the recursive method is the findPath method which uses the 
 * backtracking strategy.
 */

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

@SuppressWarnings("serial")
public class MazeSolverEngine1 extends JPanel {

	// The maze as a 2-D array of chars:
	public char[][] maze;
	int row, col;

	// stores the points of the way out of the maze
	ArrayList<Point> pathPoints = new ArrayList<Point>();

	// The total length of the path we find through the maze:
	private int totalSteps;

	// Coordinates of the start square and the end square:
	private Point startLocation;
	private Point finishLocation;

	public static final char WALL_CHAR = '#';
	public static final char FREE_CHAR = '.';
	public static final char PATH_CHAR = 'P';
	public static final char START_CHAR = 'o';
	public static final char FINISH_CHAR = '*';

	/**
	 * The constructor reads in the file and stores the maze initializes the
	 * startLocation and finishLocation
	 */
	public MazeSolverEngine1(String location) {
		readFile(location);
		totalSteps = 0;
		startLocation = getStartLocation();
		finishLocation = getFinishLocation();
	}

	/**
	 * solve() calls our recursive method findPath with the starting point as
	 * the parameter to begin finding the way out of the maze from the
	 * startLocation
	 */
	public void solve() {
		totalSteps = 0;
		if (findPath(startLocation)) {
			System.out.println("Solved the maze in " + totalSteps + " steps.");
		} else {
			System.out.println("Could not solve the maze!");
		}
	}

	/**
	 * findPath() is the main method which uses backtracking to find the way out
	 * of the maze. It stores the four adjacent locations in an array and then
	 * checks if those locations are free to move or not
	 */
	private boolean findPath(Point location) {

		// jump out if finish is reached
		if (mazeFinished(location)) {
			return true;
		}

		// else check the adjacent four squares to move
		Point[] adjacentSquares = getAdjacentSquares(location);

		for (Point potentialMove : adjacentSquares) {

			// if the square is free set it to visited and move
			// check the next four locations and keep moving
			// until finish is reached
			if (squareIsFree(potentialMove)) {
				setVisited(potentialMove);

				// recursion
				if (findPath(potentialMove)) {
					return true;
				}

				// if the path didn't turn out right just go back
				exitSquare(potentialMove);
			}
		}
		return false;
	}

	/**
	 * getAdjacentSquares() just stores the next four adjacent squares in an
	 * Array and returns it
	 */
	private Point[] getAdjacentSquares(Point location) {
		Point[] adjacentSquares = new Point[4];

		adjacentSquares[0] = new Point(location.x + 1, location.y);
		adjacentSquares[1] = new Point(location.x, location.y + 1);
		adjacentSquares[2] = new Point(location.x - 1, location.y);
		adjacentSquares[3] = new Point(location.x, location.y - 1);

		return adjacentSquares;
	}

	/**
	 * squareIsFree() checks is the square is free or not, i.e. its not a wall
	 * or is already visited or is out of the array
	 */
	private boolean squareIsFree(Point point) {
		if (point.x < 0 || point.x >= maze.length || point.y < 0
				|| point.y >= maze[point.x].length) {

			return false;
		}

		return (maze[point.x][point.y] == FREE_CHAR || maze[point.x][point.y] == FINISH_CHAR);
	}

	/**
	 * setVisited() sets the current location to visited and stores the
	 * pathPoints
	 */
	private void setVisited(Point point) {
		maze[point.x][point.y] = PATH_CHAR;
		totalSteps++;
		pathPoints.add(point);
	}

	/**
	 * exitSquare() is used to remove the unsuccessful paths by placing the
	 * free_char at that point
	 */
	private void exitSquare(Point point) {
		maze[point.x][point.y] = FREE_CHAR;
		totalSteps--;
		pathPoints.remove(point);
	}

	/**
	 * mazeFinished() checks if finish is reached or not
	 */
	private boolean mazeFinished(Point location) {
		return location.equals(finishLocation);
	}

	/**
	 * printMaze() prints the maze
	 */
	public void printMaze() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				System.out.print(maze[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * getStartLocation() looks for the start locations in the given maze
	 */
	private Point getStartLocation() {
		Point startLocation = findChar(START_CHAR);
		if (startLocation == null) {
			throw new IllegalStateException("Maze has no start square!");
		}
		return startLocation;
	}

	/**
	 * getFinishLocation() looks for the finish point in the maze
	 */
	private Point getFinishLocation() {
		Point finishLocation = findChar(FINISH_CHAR);
		if (finishLocation == null) {
			throw new IllegalStateException("Maze has no finish square!");
		}
		return finishLocation;
	}

	/**
	 * findChar() returns the location of the required square
	 */
	private Point findChar(char c) {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (maze[i][j] == c) {
					return new Point(i, j);
				}
			}
		}

		return null;
	}

	/**
	 * painComponent() is the method which converts our char maze array into an
	 * actual gui based on the characters in the array
	 */
	public void paintComponent(Graphics g) {
		int SquareSize = 20;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (i == startLocation.x && j == startLocation.y) {
					g.setColor(Color.green);
					g.fillOval(j * 20, i * 20, 20, 20);
				} else if (maze[i][j] == '#') {
					g.setColor(Color.black);
					g.fillRect(j * SquareSize, i * SquareSize, SquareSize,
							SquareSize);
				} else if (maze[i][j] == '*') {
					g.setColor(Color.red);
					g.fillOval(j * 20, i * 20, 20, 20);
				} else if (maze[i][j] == 'o') {
					g.setColor(Color.red);
					g.fillOval(j * 15, i * 15, 15, 15);
				} else if (maze[i][j] == 'P') {
					g.setColor(Color.lightGray);
					g.fillRect(j * 20, i * 20, 20, 20);
				} else {
					g.setColor(Color.white);
					g.fillRect(j * SquareSize, i * SquareSize, SquareSize - 1,
							SquareSize - 1);
				}
			}
		}
	}

	/**
	 * readFile() reads the file at the given location and then initializes the
	 * maze accordingly
	 */
	public void readFile(String location) {

		int counter = 0;
		File file = new File(location);

		try {

			Scanner obj = new Scanner(file);
			col = obj.nextInt();
			row = obj.nextInt();

			maze = new char[row][col];
			while (obj.hasNextLine()) {
				String pattern = obj.next();
				maze[counter] = pattern.toCharArray();
				counter++;
			}
			obj.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// printing the maze after reading it in
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				System.out.print(maze[i][j]);
			}
			System.out.println();
		}
	}

	public ArrayList<Point> pathPoints() {
		return pathPoints;
	}
	
	public char[][] getMaze() {
		return maze;
	}

	public void setMaze(char[][] maze) {
		this.maze = maze;
	}
}
