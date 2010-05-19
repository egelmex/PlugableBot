package Countdown;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author Andrew Cassidy
 */
public class OperatorNode extends SyntaxTreeNode {

	public OperatorType Type;
	public SyntaxTreeNode Left;
	public SyntaxTreeNode Right;
	private boolean isValid = false;

	@Override
	public boolean IsValid() {
		return isValid;
	}

	public int Evaluate() {
		int left = Left.Evaluate();
		int right = Right.Evaluate();

		isValid = Left.IsValid() && Right.IsValid();
		if (!isValid)
			return 0;

		if (Type == OperatorType.Divide) {
			isValid &= right != 0;
			if (!isValid)
				return 0;
			isValid &= left % right == 0;
			if (!isValid)
				return 0;
		}

		switch (Type) {
		case Plus:
			return Left.Evaluate() + Right.Evaluate();
		case Minus:
			return Left.Evaluate() - Right.Evaluate();
		case Times:
			return Left.Evaluate() * Right.Evaluate();
		case Divide:
			return Left.Evaluate() / Right.Evaluate();
		default:
			return Left.Evaluate() + Right.Evaluate();
		}
	}

	private String OperatorString() {
		return Type.toString();
	}

	@Override
	public String toString() {
		return toString(false);
	}

//	public boolean needsBrackets(OperatorNode parentNode, OperatorNode subNode) {
//		return (parentNode.Type.getValue() <= subNode.Type.getValue())
//				&& (subNode.Type == parentNode.Type)
//				&& (parentNode.Type == OperatorType.Plus || parentNode.Type == OperatorType.Times);
//	}

	public String toString(boolean bracket) {
		SyntaxTreeNode node = simplify();
		if (node instanceof OperatorNode) {
			OperatorNode opNode = (OperatorNode) node;

                        // parent higher?
                        boolean needsBrackets = bracket;
                        if (opNode.Parent != null)
                        {
                            // are we on rhs of a non-cumutative operator?
                            boolean needsInversion = (!opNode.Parent.Type.isComutative()
                                    && opNode.Parent.Right == opNode &&
                                    opNode.Parent.Type.getPrecedence() == opNode.Type.getPrecedence());

                            needsBrackets |= opNode.Parent.Type.getPrecedence() < opNode.Type.getPrecedence();
                            
                            //needsBrackets |= needsInversion;
                            if (needsInversion)
                            {
                                // we can invert to change the brackets;
                                needsBrackets = bracket;
                                opNode.Type = opNode.Type.invert();
                            }
                        }

                        StringBuffer returnString = new StringBuffer();
                        returnString.append(opNode.Left.toString());
                        returnString.append(opNode.Type.toString());
                        returnString.append(opNode.Right.toString());

                        if (needsBrackets)
                        {
                            returnString.append(")");
                            returnString.insert(0, "(");
                        }

                        return returnString.toString();

		} else
			return node.toString();
	}

	public boolean SubTreeSearch(SyntaxTreeNode node) {
		if (Left == node || Right == node)
			return true;
		else
			return Left.SubTreeSearch(node) || Right.SubTreeSearch(node);
	}

	public SyntaxTreeNode DeepCopy() {
		OperatorNode copy = new OperatorNode();
		copy.Left = this.Left.DeepCopy();
		copy.Right = this.Right.DeepCopy();
		copy.Left.Parent = copy;
		copy.Right.Parent = copy;
		copy.Type = this.Type;
		return copy;
	}

	public SyntaxTreeNode simplify() {

		if (Type == OperatorType.Times && Right instanceof NumberNode
				&& ((NumberNode) Right).Value() == 1) {
			return Left;
		} else if (Type == OperatorType.Times && Left instanceof NumberNode
				&& ((NumberNode) Left).Value() == 1) {
			return Right;
		} else if ((Type == OperatorType.Divide)
				&& (Right instanceof NumberNode)
				&& (((NumberNode) Right).Value() == 1)) {
			return Left;
		}

		return this;
	}

        public String Test()
        {
            OperatorNode mult = new OperatorNode();
            mult.Type = OperatorType.Times;

            OperatorNode div = new OperatorNode();
            div.Type = OperatorType.Divide;

            div.Right = mult;
            mult.Parent = div;

            NumberNode node1 = new NumberNode();
            NumberNode node2 = new NumberNode();
            NumberNode node3 = new NumberNode();
            node1.setValue(50);
            node2.setValue(5);
            node3.setValue(2);

            node1.Parent = div;
            div.Left = node1;

            node2.Parent = mult;
            mult.Left = node2;

            node3.Parent = mult;
            mult.Right = node3;

            return div.toString();
        }

}
