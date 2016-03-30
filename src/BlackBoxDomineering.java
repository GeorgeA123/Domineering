import java.util.Scanner;

public class BlackBoxDomineering {
	private static class commandLineDom implements MoveChannel<DomineeringMove> {

		@Override
		public DomineeringMove getMove() {
			//x, y
		
			Scanner scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			int x = getX(input);
			int y = getY(input);
			System.out.println(x+","+y);
			System.out.println( y);
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
		try{
			assert(!args[0].isEmpty());
			assert(!args[1].isEmpty());
			assert(!args[2].isEmpty());
			assert(!args[3].isEmpty());
		}catch(AssertionError e){
			System.err.println("Arguments are incorrect");
			System.exit(1);
		}
		
		try{
			String order = args[0];
			String hOrV = args[1];
			int width = Integer.parseInt(args[2]);
			int height = Integer.parseInt(args[3]);
			DomineeringBoard board = new DomineeringBoard(width, height);
			if (order.equals("first") && hOrV.equals("h")){
				board.tree().firstPlayer(new commandLineDom());
			}else{
				board.tree().secondPlayer(new commandLineDom());
			}
			
			
			
		}catch(AssertionError e){
			System.err.println("Something went wrong");
			System.exit(1);
		}
	
	
	
		}
}
