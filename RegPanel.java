package thesnake;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
/**
 * In this class we will be:
 * - Creating The Snake and it's Apple.
 * - Let The Snake find it's path by itself.
 * - Applying the A* Method.
 */
public class RegPanel extends JPanel implements ActionListener{
    final int SCREEN_WIDTH;
    final int SCREEN_HEIGHT;
    static final int UNIT_SIZE = 25;
    final int GAME_UNITS;
    static final int DELAY = 45;
    final int x[];
    final int y[];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JFrame frame;
    
    public RegPanel() {
        SCREEN_WIDTH = 400;
	SCREEN_HEIGHT = 600;
	GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
	this.x = new int[GAME_UNITS];
	this.y = new int[GAME_UNITS];
	this.frame = frame;
	random = new Random();
	setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
	setBackground(Color.BLACK);
	setFocusable(true);
	startGame();
    }
     /**
     * This method will initialize the game by:
     * - generating apples by calling newApple() method.
     * - assign a true value for running to Move the snake.
     * - Start the timer for how fast our snake is going to move.
     */
    public void startGame() {
        newApple();
	running = true;
	timer = new Timer(DELAY,this);
	timer.start();
    }
    
    //This method will display all the drawn objects we created in draw() method.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
	draw(g);
    }
    
    //Drawing all the game objects
    public void draw(Graphics g) {
        if (running){
            //Drawing the Grids
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            //Drawing the Apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            
            //Drawing the Sanke
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0){
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    g.setColor(Color.CYAN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            
            //Score
            g.setColor(Color.white);
            g.setFont( new Font("Sans Serif",Font.BOLD, 20));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }
    }
    
    /**
     * This method will generate a new apple every time its eaten
     * also, it will track the selected path
     */
    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;	
	appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;	
	fixApple();
    }
    
    /**
     * This method checks if the new location of the apple 
     intersects with the snake's body. if it does then it 
     recall the new apple method and tries again until the apple
     isn't within the snake.
     */
    private void fixApple() {
        for(int i = bodyParts; i>0; i--) {
            if(appleX == x[i] && appleY == y[i]) {
                newApple();
            }
        }
    }
    
    //This Method will make the snake move around the board.
    public void move(){
        for(int i = bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
		y[0] = y[0] + UNIT_SIZE;
		break;
            case 'L':
		x[0] = x[0] - UNIT_SIZE;
		break;
            case 'R':
		x[0] = x[0] + UNIT_SIZE;
		break;
        }
    }
    
    /**
     * This method checks if the snake came across the apple? 
     * if yes, then increase the snake body part.
     */
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    
    //Prevent the snake from any collisions.
    public void checkCollisions() {
        //checks if head collides with body
        for(int i = bodyParts;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
	//check if head touches right border
	if(x[0] > SCREEN_WIDTH) {
            running = false;
	}
	//check if head touches top border
	if(y[0] < 0) {
            running = false;
	}
	//check if head touches bottom border
	if(y[0] > SCREEN_HEIGHT) {
            running = false;
	}
		
	if(!running) {
            timer.stop();
	}
    }
    //Will print the Final Score and The game over statement.
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.white);
	g.setFont( new Font("Sans Serif",Font.BOLD, 20));
	FontMetrics metrics1 = getFontMetrics(g.getFont());
	g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
	//Game Over text
	g.setColor(Color.PINK);
	g.setFont( new Font("Sans Serif",Font.BOLD, 50));
	FontMetrics metrics2 = getFontMetrics(g.getFont());
	g.drawString("GAME OVER!", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER!"))/2, SCREEN_HEIGHT/2);
    }
    
    public void actionPerformed(ActionEvent event) {
        if(running) {
            pathFinder();
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    
    //This method will lead the snake alone through the board
    private void pathFinder() {
        int hCostA = 0;
	int hCostB = 0;
	int hCostC = 0;
	int xDistance;
	int yDistance;
	boolean blocked = false;
	int fCostA = 999999999;
	int fCostB = 999999999;
	int fCostC = 999999999;
        
        switch(direction) {
            case 'U':
                hCostA = 0;
		hCostB = 0;
		hCostC = 0;
                // If space to go up
                if (y[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = bodyParts; i>0; i--) {
                        if((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    
                    if (blocked != true) {
                        //GOING UP
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
			yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
			if (yDistance != 0) {
			hCostA = 4;
                        }
                        hCostA+= (xDistance * 10) + (yDistance * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }
                // If space to go left
                if(x[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for(int i = bodyParts; i>0; i--) {
                        if((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked != true) {
                        // Going left
                        xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
			yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            hCostB = 4;
                        }
                        hCostB+= (xDistance * 10) + (yDistance * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }
                // If space to go right
                if(x[0] + UNIT_SIZE < SCREEN_WIDTH) {
                    // If no body parts blocking
                    for(int i = bodyParts; i>0; i--) {
                        if((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    
                    if (blocked != true) {
                        // Going right
                        xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            hCostC = 4;
                        }
                        hCostC+= (xDistance * 10) + (yDistance * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }
                
                if(fCostA <= fCostB && fCostA <= fCostC) {
                    direction = 'U';
                }
                else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction = 'L';
                } else if(fCostC < fCostB && fCostC < fCostA) {
                    direction = 'R';
                }
                
                fCostA = 999999999;
		fCostB = 999999999;
		fCostC = 999999999;
                
                break;
            
            
            case 'D':
                hCostA = 0;
                hCostB = 0;
		hCostC = 0;
                
                // If space to go down
                if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked != true) {
                        // Going down
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
			yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
			if (yDistance != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance * 10) + (yDistance * 10);
			fCostA = hCostA + 10;
                    }
                    blocked = false;
                }
                // If space to go left
                if (x[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    
                    if (blocked != true) {
                        // Going left
                        xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance * 10) + (yDistance * 10);
			fCostB = hCostB + 14;
                    }
                    blocked = false;
                }
                // If space to go right
                if (x[0] + UNIT_SIZE < SCREEN_WIDTH) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    // Going right
                    if (blocked != true) {
                        xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
                        yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
                        if (yDistance != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance * 10) + (yDistance * 10);
			fCostC = hCostC + 14;
                    }
                    
                    blocked = false;
                }
                
                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction = 'D';
                }
                else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction = 'L';
                }
                else if (fCostC < fCostB && fCostC < fCostA) {
                    direction = 'R';
                }
                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;
                
                break;
            
            case 'L':
                hCostA = 0;
		hCostB = 0;
		hCostC = 0;
                // If space to go left
                if (x[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] - UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    
                    if (blocked != true) {
                        // Going left
                        xDistance = Math.abs((appleX - (x[0] - UNIT_SIZE)) / UNIT_SIZE);
			yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
			if (yDistance != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance * 10) + (yDistance * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }
                
                // If space to go down
                if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    
                    if (blocked != true) {
                        // Going down
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
			yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
			if (yDistance != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance * 10) + (yDistance * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }
                // If space to go up
                if (y[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked != true) {
                        // Going up
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
			yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
			if (yDistance != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance * 10) + (yDistance * 10);
			fCostC = hCostC + 14;
                    }
                    blocked = false;
                }
                
                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction = 'L';
                }
                else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction = 'D';
                }
                else if (fCostC < fCostB && fCostC < fCostA) {
                    direction = 'U';
                }
                
                fCostA = 999999999;
                fCostB = 999999999;
                fCostC = 999999999;
                
                break;
            
            case 'R':
                hCostA = 0;
		hCostB = 0;
		hCostC = 0;
                
                // If space to go right
                if (x[0] + UNIT_SIZE < SCREEN_WIDTH) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] + UNIT_SIZE == x[i]) && (y[0] == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked != true) {
                        // Going right
                        xDistance = Math.abs((appleX - (x[0] + UNIT_SIZE)) / UNIT_SIZE);
			yDistance = Math.abs((appleY - y[0]) / UNIT_SIZE);
			if (yDistance != 0) {
                            hCostA = 4;
                        }
                        hCostA += (xDistance * 10) + (yDistance * 10);
                        fCostA = hCostA + 10;
                    }
                    blocked = false;
                }
                
                // If space to go down
                if (y[0] + UNIT_SIZE < SCREEN_HEIGHT) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] + UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked != true) {
                        // Going down
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
			yDistance = Math.abs((appleY - (y[0] + UNIT_SIZE)) / UNIT_SIZE);
			if (yDistance != 0) {
                            hCostB = 4;
                        }
                        hCostB += (xDistance * 10) + (yDistance * 10);
                        fCostB = hCostB + 14;
                    }
                    blocked = false;
                }
                // If space to go up
                if (y[0] - UNIT_SIZE >= 0) {
                    // If no body parts blocking
                    for (int i = bodyParts; i > 0; i--) {
                        if ((x[0] == x[i]) && (y[0] - UNIT_SIZE == y[i])) {
                            blocked = true;
                            break;
                        }
                    }
                    if (blocked != true) {
                        // Going up
                        xDistance = Math.abs((appleX - x[0]) / UNIT_SIZE);
			yDistance = Math.abs((appleY - (y[0] - UNIT_SIZE)) / UNIT_SIZE);
			if (yDistance != 0) {
                            hCostC = 4;
                        }
                        hCostC += (xDistance * 10) + (yDistance * 10);
                        fCostC = hCostC + 14;
                    }
                    blocked = false;
                }
                
                if (fCostA <= fCostB && fCostA <= fCostC) {
                    direction = 'R';
                }
                else if (fCostB < fCostA && fCostB <= fCostC) {
                    direction = 'D';
                }
                else if (fCostC < fCostB && fCostC < fCostA) {
                    direction = 'U';
                }
                
                fCostA = 999999999;
		fCostB = 999999999;
		fCostC = 999999999;
                break;
        }
    }
}