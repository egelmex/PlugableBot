package Countdown;

import static org.junit.Assert.*;

import org.junit.Test;

public class SyntaxTreePrtingTest {

	SyntaxTreeNode root1;
	SyntaxTreeNode root2;

	public SyntaxTreePrtingTest() {

		{
			OperatorNode node1 = new OperatorNode();
			node1.Type = OperatorType.Times;
			NumberNode node2 = new NumberNode();
			node2.Value = 1;
			NumberNode node3 = new NumberNode();
			node3.Value = 4;
			node1.Left = node2;
			node1.Right = node3;
			root1 = node1;
		}
		
		{
			OperatorNode node1 = new OperatorNode();
			node1.Type = OperatorType.Times;
			NumberNode node2 = new NumberNode();
			node2.Value = 4;
			NumberNode node3 = new NumberNode();
			node3.Value = 4;
			node1.Left = node2;
			node1.Right = node3;
			root2 = node1;
		}
	}

	@Test
	public void testToString() {
		assertEquals(root1.toString(), "4");
		
		assertEquals(root2.toString(), "(4  *  4)");
	}

}
