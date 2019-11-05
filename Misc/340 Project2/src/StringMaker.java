/**
 * Interface to be implemented by the TextMaker class.
 * Describes a class that can take String input and produce randomized output
 * based on that String.
 * 
 * @author: M. Allen
 */

public interface StringMaker
{
    /**
     * Generate output based on analyzed String, provided by call to setInput(),
     * and analysis level, provided by call to setLevel(). Will use default
     * analysis level == 1 if setLevel() is not called before this method. Will
     * return an empty String if setInput() is not called before this method.
     * 
     * @return Randomly generated String, based on n-level analysis of input
     *         String given by prior call to setInput(). The return String will
     *         be empty if no prior call to setInput() exists, or if that method
     *         is called with an empty input String.
     */
    public String getOutput();

    /**
     * Reads input String that will be used to generate randomized output.
     * 
     * @param input String input for analysis.
     */
    public void setInput( String input );

    /**
     * Sets appropriate level of analysis, where 0-level uses individual
     * character frequency, 1-level uses frequency or pairs of characters,
     * 2-level uses frequency of triples of characters, and so on.
     * 
     * @param level Frequency level to use when generating random output
     *            produced by getOutput() calls.
     * 
     * @throws IllegalArgumentException Thrown exception when analysis level is
     *             any negative value, level < 0.
     */
    public void setLevel( int level ) throws IllegalArgumentException;
}
