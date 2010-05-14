/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew Cassidy
 */
public abstract class SyntaxTreeNode {
        public OperatorNode Parent;
        public abstract double Evaluate();
        public abstract boolean SubTreeSearch(SyntaxTreeNode node);
        public abstract SyntaxTreeNode DeepCopy();
}
