import java.util.Scanner;

public class CommandLineDomineering {
	private static class commandLineDom implements MoveChannel<DomineeringMove> {

		@Override
		public DomineeringMove getMove() {
			//x, y
		
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			int x = getX(input);
			int y = getY(input);
			System.out.println("x : " + x);
			System.out.println("y : " + y);
			System.out.flush();
			
			return new DomineeringMove(new Coord(x,y));
		}
		private int getY(String input){
			int commaAt = input.indexOf(',');
			if (commaAt == -1){ //null
				return -1;
			}
			String yString = input.substring(commaAt+1);
			int y = Integer.parseInt(yString);
			
			return y;
		}
		private int getX(String input){
			int commaAt = input.indexOf(',');
			if (commaAt == -1){ //null
				return -1;
			}
			String xString = input.substring(0,commaAt);
			int x = Integer.parseInt(xString);
			
			return x;
		}

		@Override
		public void giveMove(DomineeringMove move) {
			System.out.println("I play " + move);

		}

		@Override
		public void end(int value) {
			System.out.println("Game over. The result is " + value);

		}
		

		@Override
		public void comment(String msg) {
			System.out.println(msg);

		}

	}
	public static void main(String[] args) {
			
		DomineeringBoard board = new DomineeringBoard(4, 4);
		System.out.println(board);
	//	board.tree().firstPlayer(new commandLineDom());
		//board.tree().secondPlayer(new commandLineDom());
		}

	
}
