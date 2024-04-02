import java.util.Random;

/**
 * @author Antonio Terpin
 * @year 2016
 * 
 * Thread that repeatedly updates the position of the hare and the turtle
 */
public class MovesCalculator implements Runnable{

	private Referee r;
	
	public MovesCalculator(Referee r) {
		this.r = r;
	}
	
	@Override
	public void run() {
		Random r = new Random();
		int rH, rT;
		try {
			while(!Thread.currentThread().isInterrupted()
				&& this.r.a.hareX < this.r.a.finishline + this.r.a.finishlineWidth 
				&& this.r.a.turtleX < this.r.a.finishline + this.r.a.finishlineWidth) {
				rH = r.nextInt(9);
				rT = r.nextInt(9);
				this.r.CalculateMoves(rH, rT);
			}
		} catch (InterruptedException e) {
			// Thread was interrupted during a blocking operation
			Thread.currentThread().interrupt(); // Set the interrupt flag again
		}
	}
	
}
