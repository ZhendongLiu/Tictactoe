import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SuperTTT {
	
	public SuState Result(SuState s, int[] a){
		SuState newState = new SuState(s, a);
		return newState;
	}
    
	public int Heuristic(SuState s){
		int v = 0;
		
		for(int i = 0; i < 9; i ++){
			int[] a = s.arrangement[i];
			int XorO;
			if(s.turn == s.XorO) XorO = -1;
			else XorO = 1;
			v = v + hHelp(a, XorO);
		}
		return v;
	}
	
	public int hHelp(int[] a, int XorO){
    	
    	int value = 0;
    	
    	for(int i = 0; i < 3; i++) {
    		value = mapping(a[i] + a[i + 3] + a[i + 6], XorO);
    	}
    	for(int i = 0; i < 7; i += 3){
    		value = value + mapping(a[i] + a[i + 1] + a[i + 2], XorO);
    	}
    	value = value + mapping(a[0] + a[4] + a[8], XorO);
    	value = value + mapping(a[2] + a[4] + a[6], XorO);
    	
    	return value;
    	
   }
    
    /**XorO: AI plays X or O*/
    public int mapping(int value, int XorO){
    	switch(value){
    		case 3:return XorO*(-20000);//xxx
    		case 2: return XorO*(-1);//oxx
    		case 1: return 1;//oox
    		case 0: return XorO*20000;//ooo
    		case -2: return XorO*(-200);//xxe
    		case -7: return XorO*(-5);//xee
    		case -12: return 0;
    		case -4: return XorO*200;//ooe
    		case -8: return XorO*5;//oee
    		case -3: return 0;
    	}
    	
    	return 0;
    	
    	
    	
    	
    	
    }
    
    /**terminal test, get the board arrangement and see if there's a winner, or if the whole board is fully 
     * occupied*/
    public boolean Ttest(SuState s){
    	int[] arrangement = s.getThisArrang();
    	int a = getWinner(arrangement);
    	if(a != -1) return true;
    	else if(s.emptygrid().size() == 0) return true;
    	else return false;
    }
    
    int Utility(SuState s){
    	int winner = getWinner(s.getThisArrang());
    	/**if computer plays x*/
    	if(s.turn == s.XorO){
    		if(winner == 1)  return 20000;
    		else return -20000;
    	}
    	
    	else if(s.turn != s.XorO){
    		if(winner == 1) return -20000;
    		else return 20000;
    	}
    	
    	return 0;
   }
    
    public int getWinner(int[] arrangement){
    	for(int i = 0; i < 3; i ++){
			if(arrangement[i] != -4){
				if(arrangement[i] == arrangement[i + 3] && arrangement[i] == arrangement[i + 6])
					return arrangement[i];
				}
		}
		for(int i = 0; i < 7; i += 3){
			if(arrangement[i] != -4){
				if(arrangement[i] == arrangement[i + 1] && arrangement[i] == arrangement[i + 2])
					return arrangement[i];
				}
		}
		if(arrangement[4] != -4){
			if((arrangement[0] == arrangement[4] && arrangement[0] == arrangement[8]) || 
				(arrangement[2] == arrangement[4] && arrangement[2] == arrangement[6])     
				)
				return arrangement[4];
		}
		return -1;
    }
    
    int absearch(SuState s, int depth){
		int v = 0;
		if(s.turn == 1){
		 	v = MaxValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
		}else if(s.turn == 0){
			v = MinValue(s, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
		}
		return v;
	}
	
	int MaxValue(SuState s, int a, int b, int depth){
		
		if(Ttest(s)) {
			s.minimax = Utility(s);
			return Utility(s);
		}
		
		/**cut-off test*/
		else if(depth == 0){
			return Heuristic(s);
		}
		
		depth = depth --;
		int v = Integer.MIN_VALUE;
		
		ArrayList<int[]> actions = s.availMove();
		ArrayList<SuState> states = new ArrayList<SuState>();
		for(int i = 0; i < actions.size(); i ++){
			states.add(Result(s, actions.get(i)));
		}
		s.children = states;
		
		for(int i = 0; i < s.children.size(); i++){
			v = Math.max(v, MinValue(s.children.get(i), a, b, depth));
			if(v >= b){
				s.minimax = v;
				return v;
			}
			a = Math.max(v, a);
		}
		
		s.complete = 1;
		s.minimax = v;
		return v;
	}
	
	int MinValue(SuState s, int a, int b, int depth){
		
		if(Ttest(s)) {
			s.minimax = Utility(s);
			return Utility(s);
		}
		else if(depth == 0){
			return Heuristic(s);
		}
		
		depth = depth --;
		int v = Integer.MAX_VALUE;
		
		ArrayList<int[]> actions = s.availMove();
		//in case opponent goes to incompletely explored state;
		ArrayList<SuState> states = new ArrayList<SuState>();
		for(int i = 0; i < actions.size(); i ++){
			states.add(Result(s, actions.get(i)));
		}
		s.children = states;
		
		for(int i = 0; i < s.children.size(); i++){
			v = Math.min(v, MaxValue(s.children.get(i), a, b, depth));
			if(v <= a){
				s.minimax = v;
				return v;
			}
			b = Math.min(v, b);
		}
		s.complete = 1;
		s.minimax = v;
		return v;
	}
	
    
    char getXorO(int i){
		if(i == 1) return 'X';
		else if(i == -4){
			return ' ';
		}
		else return 'O';
	}
	
    
    public void printBoard(SuState s) {
        //top 3
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	for (int i = 0; i < 3; i ++) {
            for (int j = 3; j < 6; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	for (int i = 0; i < 3; i ++) {
            for (int j = 6; j < 9; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	System.err.println();

    	//middle 3
        for (int i = 3; i < 6; i ++) {
            for (int j = 0; j < 3; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	for (int i = 3; i < 6; i ++) {
            for (int j = 3; j < 6; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	for (int i = 3; i < 6; i ++) {
            for (int j = 6; j < 9; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	System.err.println();

    	//bottom 3
        for (int i = 6; i < 9; i ++) {
            for (int j = 0; j < 3; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	for (int i = 6; i < 9; i ++) {
            for (int j = 3; j < 6; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();
    	for (int i = 6; i < 9; i ++) {
            for (int j = 6; j < 9; j ++) {
                System.err.print("(" + getXorO(s.arrangement[i][j]) + ")");
            }
    		System.err.print(" ");
        }
    	System.err.println();

        
        
    }
    
   
    public boolean legalMove(int[] move, SuState state){
    	ArrayList<int[]> legalMoves = state.availMove();
    	for(int i = 0; i < legalMoves.size(); i ++){
    		//System.err.printf("%d,%d", legalMoves.get(i)[0], legalMoves.get(i)[1]);
    		if(move[0] == legalMoves.get(i)[0] && move[1] == legalMoves.get(i)[1]) return true;
    	}
    	
    	return false;
    }
    
    public void shuffle(ArrayList<SuState> list){
        Random ran = new Random();
        
        for(int i = 0; i < list.size(); i ++){
            int a = ran.nextInt(list.size());
            int b = ran.nextInt(list.size());
            
            SuState temp = list.get(a);
            list.set(a, list.get(b));
            list.set(b, temp);
        }
    }
    public void Start(){
    	
    	while(true){
    	Scanner input = new Scanner(System.in);
		
		System.err.println("please enter 1 if you want to play X, 0 otherwise");
		int a = input.nextInt();
		SuState iniState;
		SuState currState;
        int currDepth = 7;
		if(a == 1){
			iniState = new SuState(0);
			absearch(iniState, currDepth);
			currState = iniState;
		}
		else{
			Random ran = new Random();
			int[] act = new int[2];
			act[0] = ran.nextInt(9);
			act[1] = ran.nextInt(9);
			iniState = new SuState(1);
			iniState = new SuState(iniState, act);
			MinValue(iniState, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
			currState = iniState;
			System.err.println("AI makes:");
			System.out.printf("%d %d\n",currState.lastAction[0] + 1, currState.lastAction[1] + 1);
			a = 1;
		}
		
		
		printBoard(currState);
		
		int[] oppoMove = new int[2];
		int depth = 0;
		while(true){
			if(a == 1){
				System.err.println("please enter two numbers from 1 - 9 seperated by a space to make the move");
				oppoMove[0] = input.nextInt() - 1;
				oppoMove[1] = input.nextInt() - 1;
				while(!legalMove(oppoMove, currState)){
					System.err.println("illegal move!");
					System.err.println("please enter number 1 - 9 to make the move");
					oppoMove[0] = input.nextInt() - 1;
					oppoMove[1] = input.nextInt() - 1;
				}
				
					if(depth == 0 || currState.complete == 0){
					absearch(currState, currDepth+=5);
					depth = 5;
					}
					depth --;
				for(int i = 0; i < currState.children.size(); i ++){
						if(currState.children.get(i).moveMatch(oppoMove))
							currState = currState.children.get(i);
					}
//					System.err.printf("human action: %d, %d\n", currState.lastAction[0]+1,currState.lastAction[1]+1);
					
					a = 0;
			}else{
				/**if substates have not been fully explored*/
				if(depth == 0 || currState.complete == 0){
					absearch(currState, currDepth+=5);
					depth = 5;
					}
					depth --;
				
				int minimax = Integer.MIN_VALUE;
				SuState state = null;
//				System.err.printf("human action: %d, %d\n", currState.lastAction[0]+1,currState.lastAction[1]+1);
				
                shuffle(currState.children);
                
				for(int i = 0; i < currState.children.size(); i++){
						if(currState.children.get(i).minimax > minimax)
							state = currState.children.get(i);
						    minimax = state.minimax;
					}
				currState = state;
//				System.err.printf("AI action: %d, %d\n", currState.lastAction[0]+1,currState.lastAction[1]+1);
				System.err.println("AI makes:");
				System.out.printf("%d %d\n",currState.lastAction[0] + 1, currState.lastAction[1] + 1);
				a = 1;
			}
			
			printBoard(currState);
			if(Ttest(currState)){
				if(getWinner(currState.getThisArrang()) == 1) System.err.println("X won");
				else if(getWinner(currState.getThisArrang()) == 0) System.err.println("O won");
				else System.err.println("Tie");
				break;
			}
		}
		
	}
   }
}
