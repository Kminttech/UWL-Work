//@author: Kevin Minter
public class TextMaker implements StringMaker {
	
	private String input;
	private int level;
	
	public TextMaker(String input, int level){
		this.input = input;
		this.level = level;
	}
	
	@Override
	public String getOutput() {
		String output = "";
		//I do this check since with 0 Level analysis I only need to read the file into the RandomList once, works fine without this but I thought that this was better
		if(level == 0){
			RandomList<Character> rndList = new RandomList<Character>();
			for(int i = 0; i < input.length(); i++){
				rndList.add(input.charAt(i));
			}
			for(int i = 0; i < 1500; i++){
				output = output + rndList.randomElement();
			}
		}else{
			int startString = (int) (Math.random() * (input.length() - level));
			output = input.substring(startString,startString+level);
			RandomList<Character> rndList = new RandomList<Character>();
			for(int i = 0; i < 1500 - level; i++){
				rndList.clear();
				String search = output.substring(i, i+level);
				for(int j = 0; j < input.length() - level; j++){
					if(search.equals(input.substring(j,j+level))){
						rndList.add(input.charAt(j+level));
					}
				}
				output = output + rndList.randomElement();
			}
		}
		return output;
	}

	@Override
	public void setInput(String input) {
		this.input = input;
	}

	@Override
	public void setLevel(int level) throws IllegalArgumentException {
		if(level >= 0){
			this.level = level;
		}else{
			throw new IllegalArgumentException();
		}
	}

}
