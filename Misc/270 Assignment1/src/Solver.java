/**
 * CS 270: Solver template for Assignment 01
 * See assignment document for details.
 *
 * @author jsauppe
 * Last Modified: 2016-01-31
 */
public class Solver {

	private int[] powersOf2;

	/**
	 * Testing method for the Solver class that allows it to be run by itself
	 * without the Driver. This code may be helpful for testing and debugging.
	 * Feel free to modify the method for your own testing purposes.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Solver s = new Solver();
		int min = -8;
		int max = +7;
		for (int i = min; i <= max; ++i) {
			char[] binaryStr = s.decimalToBinary(i);
			System.out.format("%5d: %10s%n", i, Driver.arrayToString(binaryStr));
		}
	}

	/**
	 * Constructor for the Solver. It should allocate memory for and initialize
	 * the powersOf2 member array.
	 */
	public Solver() {
		powersOf2 = new int[16];
		for(int i = 0; i < 16; i++){
			powersOf2[i] = (int)Math.pow(2, i);
		}
	}

	/**
	 * Determines the number of bits needed to represent the given value in
	 * two's complement binary.
	 *
	 * @param value The value to represent.
	 * @return The number of bits needed.
	 */
	public int howManyBits(int value) {
		for(int i = 1; i < 16; i++){
			if(powersOf2[i] > value){
				return i;
			}
		}
		return 16;
	}

	/**
	 * Converts the given value into a two's complement binary number
	 * represented as an array of char. The array should use the minimal
	 * number of bits that is necessary.
	 *
	 * @param value The value to convert.
	 * @return A char array of 0's and 1's representing the number in two's
	 *         complement binary. The most significant bit should be stored in
	 *         position 0 in the array.
	 */
	public char[] decimalToBinary(int value) {
		if(value >= 0){
			int i = 0;
			while(powersOf2[i] <= value){
				i++;
			}
			char[] binaryRep = new char[i+1];
			binaryRep[0] = '0';
			int valueRemain = value;
			for(int j = 1; j < binaryRep.length; j++){
				if(powersOf2[binaryRep.length - j - 1] <= valueRemain){
					binaryRep[j] = '1';
					valueRemain -= powersOf2[binaryRep.length - j - 1];
				}else{
					binaryRep[j] = '0';
				}
			}
			return binaryRep;
		}else{
			int i = 0;
			while(powersOf2[i] < Math.abs(value)){
				i++;
			}
			char[] binaryRep = new char[i+1];
			binaryRep[0] = '1';
			int curRepValue = 0 - powersOf2[i];
			for(int j = 1; j < binaryRep.length; j++){
				if(curRepValue + powersOf2[binaryRep.length - j - 1] <= value){
					binaryRep[j] = '1';
					curRepValue += powersOf2[binaryRep.length - j - 1];
				}else{
					binaryRep[j] = '0';
				}
			}
			return binaryRep;
		}
	}

	/**
	 * Computes the two's complement (negation) of the given two's complement
	 * binary number and returns the result.
	 *
	 * @param binaryStr The binary number to negate.
	 * @return The negated number in two's complement binary.
	 */
	public char[] twosComplementNegate(char[] binaryStr) {
		char[] negated = new char[binaryStr.length];
		for(int i = 0; i < binaryStr.length; i++){
			if(binaryStr[i] == '0'){
				negated[i] = '1';
			}else{
				negated[i] = '0';
			}
		}
		int j = binaryStr.length - 1;
		while(negated[j] == '1'){
			negated[j--] = '0';
			if(j<0){
				break;
			}
		}
		if(j >= 0){
			negated[j] = '1';	
		}
		if(negated[0] == binaryStr[0]){
			char[] extendedNegation = new char[negated.length + 1];
			extendedNegation[0] = '0';
			for(int i = 0; i < negated.length;i++){
				extendedNegation[i+1] = negated[i];
			}
			return extendedNegation;
		}
		return negated;
	}

	/**
	 * Applies sign extension to the given two's complement binary number so
	 * that it is stored using the given number of bits. If the number of bits
	 * is smaller than the length of the input array, the input array itself
	 * should be returned.
	 *
	 * @param binaryStr The binary number to sign-extend.
	 * @param numBits The number of bits to use.
	 * @return The sign-extended binary number.
	 */
	public char[] signExtend(char[] binaryStr, int numBits) {
		if(binaryStr.length >= numBits){
			return binaryStr;
		}
		char[] extended = new char[numBits];
		for(int i = 0; i < numBits; i++){
			if(i < binaryStr.length){
				extended[extended.length - 1 - i] = binaryStr[binaryStr.length - 1 - i];
			}else{
				extended[extended.length - 1 - i] = binaryStr[0];
			}
		}
		return extended;
	}

	/**
	 * Evaluates the expression given by the two's complement binary numbers
	 * and the specified operator.
	 *
	 * @param binaryStr1 The first number.
	 * @param op The operator to apply (either "+" or "-").
	 * @param binaryStr2 The second number.
	 * @return The result from evaluating the expression, in two's complement
	 *         binary. Note that a '*' should be appended to the returned
	 *         result if overflow occurred.
	 */
	public char[] evaluateExpression(char[] binaryStr1, String op, char[] binaryStr2) {
		if(op.equals("-")){
			binaryStr2 = twosComplementNegate(binaryStr2);
		}
		if(binaryStr1.length > binaryStr2.length){
			binaryStr2 = signExtend(binaryStr2,binaryStr1.length);
		}else if(binaryStr2.length > binaryStr1.length){
			binaryStr1 = signExtend(binaryStr1,binaryStr2.length);
		}
		char[] answer = new char[binaryStr1.length];
		boolean overflow = false;
		for(int i = answer.length - 1; i >= 0; i--){
			int count = 0;
			if(binaryStr1[i] == '1'){count++;}
			if(binaryStr2[i] == '1'){count++;}
			if(answer[i] == '1'){count++;}
			if(count == 3){
				try{
					answer[i-1] = '1';
				}catch(IndexOutOfBoundsException e){}
			}else if(count == 2){
				try{
					answer[i] = '0';
					answer[i-1] = '1';
				}catch(IndexOutOfBoundsException e){}
			}else if(count == 1){
				answer[i] = '1';
			}else{
				answer[i] = '0';
			}
		}
		if(binaryStr1[0] == binaryStr2[0] && answer[0] != binaryStr1[0]){
			overflow = true;
		}
		if(overflow){
			char[] overflowAnswer = new char[binaryStr1.length + 1];
			overflowAnswer[0] = '*';
			for(int i = 0; i < answer.length;i++){
				overflowAnswer[i+1] = answer[i];
			}
			return overflowAnswer;
		}
		return answer;
	}
}
