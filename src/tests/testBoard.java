package tests;
import java.awt.Point;

import org.junit.Test;

import model.Board;
import model.Computer;
import model.ConnectFour;
import model.Space;

public class testBoard {
	@Test
	public void testKillShot(){
		ConnectFour g = new ConnectFour();
		Board b = g.getBoard();
		Computer c = g.getComp();
		b.setSpace(5, 2, Space.RED);
		b.setSpace(5, 3, Space.RED);
		Point p =c.getMove();

	}
	@Test
	public void testBlockingMove(){
		
		ConnectFour g = new ConnectFour();
		Board b = g.getBoard();

		b.setSpace(5, 2, Space.BLACK);
		b.setSpace(5, 3, Space.BLACK);
		b.setSpace(5, 4, Space.RED);
		b.setSpace(4, 2, Space.BLACK);
		b.setSpace(4, 3, Space.BLACK);
		b.setSpace(4, 4, Space.BLACK);
		int[] list = b.getListOfValidMoves();
		for(int c =0 ; c< list.length; c++){
			System.out.println(list[c] + " , " + c);
		}
		Point p = b.lookAhead();
		System.out.println(p);
	}
}
