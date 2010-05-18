package Countdown;

import java.util.ArrayList;
import java.util.Random;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew Cassidy
 */
public class RandomiserSolver extends AbstractSolver {

    private static final Random rng = new Random();
    private SyntaxTreeNode rootNode;
    private ArrayList<SyntaxTreeNode> allNodes;
    private ArrayList<OperatorNode> operators;
    private ArrayList<NumberNode> nums;

    @Override
    protected void RunWorker() {
        long start = System.currentTimeMillis();
        GenerateInitialState();

        int bestscore = Integer.MAX_VALUE;
        String bestString = "";

        int i;

        for (i = 0; i < 1000000; i++)
        {
            Mutate();
            int score = Math.abs(rootNode.Evaluate() - target);
            if (!rootNode.IsValid()) continue;


            if (score < bestscore)
            {
                bestscore = score;
                bestString = rootNode.toString() + " = " + rootNode.Evaluate();
            }
            if (score == 0)
            {
                break;
            }
        }

        result = new SolverResult();
        result.Solution = bestString;
        result.SolutionsTested = i;
        result.TimeTaken = System.currentTimeMillis() - start;
    }

    private void Mutate()
    {
        // Take 2 nodes
        SyntaxTreeNode node1, node2;

        do
        {
            node1 = allNodes.get(rng.nextInt(allNodes.size()));
            node2 = allNodes.get(rng.nextInt(allNodes.size()));
        } while (
            node1 == node2
            ); // never swap 2 operator nodes, or the same node, or the root node for something else.

        // Swap the 2 nodes

        // node 1 on the left or right side of the parent?

        OperatorNode node1parent = node1.Parent;
        OperatorNode node2parent = node2.Parent;

        if (node1parent == null) rootNode = node2;  // Empty
        else if (node1parent.Left == node1)
            node1parent.Left = node2;
        else
            node1parent.Right = node2;

        node2.Parent = node1parent;

        if (node2parent == null) rootNode = node1; // Empty
        else if (node2parent.Left == node2)
            node2parent.Left = node1;
        else node2parent.Right = node1;

        node1.Parent = node2parent;

        // mutate some of the operators
        for (int i = 0; i < operators.size() / 2; i++)
        {
            operators.get(rng.nextInt(operators.size())).Type = OperatorType.randomOpp();
        }
    }

    private void GenerateInitialState()
    {
        Integer numberCount = inputNumbers.size();

        operators = new ArrayList<OperatorNode>(numberCount - 1);
        nums = new ArrayList<NumberNode>(numberCount);

        for (int num : inputNumbers)
        {
            NumberNode n = new NumberNode();
            n.setValue ( num );
            nums.add(n);
        }

        for (int i = 0; i < numberCount - 1; i++)
        {
            OperatorNode n = new OperatorNode();
            n.Type = OperatorType.randomOpp();
            operators.add(n);
        }

        rootNode = operators.get(0);

        for (int i = 0; i < numberCount - 1; i++)
        {
            if (i == numberCount - 2)
            {
                operators.get(i).Left = nums.get(i + 1);
                nums.get(i + 1).Parent = operators.get(i);
            }
            else
            {
                operators.get(i).Left = operators.get(i + 1);
                operators.get(i + 1).Parent = operators.get(i);
            }

            operators.get(i).Right = nums.get(i);
            nums.get(i).Parent = operators.get(i);
        }

        allNodes = new ArrayList<SyntaxTreeNode>(operators);
        allNodes.addAll(nums);
    }

}
