
public class DoubleDatum implements Datum<Double> {
	
	double data;
	
	@Override
	public Double get() {
		return data;
	}

	@Override
	public void set(Double dat) {
		data = dat;
	}

	@Override
	public Datum<Double> copyOf() {
		DoubleDatum copy = new DoubleDatum();
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
