import java.util.Random;
import java.util.Scanner;
import java.awt.*;	
import java.awt.Color;


import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.io.IOException;


@SuppressWarnings("serial")
public class BackgammonGame extends JFrame
{

	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 698;
	public static final int IMAGE_WIDTH = 700;
	public static final int IMAGE_HEIGHT = 698;
	public static final int IMAGE_OFFSET = 0;
	public static final int BOARD_WIDTH_OFFSET = 60;
	public static final int BOARD_HEIGHT_OFFSET = 60;
	public static final int RADIUS = 17;

	public static Position[] board;
	public static int roll1;
	public static int roll2;
	public static boolean doubles = false;
	public static boolean doublesRoll1 = false;
	public static boolean doublesRoll2 = false;
	public static String currentPlayer;
	public static Position island;
	public static final int A_STORE = 0;
	public static final int B_STORE = 25;

	public static JButton roll1Button;
	public static JButton roll2Button;
	static int diePicked = 0;
	static int startLoc = -2;

	public static void main(String[] args) 
	{
		//Scanner console = new Scanner (System.in);

		// C - Create and display the window.
		final BackgammonGame gp = new BackgammonGame();
		gp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gp.setTitle("Backgammon");			// Optional title
		gp.setSize(1000, 698);					// Set the width and height of the window
		gp.setLayout(new FlowLayout());
		gp.setVisible(true);					// Display the window

		boolean done = false;
		while(!done)
		{
			initializeBoard();
			//Make Buttons
			for (int i = 13; i < 19; i++)
			{
				JButton b = new JButton();
				final int num = i;
				b.setText("" + i);
				b.setSize(50,20);
				int x = BOARD_WIDTH_OFFSET + (i%12 - 1) * 50;
				b.setLocation(x,BOARD_HEIGHT_OFFSET-50);
				gp.getContentPane().add(b);
				b.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e)
					{
						startLoc = num;
					}
				});  	
			}
			for (int i = 19; i < B_STORE; i++)
			{
				JButton b = new JButton();
				final int num = i;
				b.setText("" + i);
				b.setSize(50,20);
				int x = BOARD_WIDTH_OFFSET + IMAGE_WIDTH/2 - 40 + (i%18 - 1) * 50;
				b.setLocation(x,BOARD_HEIGHT_OFFSET-50);
				gp.getContentPane().add(b);
				b.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e)
					{
						startLoc = num;
					}
				});  
			}
			for (int i = 12; i > 6; i--)
			{
				JButton b = new JButton();
				final int num = i;
				b.setText("" + i);
				b.setSize(50,20);
				int x = BOARD_WIDTH_OFFSET + (12 - i) * 50;
				b.setLocation(x,IMAGE_HEIGHT - (BOARD_HEIGHT_OFFSET));
				gp.getContentPane().add(b);
				b.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e)
					{
						startLoc = num;
					}
				});  
			}
			for (int i = 6; i > A_STORE; i--) 
			{
				JButton b = new JButton();
				final int num = i;
				b.setText("" + i);
				b.setSize(50,20);
				int x = BOARD_WIDTH_OFFSET + IMAGE_WIDTH/2 - 40 + (6 - i) * 50;
				b.setLocation(x,IMAGE_HEIGHT - (BOARD_HEIGHT_OFFSET));
				gp.getContentPane().add(b);
				b.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e)
					{
						startLoc = num;
					}
				});  
			}
			JButton b = new JButton();
			b.setText("Island");
			b.setSize(50,20);
			b.setLocation(BOARD_WIDTH_OFFSET + IMAGE_WIDTH/2 - 40, IMAGE_HEIGHT/2);
			gp.getContentPane().add(b);
			b.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					startLoc = -1;
				}
			});  
			//
			boolean firstRound = true;
			rollDice();
			currentPlayer = "A";
			roll1Button = new JButton();
			System.out.println("" + roll1);
			roll1Button.setText("" + roll1);
			roll1Button.setSize(100,60);
			roll1Button.setLocation((WINDOW_WIDTH - (IMAGE_OFFSET + IMAGE_WIDTH))/2 + IMAGE_OFFSET + IMAGE_WIDTH - 35,WINDOW_HEIGHT/2 - 100);
			gp.getContentPane().add(roll1Button);
			roll1Button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					System.out.println("HELLO");
					gp.getContentPane().remove(roll1Button);
					roll1Button.setText("" + roll1);
					roll1Button.setSize(100,60);
					roll1Button.setLocation((WINDOW_WIDTH - (IMAGE_OFFSET + IMAGE_WIDTH))/2 + IMAGE_OFFSET + IMAGE_WIDTH - 35,WINDOW_HEIGHT/2 - 100);
					diePicked = 1;
					gp.getContentPane().add(roll1Button);
					//Execute when button is pressed
				}
			});  
			roll2Button = new JButton();
			System.out.println("" + roll2);
			roll2Button.setText("" + roll2);
			roll2Button.setSize(100,60);
			roll2Button.setLocation((WINDOW_WIDTH - (IMAGE_OFFSET + IMAGE_WIDTH))/2 + IMAGE_OFFSET + IMAGE_WIDTH - 35,WINDOW_HEIGHT/2 - 20);
			gp.getContentPane().add(roll2Button);
			roll2Button.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					gp.getContentPane().remove(roll2Button);
					roll2Button.setText("" + roll2);
					roll2Button.setSize(100,60);
					roll2Button.setLocation((WINDOW_WIDTH - (IMAGE_OFFSET + IMAGE_WIDTH))/2 + IMAGE_OFFSET + IMAGE_WIDTH - 35,WINDOW_HEIGHT/2 - 20);
					diePicked = 2;
					gp.getContentPane().add(roll2Button);
					//Execute when button is pressed
				}
			});  
			while (getPos(A_STORE).num != 15 || getPos(B_STORE).num != 15)
			{     
				if(firstRound)
					firstRound = false;
				else
					rollDice(); 
				while (roll1 != 0 || roll2 != 0) 
				{
					gp.getContentPane().remove(roll1Button);
					roll1Button.setText("" + roll1);
					roll1Button.setSize(100,60);
					roll1Button.setLocation((WINDOW_WIDTH - (IMAGE_OFFSET + IMAGE_WIDTH))/2 + IMAGE_OFFSET + IMAGE_WIDTH - 35,WINDOW_HEIGHT/2 - 100);
					gp.getContentPane().add(roll1Button);
					gp.getContentPane().remove(roll2Button);
					roll2Button.setText("" + roll2);
					roll2Button.setSize(100,60);
					roll2Button.setLocation((WINDOW_WIDTH - (IMAGE_OFFSET + IMAGE_WIDTH))/2 + IMAGE_OFFSET + IMAGE_WIDTH - 35,WINDOW_HEIGHT/2 - 20);
					gp.getContentPane().add(roll2Button);
					System.out.println(diePicked);
					while(diePicked == 0) {
						System.out.print("");
					}
					play(diePicked);
					gp.repaint();
					diePicked = 0;
					startLoc = -2;
				}
				doublesRoll1 = false;
				doublesRoll2 = false;
				doubles = false;
				if (currentPlayer == "A")
					currentPlayer = "B";
				else
					currentPlayer = "A";
			}
			done = gameOver();
		} 
	}


	// D - You need a paint() method with one Graphics parameter. This tells the computer 
	//	what to draw each time the screen is painted.  Put all drawing instructions here.
	public void paint(Graphics g) 		
	{
		g.clearRect(0,0, WINDOW_WIDTH,WINDOW_HEIGHT);
		/***************** BUTTONS *****************/
		//roll1Button.setText("ROLL 1: " + roll1);

		/***************** IMAGES *****************/
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("board.jpg"));
		} catch (IOException e) {
		}
		g.drawImage(img,IMAGE_OFFSET,IMAGE_OFFSET,IMAGE_WIDTH,IMAGE_HEIGHT,IMAGE_OFFSET,IMAGE_OFFSET,IMAGE_WIDTH,IMAGE_HEIGHT,null);

		/***************** TEXT *****************/
		// 1 - Write text in the window 
		g.setFont(new Font("Serif", Font.PLAIN, 30));	
		g.drawString("Backgammon!", 400, 50);			// Draw the String with its lower left corner
		//	at (200, 80). Since we haven't set a
		//	color, use the default color - black.
		int middle = (WINDOW_WIDTH - (IMAGE_OFFSET + IMAGE_WIDTH))/2 + IMAGE_OFFSET + IMAGE_WIDTH - 35;
		g.setFont(new Font("Serif",Font.PLAIN,15));
		g.drawString("Player B Store: " + board[B_STORE].num,middle,100);
		g.drawString("Player A Store: " + board[A_STORE].num,middle,WINDOW_HEIGHT - 100);
		g.setFont(new Font("Serif",Font.PLAIN, 23));
		g.drawString("Roll 1: " + roll1,middle,WINDOW_HEIGHT/2 - 30);
		g.drawString("Roll 2: " + roll2,middle,WINDOW_HEIGHT/2 + 30);
		g.drawString("Current Player: " + currentPlayer, middle, 50);




		for (int i = 13; i < 19; i++)
		{
			if (board[i].player == "A") { g.setColor(Color.white); }
			else { g.setColor(Color.gray); }
			for (int j = 0; j < board[i].num; j++)
			{
				int x = BOARD_WIDTH_OFFSET + (i%12 - 1) * 50;
				g.fillOval(x,BOARD_HEIGHT_OFFSET + j * 34,RADIUS,RADIUS);
			}	
		}
		for (int i = 19; i < B_STORE; i++)
		{
			if (board[i].player == "A") { g.setColor(Color.white); }
			else { g.setColor(Color.gray); }
			for (int j = 0; j < board[i].num; j++)
			{
				int x = BOARD_WIDTH_OFFSET + IMAGE_WIDTH/2 - 40 + (i%18 - 1) * 50;
				g.fillOval(x,BOARD_HEIGHT_OFFSET + j * 34,RADIUS,RADIUS);
			}	
		}
		for (int i = 12; i > 6; i--)
		{
			if (board[i].player == "A") { g.setColor(Color.white); }
			else { g.setColor(Color.gray); }
			for (int j = 0; j < board[i].num; j++)
			{
				int x = BOARD_WIDTH_OFFSET + (12 - i) * 50;
				g.fillOval(x,IMAGE_HEIGHT - (BOARD_HEIGHT_OFFSET + j * 34 + 20),RADIUS,RADIUS);
			}	
		}
		for (int i = 6; i > A_STORE; i--) 
		{
			if (board[i].player == "A") { g.setColor(Color.white); }
			else { g.setColor(Color.gray); }
			for (int j = 0; j < board[i].num; j++)
			{
				int x = BOARD_WIDTH_OFFSET + IMAGE_WIDTH/2 - 40 + (6 - i) * 50;
				g.fillOval(x,IMAGE_HEIGHT - (BOARD_HEIGHT_OFFSET + j * 34 + 20),RADIUS,RADIUS);
			}
		}

		if (island.num > 0)
		{
			if (island.player == "A") { g.setColor(Color.white); }
			else { g.setColor(Color.gray); }
			g.fillOval(BOARD_WIDTH_OFFSET + IMAGE_WIDTH/2 - 40, IMAGE_HEIGHT/2, RADIUS, RADIUS);
			g.drawString("" + island.num,BOARD_WIDTH_OFFSET + IMAGE_WIDTH/2 - 35 , IMAGE_HEIGHT/2);
		}

	}


	public static void rollDice()
	{
		Random gen = new Random();
		roll1 = gen.nextInt(5) + 1;
		roll2 = gen.nextInt(5) + 1;	
	}


	public static void play(int input)
	{
		if (roll1 == roll2 && doubles == false)
		{
			System.out.println("Doubles! You get to move 4 pieces " + roll1 + " spaces.");
			doublesRoll1 = true;
			doublesRoll2 = true;
			doubles = true;
		}
		if(possibleMoves())
		{
			System.out.println(startLoc);
			while (startLoc == -2) { System.out.print(""); }
			int start = startLoc;
			int end = start;
			if(roll1 != 0 && roll2 != 0)
			{
				if (input == 1)
				{
					if (currentPlayer == "A")
						end = start - roll1;
					else
						end = start + roll1;
				}
				else if (input == 2)
				{
					if (currentPlayer == "A")
						end = start - roll2;
					else
						end = start + roll2;
				}
			}
			else if (roll2 == 0)
			{
				input = 1;
				if (currentPlayer == "A")
				{
					end = start - roll1;
				}
				else
					end = start + roll1;
			}
			else if(roll1 == 0)
			{
				input = 2;
				if (currentPlayer == "A")
				{
					end = start - roll2;
				}
				else
					end = start + roll2;
			}
			if (currentPlayer == "A" && end <= A_STORE && playerCanScore())
				end = A_STORE;
			else if (currentPlayer == "B" && end >= B_STORE && playerCanScore())
				end = B_STORE;
			if (island.player != currentPlayer && start == -1)
				System.out.println("Invalid move. Please try again.");
			else if (currentPlayer == "A" && end <= A_STORE && !playerCanScore() && start != -1)
				System.out.println("All your tokens must be in your inner board to start scoring.");
			else if (currentPlayer == "B" && end >= B_STORE && !playerCanScore() && start != -1)
				System.out.println("All your tokens must be in your inner board to start scoring.");
			else if (isValidMove(start,end))
			{
				System.out.println(currentPlayer + ": moved from " + start + " to " + end);
				if(start == -1)
				{
					int start2;
					if(currentPlayer == "A")
						start2 = B_STORE;
					else
						start2 = A_STORE;
					int end2 = start2;
					if (input == 1)
					{
						if (currentPlayer == "A")
							end2 = start2 - roll1;
						else
							end2 = start2 + roll1;
					}
					if (input == 2)
					{
						if (currentPlayer == "A")
							end2 = start2 - roll2;
						else
							end2 = start2 + roll2;
					}
					makeMove(currentPlayer,start2,end2);
				}
				else
					makeMove(currentPlayer,start,end);
				if(input == 1 && doublesRoll1 == false)
					roll1 = 0;
				else if (input == 1 && doublesRoll1 == true)
					doublesRoll1 = false;
				else if(input == 2 && doublesRoll2 == false)
					roll2 = 0;
				else if(input == 2 && doublesRoll2 == true)
					doublesRoll2 = false;
			}
			else
			{
				System.out.println("Invalid move. Try again.");
			}
		}
		else
		{
			System.out.println("No possible moves. Skipping turn");
			roll1 = 0;
			roll2 = 0;
		}
	}

	public static boolean playerCanScore() 
	{
		if (island.player == currentPlayer && island.num != 0)
			return false;
		if (currentPlayer == "A") 
		{
			for (int i = A_STORE + 7; i < B_STORE - 1; i++)
			{
				if (board[i].player == "A")
					return false;
			}
		}
		else
		{
			for (int i = B_STORE - 7; i > A_STORE + 1; i--)
			{
				if (board[i].player == "B")
					return false;
			}
		}
		return true;
	}

	public static boolean possibleMoves()
	{
		if(island.num != 0 && island.player == currentPlayer)
		{
			if (board[roll1].player == currentPlayer || board[roll1].num == 0 || board[roll1].num == 1)
				return true;
		}
		for (int i = A_STORE + 1; i < B_STORE; i++)
		{
			if(board[i].num != 0 && board[i].player == currentPlayer)
			{
				if (currentPlayer == "A")
				{
					if(i > A_STORE + roll1)
					{
						if(isValidMove(i,i - roll1))
							return true;
					}
					if(i > A_STORE + roll2)
					{
						if(isValidMove(i,i - roll2))
							return true;
					}
				}
				else
				{
					if(i < B_STORE - roll1)
					{
						if(isValidMove(i,i + roll1))
							return true;
					}
					if(i < B_STORE - roll2)
					{
						if(isValidMove(i,i + roll2))
							return true;
					}
				}	
			}	
		}
		return false;
	}

	public static boolean isValidMove(int start,int end)
	{
		if(island.num != 0)
		{
			if(island.num != 0 && island.player == currentPlayer && start != -1)
				return false;
		}
		else
		{
			if (!(board[start].player == currentPlayer && board[start].num > 0))
				return false; 
			if (!(board[end].num == 0 || board[end].player == currentPlayer || board[end].num == 1))
				return false;
			if (!((currentPlayer == "A" && start > end) || (currentPlayer == "B" && start < end)))
				return false;
		}
		return true;
	}


	public static void initializeBoard()
	{
		board = new Position [26];
		for (int i = 0; i < 25; i++)
			board[i] = new Position(0,"");
		board[0] = new Position(0,"A");
		board[1] = new Position(2, "B");
		board[6] = new Position(5,"A");
		board[8] = new Position(3,"A");
		board[12] = new Position(5,"B");
		board[13] = new Position(5,"A");
		board[17] = new Position(3,"B"); 
		board[19] = new Position(5,"B");
		board[24] = new Position(2,"A"); 
		board[25] = new Position(0,"B"); 
		island = new Position(0,"");
	}


	public static Position getPos(int i)
	{
		return board[i];
	}

	public static String printBoard()
	{
		String boardString = "";
		for (int i = 13; i < 19; i++)
			boardString = boardString + i + "  ";
		boardString = boardString + "  ||";
		for (int i = 19; i < 25; i++)
			boardString = boardString + i + "  ";
		boardString = boardString + "\n";
		for(int i = 13; i < 19; i++)
			boardString = boardString + board[i].num + board[i].player + "   ";
		boardString = boardString + "||";
		for(int i = 19; i < 25; i++)
			boardString = boardString + board[i].num + board[i].player + "   ";
		boardString = boardString + "B store: " + board[B_STORE].num + "\n\n";
		for (int i = 12; i >= 7; i--)
			boardString = boardString + board[i].num + board[i].player + "   ";
		boardString = boardString + "||";
		for (int i = 6; i > 0; i--)
			boardString = boardString + board[i].num + board[i].player + "   ";
		boardString = boardString + "\n";
		for(int i = 12; i >= 7; i--)
			boardString = boardString + i + "   ";
		boardString = boardString + "||";
		for(int i = 6; i > 0; i--)
			boardString = boardString + i + "   ";
		boardString = boardString + " A store: " + board[A_STORE].num + "\n\n";
		if (island.num != 0)
			boardString = boardString + "\n\nIsland Player: " + island.player + "\n Number: " + island.num;
		return boardString;	
	}

	public static void makeMove(String p, int start, int end)
	{
		if(island.num > 0 && island.player == currentPlayer)
		{
			island.num--;
			if(island.num == 0)
				island.setPlayer("");
		}
		else
		{
			board[start].num--;
			if (board[start].num == 0)
				board[start].player = "";
		}
		if(board[end].player == currentPlayer || board[end].num == 0)
		{
			board[end].num++; 
			if (board[end].num == 1)
				board[end].player = p;	
		}
		else if(board[end].num == 1)
		{
			if (currentPlayer == "A")
				island.player = "B";
			else if (currentPlayer == "B")
				island.player = "A";
			island.num++;
			board[end].player = currentPlayer;

		}
	}

	public static boolean gameOver()
	{
		Scanner console = new Scanner(System.in);
		if (getPos(A_STORE).num == 15)
			System.out.println("Player A wins!");
		else if (getPos(B_STORE).num == 15)
			System.out.println("Player B wins!");
		System.out.println("Play again? yes or no");
		if (console.nextLine() == "yes")
			return true;
		else
			return false;
	}
}
