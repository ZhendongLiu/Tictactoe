import java.util.ArrayList;

public class SuState {
	
	/**int[i][j] : i is the # of board, j is the position in that board*/
	public int[][] arrangement;
	
	/***1 if this state is a max state , 0 otherwise*/
	public int turn; 
	
	/**the utility value of this state*/
	public int minimax;
	
	/**the successors of this state*/
	public ArrayList<SuState> children;
	
	/***1 if this is X to play, 0 otherwise, is used in the second constructor*/
	public int XorO;
	
	/**1 if all of it's substates are explored. */
	public int complete; 
	public int[] lastAction;
	
	public SuState(int turn){
		arrangement = new int[9][9];
		for(int i = 0; i < 9; i ++){
			for(int j = 0; j < 9; j ++){
				/**if a position is empty, the corresponding value
				 * is -4 (for heuristic calculation*/
				arrangement[i][j] = -4;
			}
		}
		
		this.turn = turn;
		XorO = 1;
		children = new ArrayList<SuState>();
		complete = 0;
		
		/**initialize lastAction*/
		lastAction = new int[]{-1,-1};
		
	}
	
	/***construct a new state with state previous and action a*/
		public SuState(SuState previous, int[] a){
			arrangement = new int[9][9];
			/***for not changing the previous arrangement so that emptygrid() works fine*/
			for(int i = 0; i < 9; i ++){
				for(int j = 0; j < 9; j ++){
					arrangement[i][j] = previous.arrangement[i][j];
				}
			}
			/**making moves*/
			if(previous.XorO == 1){
				arrangement[a[0]][a[1]] = 1;
				XorO = 0;
			}
			else{
				arrangement[a[0]][a[1]] = 0;
				XorO = 1;
			}
			
			/***switch turn*/
			if(previous.turn == 1) turn = 0;
			else turn = 1;
			children = new ArrayList<SuState>();
			complete = 0;
			lastAction = a;
		}
		
		/**get the board arrangement of the board just be played*/
		public int[] getThisArrang(){
			int[] arrangement = new int[9];
			int num;
			if(lastAction[0] == -1) num = 0;
			else num = lastAction[0];
			
			for(int i = 0; i < 9; i ++){
				arrangement[i] = this.arrangement[num][i];
			}
			
			return arrangement;
		}
		
		/**get the board arrangement of the board to be played next
		 * hasn't been used */
		public int[] getNextArrang(){
			int[] arrangement = new int[9];
			for(int i = 0; i < 9; i ++){
				arrangement[i] = this.arrangement[lastAction[1]][i];
			}
			
			return arrangement;
		}
		
		
		/**Available actions for next move. Using 'lastAction' to get the next board to play
		 * and empty grid.
		 * if the board is empty, next move can be every place on board. 
		 * if the next board to play is full, then next move can be everywhere else*/
		
		public ArrayList<int[]> availMove(){
			ArrayList<int[]> a = new ArrayList<int[]>();
			a = getEmptyAt(lastAction[1]);
			if(a.size() == 0) return emptygrid();
			else return a;
			}
	
		
		public boolean moveMatch(int[] move){
			if(move[0] == lastAction[0] && move[1] ==  lastAction[1]) return true;
			else return false;
		}
		
		
		/**get empty grid at board No.x */
		public ArrayList<int[]> getEmptyAt(int x){
			ArrayList<int[]> a = new ArrayList<int[]>();
			if(x == -1) return a;
			for(int i = 0; i < 9; i ++){
				if(arrangement[x][i] == -4) a.add(new int[]{x, i});
			}
			return a;
		}
		
		/**get every empty grid on the entire board*/
		public ArrayList<int[]> emptygrid(){
			ArrayList<int[]> a = new ArrayList<int[]>();
			for(int i = 0; i < 9; i ++){
				for(int j = 0; j < 9; j++ ){
					
					if(arrangement[i][j] == -4){
						
						int[] p = new int[2];
						p[0] = i;
						p[1] = j;
						a.add(p);
					}
				}
			}
			return a;
		}
}
