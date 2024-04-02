/**
 * @author Antonio Terpin
 * @year 2016
 * 
 * Monitor to synchronize the moves of the hare and the turtoise with the GUI
 */
public class Referee {
	private boolean semaphore = true;
	RunApp a;
	int hareMove = 0, turtleMove = 0, ms = 200;
	private int  modH = 3, modT = 2, offsetH = 0, nMoves = 20, cH = -1, cT = -1;
	
	public Referee(RunApp a){
		this.a = a;
	}
	
	/**
	 * Calculate the moves of the hare and the turtoise
	 * @param rH position of the hare
	 * @param rT position of the turtoise
	 * @throws InterruptedException
	 */
	public synchronized void CalculateMoves(int rH, int rT) throws InterruptedException {
		while(!semaphore) {
			// wait until the event is updated
			wait();
		}
		switch(rH) {
			case 0: case 1: case 2: case 3: {
				// the overconfident hare sleeps
				hareMove = 0;
				modH = 3;
				offsetH = 3;
			} break;
			case 4: case 5: {
				// the hare finally remembers she is taking part in the race
				hareMove = a.getWidth()/3;
				modH = 3;
				offsetH = 0;
			} break;
			case 6: case 7: case 8: case 9: {
				// the overconfident hare walks
				hareMove = a.getWidth()/6;
				modH = 3;
				offsetH = 0;
			} break;
		}
		switch(rT) {
			case 0: case 1: case 2: { 
				// turtoise sprint
				modT = 2; 
				turtleMove = a.getWidth() / 6; 
			} break;
			case 3: case 4: case 5: { 
				// turtoise is tired and moves slowly
				modT = 2; 
				turtleMove = a.getWidth() / 9;
			} break;
			case 6: case 7: case 8: case 9: { 
				// turtoise walks
				modT = 2; 
				turtleMove = a.getWidth()/12; 
			} break;
		}
		semaphore = false;
//		notifyAll();
		notify();
	}
	
	/**
	 * Updates the GUI with the new moves
	 * @throws InterruptedException
	 */
	public synchronized void PrintMoves() throws InterruptedException {
		while(semaphore) {
			// wait till the new moves are available
			wait();
		}
		if(a.firstTime) {
			a.firstTime = false;
		}
		for (int i = 0; i < nMoves; i++) {
			a.hareX += hareMove / nMoves;
			if(a.hareX > a.finishline + a.finishlineWidth) { 
				a.hareX = a.finishline + a.finishlineWidth;
				i = nMoves;
			}
			a.turtleX += turtleMove / nMoves;
			if(a.turtleX > a.finishline + a.finishlineWidth) { 
				a.turtleX = a.finishline + a.finishlineWidth;
				i = nMoves; 
			}
			cH = (cH + 1) % modH;
			cT = (cT + 1) % modT;
			a.cHare = a.hareIMG[cH + offsetH];
			a.cTurtle = a.turtleIMG[cT];
			a.repaint();
			Thread.sleep(ms);
		}
		semaphore = true;
//		notifyAll();
		notify();
	}
}
