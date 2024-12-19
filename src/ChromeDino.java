import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import java.lang.Object;
import java.util.ArrayList;

//this ChromeDino class has access to all methods and abilities
//of JPanel
public class ChromeDino extends JPanel implements ActionListener, KeyListener {
	//add properties to JPanel
	int boardWidth = 750; //800 in real Frame
	int boardHeight = 300; //200 in real Frame
	boolean gameOver = false;
	boolean tryAgain = false;
	int score = 0;

	
	//what images am i going to use
	Image dinosaurImg;
	Image dinosaurDeadImg;
	Image dinosaurJumpImg;
	Image dinosaurDuck; 
	Image cactus1Img;
	Image cactus2Img;
	Image cactus3Img;

	
	//Only ChromeDino class uses Block so it can be an inner class
	//can be made into a separate class
	public class Block {
		int x;
		int y;
		int width;
		int height;
		Image img;
		
		public Block(int x, int y, int width, int height, Image image) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.img = image;
		}
		
	}
	
	//dinosaur properties in game
	int dinoWidth = 88;
	int dinoHeight = 94;
	//duck state can be either on or off
	//hence the boolean
	boolean isDinoDuck = false;
	int dinoXPosition = 50;
	int dinoYPosition = boardHeight - dinoHeight; 
	Block dinosaur;
	Timer gameLoop;
	
	int velocityX = -12; //to simulate cactus moving to the left
	int velocityY;
	int gravity = 1;
	
	
	
	/**
	We have multiple cactus so put them in arrayList
	Block cactus1;
	Block cactus2;
	Block cactus3;		
	**/
	
	int cactus1Width = 34;
	int cactus2Width = 68;
	int cactus3Width = 102;
	
	int cactusHeight = 70;
	int cactusX = 750;
	int cactusY = boardHeight - cactusHeight;
	ArrayList<Block> obstacles;
	
	//cactus randomly generated
	Timer placeCactusTimer;
	
	public ChromeDino(){
		setPreferredSize(new Dimension(boardWidth,boardHeight));
		setBackground(Color.lightGray);
		setFocusable(true);
		addKeyListener(this);
		//need to do this so visual can be added to window
		//creating ImageIcon Object and giving it the location
		//of what we want
		dinosaurImg = new ImageIcon(getClass().getResource
				("./img/dino-run.gif")).getImage();
		
		dinosaurDeadImg = new ImageIcon(getClass().getResource
				("./img/dino-dead.png")).getImage();
		
		dinosaurJumpImg = new ImageIcon(getClass().getResource
				("./img/dino-jump.png")).getImage();
		
		dinosaurDuck = new ImageIcon(getClass().getResource
				("./img/dino-duck.gif")).getImage();
		
		cactus1Img = new ImageIcon(getClass().getResource
				("./img/cactus1.png")).getImage();
		
		cactus2Img = new ImageIcon(getClass().getResource
				("./img/cactus2.png")).getImage();
		
		cactus3Img = new ImageIcon(getClass().getResource
				("./img/cactus3.png")).getImage();
		
		//initialize the Dino
		dinosaur = new Block(dinoXPosition, dinoYPosition, 
			dinoWidth, dinoHeight, dinosaurImg);
		
		obstacles = new ArrayList<Block>();
		
		//initialize game loop
		//60 frames per 1000ms
		//this refers to class which is technically just a JPanel with a little extra
		gameLoop = new Timer(1000/60, this);
		gameLoop.start();
		
		//adding cactus into game	
		//allows for them in real time 
		//keeps going like while loop
		placeCactusTimer = new Timer(1500, new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				placeCactus();
			}

		});
		placeCactusTimer.start();
			
		
	}
		
		//for adding cactus randomly 
		//this does not draw them into the window
		public void placeCactus() {
			//when obsatcle is hit no more cactus generation
			if(gameOver == true) {
				return;
			}
			
			double placeCactusChance = Math.random(); //0 to 0.9999999
			if(placeCactusChance > 0.90) {
				Block cactus = new Block(cactusX, cactusY, cactus3Width,cactusHeight, cactus3Img);
				obstacles.add(cactus);
			}
			else if(placeCactusChance > 0.70) {
				Block cactus = new Block(cactusX, cactusY, cactus2Width,cactusHeight, cactus2Img);
				obstacles.add(cactus);
			}
			else if (placeCactusChance > 0.40) {
				Block cactus = new Block(cactusX, cactusY, cactus1Width,cactusHeight, cactus1Img);
				obstacles.add(cactus);
			}
			
			//ArrayList will get massive and so performance to need to remove old entities
			//after 'x' amount of cactus
			if(obstacles.size() > 10) {
				obstacles.remove(0);
			}
		}
		
		//making component visible on screen
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			draw(g);
		}
	
		//allow for component to be visible on screen
		public void draw(Graphics g) {
			//only first frame of gif is shown
			//needs a timer for running animation to be showcased correctly
			//showcase dinosaur

			
			if(isDinoDuck == true) {
				dinosaur.img = dinosaurDuck;
			}
			else {
				dinosaur.img = dinosaurImg;

			}
			g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, 
					dinosaur.height, null);
			
			//showcase cactus enemy
			for(int i = 0; i < obstacles.size(); i++) {
				Block enemy = obstacles.get(i);
				g.drawImage(enemy.img, enemy.x, enemy.y, enemy.width, enemy.height, null);
			}
			
			//showcase the player score
			g.setColor(Color.black);
			g.setFont(new Font("Ariel", Font.PLAIN, 32));
			if(gameOver == true) {
				g.drawString("Game over: " + String.valueOf(score), 10, 35);
			}
			else {
				g.drawString(String.valueOf(score), 10, 35);
			}
		}

		public void move() {
			dinosaur.y += velocityY;
			velocityY += gravity;		
				
			//to stop dino from falling into the ground
			if(dinosaur.y > dinoYPosition) {
				dinosaur.y = dinoYPosition;
				velocityY = 0;
				dinosaur.img = dinosaurImg;
			}
			
			for(int i = 0; i < obstacles.size(); i++) {
				Block cactus = obstacles.get(i);
				cactus.x += velocityX;
				
				if(CollisionChecker(dinosaur, cactus)) {
					gameOver = true;
					dinosaur.img = dinosaurDeadImg; 
				}
			}
			
			score++;
		}
		
		//going to be called 60 times per second using game loop timer
		//allow for game components to move by updating frames
		public void actionPerformed(ActionEvent e) {
			move();
			repaint();
			if(gameOver == true) {
				placeCactusTimer.stop();
				gameLoop.stop();
			}
		}
		
		//CODING JUMP MECHANIC
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				//action will not regester on its own 
				//need to add setFocusable() to constructor
				//so method knows where to check
				
				//might need different variable
				//only jump if on the ground
				if(dinosaur.y == dinoYPosition) {
					velocityY = -18;
					dinosaur.img = dinosaurJumpImg;
				}  
				if(gameOver == true) {
					//to fix position if dead dino in air
					dinosaur.y = dinoYPosition;
					//to remove dead dino png
					dinosaur.img = dinosaurImg;
					velocityY = 0;
					obstacles.clear();
					gameOver = false;
					gameLoop.start();
					placeCactusTimer.start();
				}
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				//duck image only shows for a second before it gets repainted
				isDinoDuck = true;
				dinoHeight = 94/2;
				
				
			}	
		}
			
		//not gonna use keyTyped
		public void keyTyped(KeyEvent e) {}

		//not gonna use keyReleased
		public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				//duck image only shows for a second before it gets repainted
				isDinoDuck = false;
				dinoHeight = 94;	
			}	
		}
		
		//detect collisions between 2 objects
		public static boolean CollisionChecker(Block a, Block b) {
			//detect collision between 2 rectangles
			//both objects are treated like rectangles even though they are not
			return 	a.x < b.x + b.width &&
					a.x + a.width > b.x &&
					a.y < b.y + b.height &&
					a.y + a.height > b.y;
		}
		
		
}
