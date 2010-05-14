/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew Cassidy
 */
public class OperatorNode extends SyntaxTreeNode {

        public int Type;
        public SyntaxTreeNode Left;
        public SyntaxTreeNode Right;

        public double Evaluate()
        {
            switch (Type)
            {
                case 0:
                    return Left.Evaluate() + Right.Evaluate();
                case 1:
                    return Left.Evaluate() - Right.Evaluate();
                case 2:
                    return Left.Evaluate() * Right.Evaluate();
                case 3:
                    return Left.Evaluate() / Right.Evaluate();
                default:
                    return Left.Evaluate() + Right.Evaluate();
            }
        }

        private String OperatorString()
        {
            switch (Type)
            {
                case 0:
                    return "+";
                case 1:
                    return "-";
                case 2:
                    return "*";
                case 3:
                    return "/";
                default:
                    return "+";
            }
        }

    @Override
        public String toString()
        {
            return "(" + Left.toString() + " " + OperatorString() + " " + Right.toString() +")";
        }

        public boolean SubTreeSearch(SyntaxTreeNode node)
        {
            if (Left == node || Right == node) return true;
            else return Left.SubTreeSearch(node) || Right.SubTreeSearch(node);
        }

        public SyntaxTreeNode DeepCopy()
        {
            OperatorNode copy = new OperatorNode();
            copy.Left = this.Left.DeepCopy();
            copy.Right = this.Right.DeepCopy();
            copy.Left.Parent = copy;
            copy.Right.Parent = copy;
            copy.Type = this.Type;
            return copy;
        }

}
