import java.util.NoSuchElementException;
//@author: Kevin Minter
public class RandomList<T> implements RandomizedList<T> {
	
	private int size;
	private T[] content;
	
	private static final int DEFAULT_CAPACITY = 10;
	
	public RandomList(){
		size = 0;
		content = (T[]) new Object[DEFAULT_CAPACITY];
	}
	
	public void clear()
    {
        size = 0;
        setCapacity( DEFAULT_CAPACITY );
    }
	
	private void setCapacity(int newCapacity)
	{
	        if ( newCapacity < size ){
	            return;
	        }
	        
	        T[] old = content;
	        content = (T[]) new Object[newCapacity];
	        for ( int i = 0; i < size; i++ ){
	            content[i] = old[i];		
	        }
	}
	 
	@Override
	public void add(T newItem) {
		if(content.length == size){
			setCapacity(content.length * 2 + 1);
		}
		content[size++] = newItem;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public T randomElement() throws NoSuchElementException {
		if(size <= 0){
			throw new NoSuchElementException();
		}
		return content[(int)(Math.random() * size)];
	}

}
