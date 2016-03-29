import java.util.HashSet;

import java.util.Set;

public class DomineeringBoard extends Board<DomineeringMove> {
	private static final Player h = Player.MAXIMIZER;
	private static final Player v = Player.MINIMIZER;

	private int width = 0;
	private int height = 0;

	private final boolean[][] moves;
	private final Player turn;

	public DomineeringBoard() {
		this.width = 4;
		this.height = 4;
		this.moves = new boolean[this.width][this.height];
		turn = h;

	}

	public DomineeringBoard(int width, int height) {
		this.width = width;
		this.height = height;
		this.moves = new boolean[this.width][this.height];
		turn = h;

	}

	private DomineeringBoard(boolean[][] moves, int width, int height, Player turn) {
		this.width = width;
		this.height = height;

		this.moves = moves;
		this.turn = turn;

	}

	@Override
	Player nextPlayer() {
		int numberOfMoves = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (this.moves[x][y]) {
					numberOfMoves++;
				}
			}
		}
		numberOfMoves = numberOfMoves / 2;
		return numberOfMoves % 2 == 0 ? h : v;

	}

	@Override
	public Set<DomineeringMove> availableMoves() {
		Set<DomineeringMove> setOfMoves = new HashSet<DomineeringMove>();
		if (nextPlayer().equals(h)) {
			setOfMoves = availableHMoves(); //selected and right
		} else {
			setOfMoves = availableVMoves(); //selected and down
		}
		return setOfMoves;
	}

	private Set<DomineeringMove> availableHMoves() {
		Set<DomineeringMove> setOfMoves = new HashSet<DomineeringMove>();
		for (int x = 0; x < width - 1; x++) {
			for (int y = 0; y < height; y++) {
				if (!this.moves[x + 1][y] && !this.moves[x ][y]) {
					setOfMoves.add(new DomineeringMove(new Coord(x, y)));
				}
			}
		}
		return setOfMoves;

	}

	private Set<DomineeringMove> availableVMoves() {
		
		Set<DomineeringMove> setOfMoves = new HashSet<DomineeringMove>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height-1; y++) {
				if (!this.moves[x][y + 1] && !this.moves[x ][y]) {
					setOfMoves.add(new DomineeringMove(new Coord(x, y)));
				}
			}
		}
		return setOfMoves;

	}

	@Override
	int value() {
		if (availableMoves().isEmpty()) {
			if (!nextPlayer().equals(h)) {
				return 1;
			} else {
				return -1;
			}
		}
		return 0;

	}

	private boolean[][] copyArrayMoves() {
		boolean[][] newMoves = new boolean[this.width][this.height];
		for (int i = 0; i < this.width; i++) {
			newMoves[i] = moves[i].clone();

		}

		return newMoves;

	}

	private boolean[][] add(DomineeringMove move) {
		int x = move.getMoves().getX();
		int y = move.getMoves().getY();
		boolean[][] newMoves = copyArrayMoves();

		assert (!newMoves[x][y]);

		newMoves[x][y] = true;
		if (nextPlayer().equals(h)) {
			assert (!newMoves[x + 1][y]);
			newMoves[x + 1][y] = true;
		} else {
			assert (!newMoves[x][y +1]);
			newMoves[x][y + 1] = true;
		}
		return newMoves;

	}

	@Override
	Board<DomineeringMove> play(DomineeringMove move) {
		// System.out.println(this);
		boolean[][] newBoard = add(move);
		
		return new DomineeringBoard(newBoard, this.width, this.height, nextPlayer());
	}
	public int hashCode(){
		return toString().hashCode();
		
	}
	public String toString() {
		String output = "";
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (this.moves[x][y]){
					output += "|+";
				}else{
					output += "|-";
				}
			
			}
			
			output += "|\n";
		}
		return output;

	}

}
