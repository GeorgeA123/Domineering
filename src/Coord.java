
public class Coord {
	private int x;
	private int y;
	public Coord(int x, int y){
		this.setX(x);
		this.setY(y) ;
	}
	public String toString(){
		return this.getX() + ", " + this.getY();
	}
	/**
	 * Compares two points
	 * @param point
	 * @return whether the points are the same
	 */
	public boolean equals(Coord point){
		return (point.getX() == this.getX() && point.getY() == this.getY());
		
	}
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
}
