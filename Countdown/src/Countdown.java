
import AndrewCassidy.PluggableBot.DefaultPlugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andrew Cassidy
 */
public class Countdown extends DefaultPlugin {

    private boolean GameRunning = false;
    private String lastResult = null;
    private static final Random rng = new Random();
    private Integer[] numbers = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 25, 50, 75, 100};
    private ArrayList<Integer> numbersToUse;
    private Integer target;
    private final Timer tim = new Timer();
    private String channel;
    private Thread runningThread;
    private String lastPuzzle = null;

    private final TimerTask task = new TimerTask() {

        @Override
        public void run() {
            GameRunning = false;
            bot.Message(channel, "Time's Up!");
        }
    };

    private final Runnable calculate = new Runnable()
    {
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

        public void run() {
            LinkedList<SyntaxTreeNode> stateQueue = new LinkedList<SyntaxTreeNode>();

            for (int num : numbersToUse)
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
                        // ToDo
                        lastResult = lastPuzzle + " : " + currentResult.toString();
                        return;
                    }
                }
                catch (Exception ex) { }

                // Generate child states
                ArrayList<NumberNode> leaves = GetLeaves(currentResult);

                ArrayList<Integer> possibleNumbers = new ArrayList<Integer>(numbersToUse);

                for (NumberNode leaf : leaves)
                    possibleNumbers.remove((int)leaf.Value);

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
                                replacement.Type = j;

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
    };

    @Override
    public String getHelp() {
        return "Plays the countdown numbers game. !countdown starts the game, in which you must try and calculate the target result using the 6 given numbers in 30 seconds. Once the time is up, use !solution to get a solution.";
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.startsWith("!countdown"))
        {
            // game running? decline request
            if (GameRunning)
            {
                super.bot.Message(channel, "A game is currently in progress.");
            }
            else
            {
                GameRunning = true;
                this.channel = channel;

                // start a new game
                ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(numbers));
                numbersToUse = new ArrayList<Integer>(6);

                StringBuffer buffer = new StringBuffer();

                for (int i = 0; i < 6; i++)
                {
                    numbersToUse.add(list.remove(rng.nextInt(list.size())));
                    buffer.append(numbersToUse.get(i));

                    if (i < 5)
                        buffer.append(", ");
                }

                target = rng.nextInt(900) + 100;

                buffer.append(" Target: ");
                buffer.append(target);

                lastPuzzle = buffer.toString();

                bot.Message(channel, lastPuzzle);

                tim.schedule(task, 30000);

                runningThread = new Thread(calculate);
                runningThread.start();
            }
        }
        else if (message.startsWith("!solution"))
        {
            if (GameRunning)
            {
                super.bot.Message(channel, "A game is currently in progress.");
            }
            else if (lastResult == null)
            {
                super.bot.Message(channel, "No games played.");
            }
            else
            {
                super.bot.Message(channel, lastResult);
            }
        }
    }
}
