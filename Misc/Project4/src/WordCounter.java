// Interface to be implemented by the Counter class.
// An interface for an object that holds a String and its occurrence count.
// Such objects will be Comparable to other ones like them.
public interface WordCounter extends Comparable
{
    public String getWord();
    public int getCount();
    public void setWord( String word );
    public void setCount( int count );
}
