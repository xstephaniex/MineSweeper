
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;


import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 40;
	private static final int GRID_Y = 30;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 10;   
	public static final int MINE = -1;
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	
	
	//TOTAL OF FLAGS THAT CAN BE PUT SAME AS THE TOTAL OF MINES THAT CAN BE PUT
	public int totalOfFlags = 12;
	//STATUS CHECK IF THE PLAYER WON
	public boolean didThePlayerWon = false;

	//TOTAL OF MINES THAT CAN BE PUT
	public int totalMines = 12;
	//HOLDS THE COLOR ON THE GRID IN X AND Y COORDINATES
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	//HOLDS THE MINES THAT ARE GOING TO BE PUT IN X AND Y COORDINATES 
	public int[][] mines = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	//KEEPS TABS IF GRID SELECTED IS HIDDEN OR PRESSED
	public boolean[][]hiddenGrid = new boolean[TOTAL_COLUMNS][TOTAL_ROWS];
	
	//HOLDS NUMBER OF MINES AROUND THE GRID AFTER LOCALIZING THE MINES AROUND
	public int[][] numbers = new int[TOTAL_COLUMNS][TOTAL_ROWS];			

	/////////////////////////////////////////////////
	// CONSTRUCTOR TO INITIALIZE THE MYPANEL CLASS //
	/////////////////////////////////////////////////

	public MyPanel() { 

		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
	
		
	//STARTS A NEW GAME
		NewGame();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		////////////////////////
		//INTERIOR COORDINATES//
		////////////////////////
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;
		////////////////////	
		//BACKGROUND PAINT//
		////////////////////	
		g.setColor(Color.CYAN);
		g.fillRect(x1, y1, width + 1, height + 1);
		////////////////////
		//10X10 GRID	  //	
		////////////////////
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS)));
		}
		////////////////////////////////////////
		//PAINT CELL COLORS IN THIS CASE WHITE//
		///////////////////////////////////////
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS )) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}

		
		////////////////////////////////////////////////////////////////
		//DrAWS THE NUMBER AND PAINTS THE GRIDS THAT HAVE MINES AROUND//
		///////////////////////////////////////////////////////////////

		Font gridFont = new Font("Times New Roman",Font.BOLD,24);

		g.setFont(gridFont);
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				Color c = colorArray[x][y];
				g.setColor(c);
				g.fillRect(GRID_X + x1 + ((INNER_CELL_SIZE + 1) * x) + 1, y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * y) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);


				Color colorNumber = setNumberColor(numbers[x][y]);
				g.setColor(colorNumber);
		if(numbers[x][y]>0 && colorArray[x][y] == Color.lightGray && hiddenGrid[x][y] == false){
			g.drawString("  "+String.valueOf(numbers[x][y]),GRID_X+((INNER_CELL_SIZE+1)*x),y1+(2*GRID_Y)+((INNER_CELL_SIZE)*y));
		}
			}
		
			

		}


	}
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);

		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
	
	/**
	 * @Author Jainel
	 * IT'S GOING TO TELL IF THERE ARE 12 HIDDEN GRIDS IN THE BOARD
	 * 
	 */
	public boolean didThePlayerWon(){
		int TheCellIsHidden = 0;
		
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if(hiddenGrid[x][y]){
					TheCellIsHidden++;
				}
			}
		}
	return (TheCellIsHidden == 12);
	}
	
	/**
	 * @author Stephanie
	 * SET RANDOM MINES INSIDE THE GRID
	 * 
	 */
	public void setRandomMines()
	{
		Random minesRandomized = new Random();

		while(totalMines != 0)
		{

			int x = minesRandomized.nextInt(10);
			int y = minesRandomized.nextInt(10);

			if((mines[x][y] != MINE))
			{
				mines[x][y] = MINE;
				totalMines--;
				System.out.println("(x,y) ("+x+" , "+y+")");
				System.out.println(MINE);
			}
		}
	}
	/**
	 * @author Stephanie
	 * 
	 * IF THE PLAYER LOST IS GONNA SHOW THE MINES
	 * 
	 */
	public void showMines(){

		for(int x=0;x<TOTAL_ROWS;x++)
		{
			for(int y=0;y<TOTAL_COLUMNS;y++)
			{
				if(mines[x][y]==MINE)
				{
					colorArray[x][y] = Color.WHITE;
				}
			}
		}

	}
	/**
	 * @author Stephanie
	 * THIS METHOD LOCATES AND COUNT THE MINES ADJACENT TO THE GRID
	 * @param x THIS TAKES THE X COORDINATE
	 * @param y THIS TAKES THE Y COORDINATE
	 * @return TOTAL OF MINES LOCATED
	 */
	public int getAllTheMinesAround(int x, int y){

		int totalMines = 0;
		int xright;
		int xleft;
		int ytop;
		int ybottom;
		if(x>=9){xright = 9;}else{xright=x+1;}
		if(x== 0){xleft = 0;}else{xleft=x-1;};
		if(y>=9){ybottom = 9;}else{ybottom = y+1;}
		if(y==0){ytop = 0;}else{ytop = y-1;}

		for(int a= xleft;a<=xright;a++ ){
			for(int b = ytop;b<=ybottom;b++){
				if((a==x && b == y)|| mines[x][y] == MINE){
					continue;
				}else{
					if(mines[a][b] == MINE){
						totalMines++;
					}
				}
			}
		}
		System.out.println(totalMines);
		return totalMines;
	}
	public void FloodFill(int x, int y){

		if( x < 0 || x > 9 || y < 0 || y > 9) return;

		if (numbers[x][y] == 0 && hiddenGrid[x][y] && colorArray[x][y] != Color.red){
			hiddenGrid[x][y] = false;
			colorArray[x][y] = Color.LIGHT_GRAY;
			if(didThePlayerWon()){
				didThePlayerWon = true;
				playerWonTheGame();
			}
			FloodFill(x + 1, y);
			FloodFill(x - 1, y );
			FloodFill(x, y - 1);
			FloodFill(x, y + 1);
			FloodFill(x + 1, y + 1);
			FloodFill(x + 1, y - 1);
			FloodFill(x - 1, y - 1);
			FloodFill(x - 1, y + 1);

		}else{
			if (numbers[x][y] > 0 && hiddenGrid[x][y] && mines[x][y] == 0){
				selectGrid(x, y);
			}
			return;
		}
	}
	/**
	 * @author Stephanie
	 * 
	 * @param DEPENDING ON THE NUMBER ITS GONNA BE ITS COLOR
	 * @return numberColor that represents the color of the number. 
	 */
	public Color setNumberColor(int number){
		Color numberColor = null;

		switch(number){

		case 1	: numberColor = Color.BLACK;
		break;
		case 2	: numberColor = Color.red;
		break;
		case 3	: numberColor = Color.YELLOW;
		break;
		case 4	: numberColor = Color.DARK_GRAY;
		break;
		case 5	: numberColor = Color.blue;
		break;
		
		}
		return numberColor;	
	}
	/**
	 * @author Stephanie
	 * 
	 * RESETS THE BOARD AND STARTS A NEW GAME
	 */
	public void resetBoard(){
		for(int x=0;x<TOTAL_ROWS;x++)
		{
			for(int y=0;y<TOTAL_COLUMNS;y++)
			{
				mines[x][y] = 0;
			}
		}
		
		for(int x=0;x<TOTAL_ROWS;x++)
		{
			for(int y=0;y<TOTAL_COLUMNS;y++)
			{
				numbers[x][y]=0;
			}
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {  
			for (int y = 0; y < TOTAL_ROWS; y++) {
				hiddenGrid[x][y]=true;
			}
			
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {  
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.darkGray;
			}
			
		}
		totalMines = 12;
		didThePlayerWon = false;
		totalOfFlags = 12;
	}
	/**
	 * @author Stephanie & Jainel & Steven
	 * THIS METHOD IS USED WHEN THE PLAYR SELECTS A GRID
	 */
	public void selectGrid(int x, int y){
		

			if((hiddenGrid[x][y] && colorArray[x][y] != Color.YELLOW)){ //Grid is hidden and isn't a flag
				if(mines[x][y] == MINE){
					playerLostTheGame();
				}else{
					if(numbers[x][y] > 0){
						colorArray[x][y] = Color.lightGray;
						hiddenGrid[x][y] = false;
						if(didThePlayerWon()){
							didThePlayerWon = true;
							playerWonTheGame();
						}
					}else if(numbers[x][y] == 0){
						FloodFill(x, y);
					}
				}
			}
		}
		
	
	/**
	 * @author Stephanie & Jainel & Steven
	 * STARTS THE GAME
	 */
	public void NewGame(){
		resetBoard();
		setRandomMines();
		
		
		for (int x = 0; x < TOTAL_COLUMNS; x++) {  
			for (int y = 0; y < TOTAL_ROWS; y++) {
			
			
				numbers[x][y] = getAllTheMinesAround(x,y);
				
			}
			
			}
		
	}
	/**
	 * IF THE PLAYER LOST THE GAME THE PLAYER HAS THE OPTION TO PLAY AGAIN
	 */
	public void playerLostTheGame(){
		showMines();
		repaint();
		int confirmation = JOptionPane.showConfirmDialog(null, "You lost! Want to play again?", null, JOptionPane.YES_NO_OPTION);
		if(confirmation == JOptionPane.YES_OPTION){
			JOptionPane.showMessageDialog(null, "Let's get ready!");
			resetBoard();
			NewGame();
		}
		else{
			JOptionPane.showMessageDialog(null, "Thank You for playing!");
			System.exit(0);
		}
	}
	/**
	 * 
	 * IF THE PLAYER WON THE GAME THE PLAYER HAS THE OPTION TO PLAY AGAIN
	 */
	public void playerWonTheGame(){

		showMines();
		repaint();

		int buttonPressed = JOptionPane.showConfirmDialog(null, "You win! Want to play again?", null, JOptionPane.YES_NO_OPTION);
		if(buttonPressed == JOptionPane.YES_OPTION){
			JOptionPane.showMessageDialog(null, "Let's get started");
			resetBoard();
			NewGame();
		}
		else{
			JOptionPane.showMessageDialog(null, "Thank You for playing!");
			System.exit(0);
		}
	}

			  
	


}