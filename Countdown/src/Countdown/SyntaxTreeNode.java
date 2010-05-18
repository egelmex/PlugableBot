package Countdown;
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
        public abstract int Evaluate();
        public abstract boolean SubTreeSearch(SyntaxTreeNode node);
        public abstract SyntaxTreeNode DeepCopy();
        public boolean IsValid() { return true; }
        
        public SyntaxTreeNode simplify(SyntaxTreeNode complexNode) {
        	return this;
        	
        }
}
