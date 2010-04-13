/**
 * RNG ftw :D
 * 
 * @author Murmew
 * @version 1.0
 */
public class MersenneTwister
{
    private int[] state;
    private int index;

    /**
     * Constructor for objects of class Mersennetwiser
     */
    public MersenneTwister()
    {
        state = new int[624];
        
        init((int)(new java.util.Date().getTime()));
    }
    
    /**
     * Initialize the generator from a seed
     * @param seed Seed for RNG.
     */
    synchronized private void init(int seed)
    {
        state[0] = seed;
        for (int i = 1; i <= 623; i++) {
            state[i] = (1812433253 * (state[i-1] ^ (state[i-1] >>> 30)) + i) & 0xffffffff; // 0x6c078965
        }
    }
    
    /**
     * Extract a tempered pseudorandom number based on the index-th value,
     * calling generateNumbers() every 624 numbers
     * @return Random number
     */
    synchronized public int extractNumber()
    {
        if (index == 0) {
            generateNumbers();
        }
        
        int ret = state[index];
        ret ^= (ret >>> 11);
        ret ^= (ret << 7) & 0x9d2c5680;
        ret ^= (ret << 15) & 0xefc60000;
        ret ^= (ret >>> 18);
        
        index = (index + 1) % 624;
        
        return ret;
    }
    
    /**
     * Generate an array of 624 untempered numbers
     */
    synchronized private void generateNumbers()
    {
        for (int i = 0; i <= 623; i++) {
            int y = (state[i] & 0x80000000) | (state[(i+1) % 624] & 0x7FFFFFFF);
            state[i] = state[(i + 397) % 624] ^ (y >>> 1);
            if (y % 2 == 1) {
                state[i] = state[i] ^ 0x9908b0df;
            }
        }
    }
}
