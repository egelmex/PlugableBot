package Countdown;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class IterativeSolver extends AbstractSolver {

    @Override
    protected void RunWorker() {
        LinkedList<SyntaxTreeNode> stateQueue = new LinkedList<SyntaxTreeNode>();

        long start = System.currentTimeMillis();

        for (int num : inputNumbers)
        {
            NumberNode n = new NumberNode();
            n.Parent = null;
            n.Value = num;
            stateQueue.addLast(n);
        }

        int testCount = 1;

        while (stateQueue.size() > 0)
        {
            SyntaxTreeNode currentResult = stateQueue.removeFirst();

            try
            {
                if (currentResult.Evaluate() == target)
                {
                    result = new SolverResult();
                    result.Solution = currentResult.toString();
                    result.SolutionsTested = testCount;
                    result.TimeTaken = System.currentTimeMillis() - start;
                    return;
                }
            }
            catch (Exception ex) { }

            // Generate child states
            ArrayList<NumberNode> leaves = GetLeaves(currentResult);

            ArrayList<Integer> possibleNumbers = new ArrayList<Integer>(inputNumbers);

            for (NumberNode leaf : leaves)
            {
                int index = possibleNumbers.indexOf((Integer)(int)leaf.Value);
                possibleNumbers.remove(index);
            }

            for (int i = 0; i < leaves.size(); i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    for (int k = 0; k < 2; k++)
                    {
                        for (int num : possibleNumbers)
                        {
                            SyntaxTreeNode tempSolution = currentResult.DeepCopy();
                            ArrayList<NumberNode> tempLeaves = GetLeaves(tempSolution);

                            NumberNode leafToReplace = tempLeaves.get(i);

                            OperatorNode replacement = new OperatorNode();
                            replacement.Type = OperatorType.intToType(j);

                            if (leafToReplace.Parent == null)
                            {
                                tempSolution = replacement;
                            }
                            else
                            {
                                // parent left or right?
                                if (leafToReplace.Parent.Left == leafToReplace)
                                {
                                    leafToReplace.Parent.Left = replacement;
                                }
                                else
                                {
                                    leafToReplace.Parent.Right = replacement;
                                }
                            }

                            leafToReplace.Parent = replacement;
                            if (k == 0)
                            {
                                replacement.Left = leafToReplace;
                                replacement.Right = new NumberNode();
                                replacement.Right.Parent = null;
                                ((NumberNode)replacement.Right).Value = num;
                            }
                            else
                            {
                                replacement.Right = leafToReplace;
                                replacement.Left = new NumberNode();
                                replacement.Left.Parent = null;
                                ((NumberNode)replacement.Left).Value = num;
                            }

                            stateQueue.addLast(tempSolution);
                        }
                    }
                }

            }
            testCount++;
        }
    }

    private ArrayList<NumberNode> GetLeaves(SyntaxTreeNode rootNode)
    {
        ArrayList<NumberNode> ret = new ArrayList<NumberNode>();
        if (rootNode instanceof NumberNode)
        {
            ret.add((NumberNode)rootNode);
        }
        else
        {
            OperatorNode n = (OperatorNode)rootNode;

            if (n.Left instanceof NumberNode)
                ret.add((NumberNode)n.Left);
            else
                ret.addAll(GetLeaves(n.Left));

            if (n.Right instanceof NumberNode)
                ret.add((NumberNode)n.Right);
            else
                ret.addAll(GetLeaves(n.Right));
        }

        return ret;
    }



}
