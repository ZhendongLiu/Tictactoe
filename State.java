import java.util.ArrayList;

public class State {
	
	public int[] arrangment;
	public int turn; //1 if it's max turn to move 0 otherwise
	public int minimax;//utility value
	public ArrayList<State> children; //its successor states a.k.a frontier
	public int XorO;//1 if this is X to play
	public int complete; // 1 if all of the substates are explored
	public int lastAction;
	
	//constructs a state with its turn
	public State(int turn){
		arrangment = new int[9];//int[] for storing "table"
		//initiate empty table
		for(int i = 0; i < 9; i ++){
			arrangment[i] = -1;
		}
		
		this.turn = turn;
		XorO = 1;
		children = new ArrayList<State>();
		complete = 0;
	}
	
	public State(){
		
	}
	
	//construct a new state with state previous and action a
	public State(State previous, int a){
		arrangment = new int[9];
		//for not changing the previous arrangement so that emptygrid() works fine
		for(int i = 0; i < 9; i ++){
			arrangment[i] = previous.arrangment[i];
		}
		//making moves
		if(previous.XorO == 1){
			arrangment[a] = 1;
			XorO = 0;
		}
		else{
			arrangment[a] = 0;
			XorO = 1;
		}
		
		//switch turn
		if(previous.turn == 1) turn = 0;
		else turn = 1;
		children = new ArrayList<State>();
		complete = 0;
		lastAction = a;
		
	}
	

	//returns an int arraylist consists of all the applicable actions (a.k.a empty spots on table)
	public ArrayList<Integer> emptygrid(){
		ArrayList<Integer> a = new ArrayList<Integer>();
		for(int i = 0; i < 9; i ++){
			if(arrangment[i] == -1) a.add(i);
		}
		return a;
	}
	
}
