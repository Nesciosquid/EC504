package homework_1;

public class FunctionGenerator {
	
	
	public static void printTwos(long num){
		System.out.print("Modulos for i = " + num + ": ");
		for (int j = 0; j < 20; j++){
			//System.out.print(num % (Math.pow(2, j)));
			System.out.print("%" + (int)(Math.pow(2,j)) + ": " + (int)(num%(Math.pow(2,j))) + ", ");
		}
		System.out.println("");
	}
	
	public static void main(String[] args) {
		Fiver captainJackSparrow = new Fiver();
		System.out.println("Running!");
		long depth = 10000000000000L;
		long known = 0;
		long guess = 0;
		for (long ii = 0; ii < depth; ii ++){
			if (ii % 100000000 == 0){
				System.out.println(ii);
			}
			
			if (ii == 0){
				known= 0;
				guess = captainJackSparrow.calcV(ii);
			}
			else if (ii == 1){
				known = 0;
				guess = captainJackSparrow.calcV(ii);
			}
			else {
			known= ((ii^(ii+1))%(ii-1));
			//guess = captainJackSparrow.calcV(ii);
			}
			
			if (false){
				System.out.println("["+ii+"]: " + known + ". Guessed: " + guess);
				//printTwos(i);
			} 
			
		}
			
		
		for (long j = 9; j < 100L; j++){
				//System.out.println(j+ ": " + calcV(j));
		}
		System.out.println("Done!");
	}

}
