import java.util.Scanner;

public class Main {

	public static void main(String[] args){
		Scanner input = new Scanner(System.in);
		System.err.println("please enter 1 if you want to play basic tic tac toe, enter 2 if you want to play advanced");
		int a = input.nextInt();
		SuperTTT suttt = new SuperTTT();
		TTT ttt = new TTT();
		if(a == 1)
		ttt.Start();
		else if(a == 2)
		suttt.Start();
	}
	
	
	
}
