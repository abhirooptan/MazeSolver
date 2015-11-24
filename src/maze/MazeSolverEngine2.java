package maze;

/**
 * MazeSolverEngine2.java
 *
 * @author Abhiroop Tandon (20061667)
 *
 * The program uses backtracking to find the way out of the maze
 * similar to MazeSolverEngine1 but it doesn't removes the unsuccessful
 * paths and keeps repainting the maze with a thread in between for a delay
 * which makes it look like an animation
 * the recursive method is the moveFrom method which uses the 
 * backtracking strategy.
 */

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;

@SuppressWarnings("serial")
class MazeSolverEngine2 extends JPanel implements Runnable {

	final private int squareSize;
	private boolean free;
	int locX, locY;
	int row, col;
	private char[][] maze;

	/**
	 * the constructor initializes the instances and reads the file to
	 * initialize the maze as well
	 */
	public MazeSolverEngine2(String location) {
		squareSize = 20;
		free = false;
		readFile(location);
	}

	/**
	 * painComponent() is the method which converts our char maze array into an
	 * actual gui based on the characters in the array
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[i].length; j++) {
				if (i == locX && j == locY) {
					g.setColor(Color.red);
					g.fillOval(j * squareSize, i * squareSize, squareSize,
							squareSize);
				} else if (maze[i][j] == '#') {
					g.setColor(Color.black);
					g.fillRect(j * squareSize, i * squareSize, squareSize,
							squareSize);
				} else if (maze[i][j] == '*') {
					g.setColor(Color.blue);
					g.fillOval(j * squareSize, i * squareSize, squareSize,
							squareSize);
				} else if (maze[i][j] == 'o') {
					g.setColor(Color.red);
					g.fillOval(j * squareSize, i * squareSize, squareSize,
							squareSize);
				} else if (maze[i][j] == 'P') {
					g.setColor(Color.yellow);
					g.fillOval(j * squareSize, i * squareSize, squareSize,
							squareSize);
				} else {
					g.setColor(Color.white);
					g.fillRect(j * squareSize, i * squareSize, squareSize,
							squareSize);
				}
			}
		}
	}

	/**
	 * run() begins the program and calls our recursive method moveFrom which
	 * starts checking the adjacent blocks of the start location to find the way
	 * out
	 */
	public void run() {
		moveFrom(locX, locY);
		if (!free)
			JOptionPane.showMessageDialog(this, "Solution complete: finish not reachable");
	}

	/**
	 * isGoal() checks if the current square is the finish point or not and
	 * returns accordingly
	 */
	public boolean isGoal(int x, int y) {
		if (maze[x][y] == '*')
			return true;
		else
			return false;
	}

	/**
	 * setVisited() sets the current location as visited so that we don't go
	 * back on that route again
	 */
	public void setVisited(int x, int y) {
		maze[x][y] = 'P';
		locX = x;
		locY = y;
	}

	/**
	 * isWall() checks if the current square is a wall or not and returns
	 * accordingly
	 */
	public boolean isWall(int x, int y) {
		if (maze[x][y] == '#')
			return true;
		else
			return false;
	}

	/**
	 * isVisited() checks if we have already been to the current square or not
	 */
	public boolean isVisited(int x, int y) {
		if (maze[x][y] == 'P')
			return true;
		else
			return false;
	}

	/**
	 * moveFrom() is the main method which uses backtracking to find the way out
	 * of the maze. It stores the four adjacent locations in an array and then
	 * checks if those locations are free to move or not and keeps repainting
	 * the gui again and again until all the squares are finished or finished is
	 * reached
	 */
	private void moveFrom(int x, int y) {
		if (isWall(x, y))
			return;
		if (isVisited(x, y))
			return;
		if (isGoal(x, y)) {
			free = true;
			JOptionPane.showMessageDialog(this, "Solution complete: finish reachable");
		}
		if (!free) {
			setVisited(x, y);
			repaint();
			// putting a delay between two consecutive repaints
			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}

			// checking the adjacent blocks
			moveFrom(x - 1, y);
			moveFrom(x + 1, y);
			moveFrom(x, y - 1);
			moveFrom(x, y + 1);
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
				// setting the starting point
				if (maze[i][j] == 'o') {
					locX = i;
					locY = j;
				}
				System.out.print(maze[i][j]);
			}
			System.out.println();
		}
	}
}