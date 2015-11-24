package maze;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * A graphical user interface for the maze app. No work is being done here. This
 * class is responsible just for putting up the display on screen. It then
 * refers to the "MazeSolver" and "MazeSolver2" to do all the real work.
 * 
 * @author Abhiroop Tandon (20061667)
 * @version 11 March 2015
 */

public class MazeSolverUI implements ActionListener {

	private JFrame frame;
	private JTextField name;
	ArrayList<Point> points = new ArrayList<Point>();
	int counter = 0;

	public MazeSolverUI() {

		makeFrame();
		frame.setVisible(true);
	}

	// GUI Methods
	/**
	 * Make this interface visible again. (Has no effect if it is already
	 * visible.)
	 */
	public void setVisible(boolean visible) {
		frame.setVisible(visible);
	}

	private void makeFrame() {
		frame = new JFrame("Maze Solver");

		JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));

		name = new JTextField();
		JPanel buttonPanel = new JPanel(new GridLayout(7, 1));
		buttonPanel.add(new JLabel("Enter Maze Name"));
		buttonPanel.add(name);
		
		// Adding buttons
		addButton(buttonPanel, "Load Maze");
		addButton(buttonPanel, "Start");
		addButton(buttonPanel, "Step");
		addButton(buttonPanel, "Toggle Animation");
		addButton(buttonPanel, "Shortest Path");
		JPanel displayPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.WEST);
		contentPane.add(displayPanel, BorderLayout.CENTER);
		frame.pack();
	}

	/**
	 * Add a button to the button panel.
	 */
	private void addButton(Container panel, String buttonText) {
		JButton button = new JButton(buttonText);
		button.addActionListener(this);
		panel.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		String action = event.getActionCommand();
		MazeSolverEngine2 mazeAnim = null;
		MazeSolverEngine1 solver = null;

		JFrame world = new JFrame();
		
		// setting the actions of each button
		if (action.equals("Load Maze")) {
			mazeAnim = new MazeSolverEngine2(getName());
			solver = new MazeSolverEngine1(getName());
		}
		if (action.equals("Start")) {
			counter = 0;
			solver = new MazeSolverEngine1(getName());
			solver.solve();
			points = new ArrayList<Point>();
			points = solver.pathPoints();
			solver = new MazeSolverEngine1(getName());
			world.setSize(solver.row*30, solver.col*20);
			world.setTitle("Maze solver");
			world.setContentPane(solver);
			world.setVisible(true);
		}
		if (action.equals("Step")) {
			solver = new MazeSolverEngine1(getName());
			for (int i = 0; i <= counter; i++) {
				solver.maze[points.get(i).x][points.get(i).y] = 'y';
				System.out
						.println(solver.maze[points.get(i).x][points.get(i).y]);
				System.out.println(counter);
			}
			if (counter < (points.size() - 1))
				counter++;
			else
				JOptionPane.showMessageDialog(world, "Solution complete: finish reachable");
			solver.printMaze();

			world.repaint();
			world.revalidate();
		}
		if (action.equals("Toggle Animation")) {
			mazeAnim = new MazeSolverEngine2(getName());
			world.setSize(mazeAnim.row*30, mazeAnim.col*20);
			world.setTitle("Maze solver");
			world.setContentPane(mazeAnim);
			world.setVisible(true);
			Thread gameThread = new Thread(mazeAnim);
			gameThread.start();
		}
		if (action.equals("Shortest Path")){
			solver = new MazeSolverEngine1(getName());
			solver.solve();
			points = solver.pathPoints();
			world.setSize(solver.row*30, solver.col*20);
			world.setTitle("Maze solver");
			world.setContentPane(solver);
			world.setVisible(true);
		}
		
	}

	public String getName() {
		return name.getText();
	}

	public static void main(String args[]) {
		new MazeSolverUI();
	}
}
