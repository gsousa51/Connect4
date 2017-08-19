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
}
