/**
 * @author Antonio Terpin
 * @year 2016
 * 
 * Thread that repeatedly updates the GUI
 */
public class MovesPrinter implements Runnable {
	private Referee r;
	
	public MovesPrinter(Referee r) {
		this.r = r;
	}

	@Override
	public void run() {
		try {
			do {
				this.r.PrintMoves();
			} while(!Thread.currentThread().isInterrupted()
					&& r.a.hareX < r.a.finishline + r.a.finishlineWidth 
					&& r.a.turtleX < r.a.finishline + r.a.finishlineWidth);
		} catch (InterruptedException e) {
			// Thread was interrupted during a blocking operation
			Thread.currentThread().interrupt(); // Set the interrupt flag again
		}
		String msg = "";
		if(r.a.hareX >= r.a.finishline && r.a.turtleX >= r.a.finishline) {
			// draw
			msg = r.a.msg_draw;
		} else if(r.a.hareX >= r.a.finishline) {
			// hare won
			msg = r.a.msg_hare;
		} else if(r.a.turtleX >= r.a.finishline) {
			// turtle won
			msg = r.a.msg_turtle;
		}

		if (!msg.isEmpty()) {
			r.a.btn_pause.setEnabled(false);
			r.a.btn_exit.setEnabled(true);
			r.a.lbl_msg.setText(msg);
			for (int i = 0; i < 3; i++) {
				r.a.lbl_msg.setVisible(false);
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) { }
				r.a.lbl_msg.setVisible(true);
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) { }
			}
		}
	}
}
