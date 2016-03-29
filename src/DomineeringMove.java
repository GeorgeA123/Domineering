
public class DomineeringMove {
	private final Coord moves;

	public DomineeringMove(Coord moves) {
		this.moves = moves;
	}

	/**
	 * @return the moves
	 */
	public Coord getMoves() {
		return moves;
	}
	public boolean equals(Object move){
	
		return (this.hashCode() == move.hashCode());
		
	}
	public String toString(){
		return this.moves.toString();
	}
	public int hashCode(){
		
		return toString().hashCode();
		
	}

}
