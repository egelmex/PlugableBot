package Countdown;

import java.security.InvalidParameterException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author A.Cassidy (andrew.cassidy@bytz.co.uk)
 */
public class NumberNode extends SyntaxTreeNode
    {
        private int value;

        public int Evaluate()
        {
            return value;
        }

    @Override
        public String toString()
        {
            return Integer.toString(value);
        }

        public boolean SubTreeSearch(SyntaxTreeNode node)
        {
            return false;
        }

        public SyntaxTreeNode DeepCopy()
        {
           NumberNode copy = new NumberNode();
           copy.value = this.value;
           return copy;
        }
        
        public void setValue(int value) {
        	switch (value) {
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8: 
			case 9:
			case 10:
			case 25:
			case 50: 
			case 75: 
			case 100:
				this.value = value;
				break;
			default:
				throw new InvalidParameterException();
			}
        }
        
        public int Value() {
        	return value;
        }
    }