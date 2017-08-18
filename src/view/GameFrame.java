package view;

import javax.swing.JFrame;

import model.ConnectFour;

public class GameFrame extends JFrame {

	private ConnectFour game;
	private GUI gui;
	private static int width = 400;
	private static int height = 400;
	
	public static void main (String argv[]){
		GameFrame g = new GameFrame();
		g.setVisible(true);
	}
	public GameFrame(){
		   this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    this.setSize(width, height);
		    this.setLocation(400, 300);
		    this.setTitle("Connect Four");
		    game = new ConnectFour();
		    gui = new GUI();
		    game.addObserver(gui);
		    gui.setGame(game);
		    this.add(gui);
	}
}
