package Countdown;

import static org.junit.Assert.*;

import org.junit.Test;

public class SyntaxTreePrtingTest {

	SyntaxTreeNode root1;
	SyntaxTreeNode root2;
	SyntaxTreeNode root3;
	SyntaxTreeNode root4;
	
	public SyntaxTreePrtingTest() {

		{
			OperatorNode node1 = new OperatorNode();
			node1.Type = OperatorType.Times;
			NumberNode node2 = new NumberNode();
			node2.setValue(1);
			NumberNode node3 = new NumberNode();
			node3.setValue(4);
			node1.Left = node2;
			node1.Right = node3;
			root1 = node1;
		}
		
		{
			OperatorNode node1 = new OperatorNode();
			node1.Type = OperatorType.Times;
			NumberNode node2 = new NumberNode();
			node2.setValue(4);
			NumberNode node3 = new NumberNode();
			node3.setValue(4);
			node1.Left = node2;
			node1.Right = node3;
			root2 = node1;
		}
		
		{
			OperatorNode node1 = new OperatorNode();
			node1.Type = OperatorType.Plus;
			NumberNode node2 = new NumberNode();
			node2.setValue(4);
			NumberNode node3 = new NumberNode();
			node3.setValue(4);
			OperatorNode node4 = new OperatorNode();
			node4.Type = OperatorType.Plus;
			NumberNode node5 = new NumberNode();
			node5.setValue(4);
			node1.Left = node2;
			node1.Right = node4;
			node4.Left = node3;
			node4.Right = node5;
			root3 = node1;
		}
		
		{
			OperatorNode node1 = new OperatorNode();
			node1.Type = OperatorType.Plus;
			NumberNode node2 = new NumberNode();
			node2.setValue(4);
			NumberNode node3 = new NumberNode();
			node3.setValue(4);
			OperatorNode node4 = new OperatorNode();
			node4.Type = OperatorType.Times;
			NumberNode node5 = new NumberNode();
			node5.setValue(4);
			node1.Left = node2;
			node1.Right = node4;
			node4.Left = node3;
			node4.Right = node5;
			root4 = node1;
		}
	}

	@Test
	public void testToString() {
		assertEquals("4", root1.toString());
		
		assertEquals("4 * 4", root2.toString());
		
		assertEquals("4 + 4 + 4", root3.toString());
		
		System.out.println(root4.toString());
		
	}

}
