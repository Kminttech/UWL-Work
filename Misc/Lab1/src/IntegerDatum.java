
public class IntegerDatum implements Datum<Integer> {
	
	int data;
	
	@Override
	public Integer get() {
		return data;
	}

	@Override
	public void set(Integer dat) {
		data = dat;
	}
	
	@Override
	public Datum<Integer> copyOf() {
		IntegerDatum copy = new IntegerDatum();
		copy.set(data);
		return copy;
	}
	/**
	@Override
	public String toString(){
		return "" + data;
	}
	*/
}
