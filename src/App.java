/**
 * No Internet Chrome Browser Dinosaur game
 * The game takes place in a 2d window and interface
 * 
 * 
 * 
 */


import javax.swing.*;

public class App {
	public static void main(String[] args) {
		//dimensions for 2d window for game
		int boardWidth = 750;
		int boardLength = 300;
		
		//initializing the window
		JFrame display = new JFrame("Dinosaur Game");
		display.setSize(boardWidth, boardLength);
		
		//BASIC PROPERTIES
		//make visible
		//should be made visible at bottom
		//after adding all important components
		//display.setVisible(true);
		
		//where should the display open up (Middle/Top/Bottom)
		display.setLocationRelativeTo(null);
		//don't want the user to resize the window
		display.setResizable(false);
		//to close the window by clicking 'x'
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add things to the window
		//by using JPanel
		ChromeDino dino = new ChromeDino();
		
		display.add(dino);
		display.pack();
		
		//we want ActionListener to target the dino object
		dino.requestFocus();
		display.setVisible(true);
		
	}
}
