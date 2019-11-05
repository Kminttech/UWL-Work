/**
 * Simple football player data class for shell-sorting lab.
 *
 * @author M. Allen
 * @author YOUR NAME HERE
 *
 */

public class Player implements Comparable<Player>
{
    private int num;
    private String name;
    private String surname;
    private String position;
    private int weight;
    private int sortMethod;
    
    /**
     * Basic constructor to save instance data.
     *
     * @param n Number of player.
     * @param first Player first name.
     * @param last Player last name.
     * @param pos Position of player.
     * @param wgt Weight of plaer.
     */
    public Player( int n, String first, String last, String pos, int wgt )
    {
        num = n;
        name = first;
        surname = last;
        position = pos;
        weight = wgt;
        sortMethod = 1;
    }
    
    /* Implicit String conversion. Overrides Object version. */
    public String toString()
    {
        String s = "";
        if ( num < 10 )
            s += " " + num;
        else
            s += num;
        return s + "   " + name + " " + surname + "   " + position + "   " +
        weight + " lbs.";
    }

	@Override
	public int compareTo(Player arg0) {
		if(sortMethod == 1){
			return num - arg0.num;
		}else if(sortMethod == 2){
			return surname.compareTo(arg0.surname);
		}else if(sortMethod == 3){
			return weight - arg0.weight;
		}
		return 0;
	}
	
	public void selectSort(int choice){
		if(choice >= 1 && choice <= 3){
			sortMethod = choice;
		}
	}
}
