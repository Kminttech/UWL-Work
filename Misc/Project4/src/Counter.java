
public class Counter implements WordCounter {

	private String word;
	private int count;
	
	public Counter(String word,int count){
		this.word = word;
		this.count = count;
	}
	
	@Override
	public int compareTo(Object arg0) {
		if(arg0 instanceof Counter){
			Counter obj = (Counter)arg0;
			return word.compareTo(obj.getWord());
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object compare){
		if(compare instanceof Counter){
			Counter temp = (Counter)compare;
			return word.equals(temp.getWord());
		}
		return false;
	}

	@Override
	public String getWord() {
		return word;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public void setWord(String word) {
		this.word = word;
	}

	@Override
	public void setCount(int count) {
		this.count = count;
	}
	
}
