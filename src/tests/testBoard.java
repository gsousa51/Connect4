package tests;
import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

import model.Board;
import model.Computer;
import model.Space;

public class testBoard {

	@Test
	public void testTopRowWin() {
		Board b = new Board();
		b.setSpace(0, 0, Space.RED);
		b.setSpace(0, 1, Space.RED);
		b.setSpace(0, 2, Space.RED);
		b.setSpace(0, 3, Space.RED);
		assertTrue(b.checkForWin(Space.RED));
	}
	
	@Test
	public void testLeftWin(){
		Board b = new Board();
		b.setSpace(0, 0, Space.RED);
		b.setSpace(1, 0, Space.RED);
		b.setSpace(2, 0, Space.RED);
		b.setSpace(3, 0, Space.RED);
		assertTrue(b.checkForWin(Space.RED));
	}
	@Test
	public void testDiagWin(){
		Board b = new Board();
		b.setSpace(0, 0, Space.RED);
		b.setSpace(1, 1, Space.RED);
		b.setSpace(2, 2, Space.RED);
		b.setSpace(3, 3, Space.RED);
		assertTrue(b.checkForWin(Space.RED));
	}
	@Test
	public void testBackDiagWin(){
		Board b = new Board();
		b.setSpace(0, 6, Space.RED);
		b.setSpace(1, 5, Space.RED);
		b.setSpace(2, 4, Space.RED);
		b.setSpace(3, 3, Space.RED);
		assertTrue(b.checkForWin(Space.RED));
	}
	
	@Test 
	public void testCompVertWin(){
		Board b = new Board();
		Computer c = new Computer(b);
		b.setSpace(3, 0, Space.RED);
		b.setSpace(4, 0, Space.RED);
		b.setSpace(5, 0, Space.RED);
		Point move = c.getMove();
		assertEquals(2 , move.y);
		assertEquals(0 , move.x);
	}
	@Test
	public void testCompHorWin(){
		Board b = new Board();
		Computer c = new Computer(b);
		b.setSpace(5, 0, Space.RED);
		b.setSpace(5, 1, Space.RED);
		b.setSpace(5, 2, Space.RED);
		Point move = c.getMove();
		assertEquals(5 , move.y);
		assertEquals(3 , move.x);
	}
	@Test 
	public void testDiagWin1(){
		Board b = new Board();
		Computer c = new Computer(b);
		b.setSpace(4, 5, Space.RED);
		b.setSpace(3, 4, Space.RED);
		b.setSpace(2, 3, Space.RED);
		Point move = c.getMove();
		assertEquals(5 , move.y);
		assertEquals(6 , move.x);
	}
	@Test
	public void testDiagWin2(){
		Board b = new Board();
		Computer c = new Computer(b);
		b.setSpace(4, 1, Space.RED);
		b.setSpace(3, 2, Space.RED);
		b.setSpace(2, 3, Space.RED);
		Point move = c.getMove();
		System.out.println("Diag2:" + move.y + "," + move.x);
		assertEquals(5 , move.y);
		assertEquals(0 , move.x);
	}
	@Test
	public void testDiagWin3(){
		Board b = new Board();
		Computer c = new Computer(b);
		b.setSpace(4, 2, Space.RED);
		b.setSpace(3, 3, Space.RED);
		b.setSpace(2, 4, Space.RED);
		Point move = c.getMove();
		System.out.println("Diag2:" + move.y + "," + move.x);
		assertEquals(5 , move.y);
		assertEquals(1 , move.x);
	}
	@Test
	public void testDiagWin4(){
		Board b = new Board();
		Computer c = new Computer(b);
		b.setSpace(4, 3, Space.RED);
		b.setSpace(3, 4, Space.RED);
		b.setSpace(2, 5, Space.RED);
		Point move = c.getMove();
		System.out.println("Diag2:" + move.y + "," + move.x);
		assertEquals(5 , move.y);
		assertEquals(2 , move.x);
	}
}
