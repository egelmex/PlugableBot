package Countdown;

import java.util.ArrayList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author A.Cassidy (andrew.cassidy@bytz.co.uk)
 */
public abstract class AbstractSolver extends Thread
{
    protected ArrayList<Integer> inputNumbers;
    protected Integer target;
    protected SolverResult result;

    protected void Solve(ArrayList<Integer> inputNumbers, Integer target)
    {
        this.inputNumbers = inputNumbers;
        this.target = target;

        start();
    }

    protected abstract void RunWorker();

    @Override
    public void run()
    {
        RunWorker();
    }

    public SolverResult GetResult()
    {
        return result;
    }
}
