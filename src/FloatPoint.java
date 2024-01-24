import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class FloatPoint {

	public FloatPoint() {

	}

	public void printSpecialNumberBitPatterns() {
		System.out.println("Bit Pattern For Zero");
		System.out.println("0-00000000-00000000000000000000000" + " -> Positive Zero");
		System.out.println("1-00000000-00000000000000000000000" + " -> Negative Zero");

		System.out.println("\nBit Pattern For +Infinite");
		System.out.println("0-111111111-00000000000000000000000");

		System.out.println("\nBit Pattern For -Infinite");
		System.out.println("1-111111111-00000000000000000000000");

		System.out.println("\nBit Pattern For Minimum Normalized Number");
		System.out.println("*-00000001-00000000000000000000000");

		System.out.println("\nBit Pattern For Maximum Normalized Number");
		System.out.println("*-11111110-11111111111111111111111");

		System.out.println("\nNote: * Sign bit can be either 0 or 1.");
	}

	public void printGivenNumberFPBitPattern(double number) {
		String fpRepresentation = convertDecimalToBinary(number);

		String signPart = findSignPart(number);
		String exponantialPart = findExponentialPart(fpRepresentation);
		String fractionPart = findFractionPart(fpRepresentation);
		
		System.out.println(signPart + "-" + exponantialPart + "-" + fractionPart);
	}
	
	public void printBinaryBitStringToDoubleEquivalent(String bitString) {
		System.out.println(convertBinaryBitStringToDouble(bitString));
	}

	// Helpers
	public double convertBinaryBitStringToDouble(String bitString) {
		double doubleEquivalent = 0;
		int maxExponent = bitString.indexOf(".") - 1;

		for (int i = 0; i < bitString.length(); i++) {
			if(bitString.charAt(i) != '.') {					
				doubleEquivalent += (Character.getNumericValue(bitString.charAt(i)) * Math.pow(2, maxExponent));
				maxExponent -= 1;
			}
		}
		
		return doubleEquivalent;
	}
	
	public String convertDecimalToBinary(double number) {
		int wholePart = getWholePartOfDouble(number);
		double decimalPart = getDecimalPartOfDouble(number);

		String fpRepresentation = "";
		fpRepresentation = fpRepresentation + integerToBinary(wholePart);
		fpRepresentation = fpRepresentation + ".";
		fpRepresentation = fpRepresentation + fractionToBinary(decimalPart);

		return fpRepresentation;
	}

	public String findSignPart(double number) {
		if(number>=0) {
			return "0";
		}
		else {
			return "1";
		}
	}
	
	public String findExponentialPart(String fpRepresentation) {
		int exponantialValue = fpRepresentation.indexOf(".") - 1; //after normalized representation, we get power of two.
		exponantialValue = exponantialValue + 127; //add bias
		
		return integerToBinary(exponantialValue);
	}
	
	public String findFractionPart(String fpRepresentation) {
		String fractionPart = "";
		
		for(int i = 1; i < fpRepresentation.length(); i++) { //dont take whole part, so start from index 1.
			if(fpRepresentation.charAt(i) != '.') { //skip "."
				fractionPart = fractionPart + fpRepresentation.charAt(i);
			}
		}
		
		//fraction part has 23 bit.
		if(fractionPart.length() > 23) {
			return fractionPart.substring(0, 22);
		}
		else {
			int neededZeroCount = 23 - fractionPart.length(); //complete in 23 bits.
			
			for (int i = 0; i < neededZeroCount; i++) {
				fractionPart = fractionPart + "0";
			}
			
			return fractionPart;
		}
		 
	}

	public String integerToBinary(int number) {
		Stack remainders = new Stack();
		String binaryRepresentation = "";

		while (number > 0) {
			remainders.push(number % 2);

			number = number / 2;
		}

		while (!remainders.isEmpty()) {
			binaryRepresentation = binaryRepresentation + remainders.pop();
		}

		return binaryRepresentation;
	}

	public String fractionToBinary(double fraction) {
		ArrayList<Double> usedNumbers = new ArrayList<Double>();
		usedNumbers.add(fraction);

		boolean isFound = false;
		String binaryRepresentation = "";

		while (!isFound) {
			fraction = fraction * 2;

			if (fraction >= 1) {
				fraction = fraction % 1; // Get rid of whole part, and continue to proccess.
				fraction = Math.round(fraction * 10000000000000.0) / 10000000000000.0; // Hassasiyetten ötürü gerçek deðeri elde edemiyoruz.

				binaryRepresentation = binaryRepresentation + "1"; //if 0.80*2 = 1.60 , then we get 1
			} else {
				binaryRepresentation = binaryRepresentation + "0"; //if 0.40*2 = 0.80 , then we get 0
			}

			isFound = usedNumbers.contains(fraction); //if numbers started to repeat, we will stop our loop.
			usedNumbers.add(fraction); 				  //else we can continue to proccess.
		}

		return binaryRepresentation;
	}

	public int getWholePartOfDouble(double number) {
		return (int) number;
	}

	public double getDecimalPartOfDouble(double number) {
		int wholePart = getWholePartOfDouble(number);
		double decimal = number % wholePart;

		decimal = Math.round(decimal * 10000000000000.0) / 10000000000000.0; // Hassasiyetten ötürü gerçek deðeri elde edemiyoruz.
		return decimal;
	}
}
