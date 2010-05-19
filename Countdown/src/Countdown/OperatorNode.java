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

	@Override
	public String toString() {
		return toString(false);
	}

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

                            if (needsInversion)
                                opNode.Type = opNode.Type.invert();
                            else
                                needsBrackets |= opNode.Parent.Type.getPrecedence() < opNode.Type.getPrecedence();
                        }

                        StringBuffer returnString = new StringBuffer(opNode.Left.toString());
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
}
