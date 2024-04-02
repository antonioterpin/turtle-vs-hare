import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Button;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


public class RunApp extends JFrame implements Runnable {
	int hareX = 0;
	int turtleX = 0;
	int hareY;
	int turtleY;
	int finishline;
	int finishlineWidth = 10;

	private int nHareIMG = 6;
	private int nTurtleIMG = 2;
	private int offsetTY;
	private int offsetHY;
	private int d_button = 10;
	private int x_bStart;
	private int x_bRestart;
	private int x_bStop;
	private int x_bPause;
	private int x_bResume;
	private int x_bExit;
	private int w_bStart;
	private int w_bRestart;
	private int w_bStop;
	private int w_bPause;
	private int w_bResume;
	private int w_bExit;
	private int h_b;
	private int lblWidth;
	private int lblHeight;

	GraphicsDevice gd;
	Image cHare;
	Image cTurtle;
	Image hareIMG[];
	Image turtleIMG[];
	Image bufferImage;	
	Graphics bufferImageContext;

	boolean firstTime = true;
	Thread t = new Thread(this);
	Thread c;
	Thread p;

	Button btn_start;
	Button btn_restart;
	Button btn_stop;
	Button btn_pause;
	Button btn_resume;
	Button btn_exit;

	Font b_f = new Font("TimesRoman",Font.BOLD,16);
	Font l_f = new Font("TimesRoman",Font.ITALIC, 20); 
	
	Label lbl_msg;
	String msg_start;
	String msg_draw;
	String msg_hare;
	String msg_turtle;
	String msg_resume;

	public RunApp() {
        initUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RunApp run = new RunApp();
			run.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            run.setVisible(true);
        });
    }
	
	public void initUI() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.setUndecorated(false);
		this.setResizable(false);
		gd.setFullScreenWindow(this);
		this.setSize(1000, 800);

		
		bufferImage = createImage(this.getSize().width, this.getSize().height);
		bufferImageContext = bufferImage.getGraphics();
		
		msg_start = "Press the start button to start";
		msg_draw = "Oooh, it's a draw!!";
		msg_turtle = "Turtle won!!! Unbelievable!";
		msg_hare = "Well done hare, you win!!";
		msg_resume = "Press the resume button to resume";
		
		//lbl dimension
		lblWidth = 600;
		lblHeight = 100;
		
		//btn dimension
		
		w_bStart = 70;
		w_bPause = 70;
		w_bResume = 90;
		w_bRestart = 90;
		w_bStop = 70;
		w_bExit = 70;
		
		h_b = 40;
		
		this.setBackground(Color.white);
		
		String nHimg[] = { 
			"HareRun1.png", 
			"HareRun2.png", 
			"HareRun3.png" , 
			"HareSleep1.png" , 
			"HareSleep2.png" , 
			"HareSleep3.png"
		};
		String nTimg[] = { 
			"TurtleRun1.png", 
			"TurtleRun2.png" 
		};
		hareIMG = new Image[nHareIMG];
		turtleIMG = new Image[nTurtleIMG];
		for (int i = 0; i < nHareIMG; i++) {
			try {
				hareIMG[i] = ImageIO.read(new File("img/" + nHimg[i]));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < nTurtleIMG; i++) {
			try {
				turtleIMG[i] = ImageIO.read(new File("img/" + nTimg[i]));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		setLayout(null);
		
		btn_start = new Button("Start");
		btn_restart = new Button("Restart");
		btn_stop = new Button("Stop");
		btn_pause = new Button("Pause");
		btn_resume = new Button("Resume");
		btn_exit = new Button("Exit");
		initButton();
		
		lbl_msg = new Label("lbl", Label.CENTER);
		// lbl_msg.setBackground(); to make it trasparent
		lbl_msg.setForeground(Color.BLACK);
		lbl_msg.setFont(l_f);
		lbl_msg.setText(msg_start);
		lbl_msg.setVisible(true);
		
		// add buttons
		add(btn_start);
		add(btn_restart);
		add(btn_stop);
		add(btn_pause);
		add(btn_resume);
		add(btn_exit);
				
		add(lbl_msg);
	}
	
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		draw(bufferImageContext);
		g.drawImage(bufferImage, 0, 0, this);
	}
	
	private void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getSize().width, this.getSize().height);
		// finishline
		finishline = this.getWidth() - this.getWidth() / 6;
		
		// update buttons and labels
		// btn x
		x_bResume = finishline / 2 - w_bResume / 2;
		x_bPause = x_bResume - d_button - w_bPause;
		x_bStart = x_bPause - d_button - w_bStart;
		x_bRestart = x_bResume + w_bResume + d_button;
		x_bStop = x_bRestart + w_bRestart + d_button;
		x_bExit = this.getWidth() - d_button - w_bExit;
				
		btn_start.setBounds(x_bStart, d_button, w_bStart, h_b);
		btn_pause.setBounds(x_bPause, d_button, w_bPause, h_b);
		btn_resume.setBounds(x_bResume, d_button, w_bResume, h_b);
		btn_restart.setBounds(x_bRestart, d_button, w_bRestart, h_b);
		btn_stop.setBounds(x_bStop, d_button, w_bStop, h_b);
		btn_exit.setBounds(x_bExit, d_button, w_bExit, h_b);
		
		lbl_msg.setBounds(
			this.getWidth() / 2 - lblWidth / 2, 
			this.getHeight() / 2 - lblHeight/2, 
			lblWidth, 
			lblHeight);
		
		// calculate the offsets
		offsetTY = this.getWidth()/8;
		offsetHY = offsetTY/2;
		
		// calculate y positions
		turtleY = this.getHeight()/2-offsetTY;
		hareY = this.getHeight()/2+offsetHY;
		
		drawFinishLine(g, finishline, 0, finishlineWidth, this.getHeight());
		if(firstTime) {
			// System.out.println("The run is not started yet");
			cTurtle = turtleIMG[0];
			cHare = hareIMG[0];
		}
		
		if (cTurtle != null) g.drawImage(cTurtle, turtleX, turtleY, this);
		else System.out.println("No turtle image found");
		if (cHare != null) g.drawImage(cHare, hareX, hareY, this);
		else System.out.println("No hare image found");
	}
	
	public void stop() {
		if (t != null) {
			t.interrupt();
			t = null;
		}
	}
	
	@Override
	public void run() {
		// System.out.println("Lets go!");
		Referee r = new Referee(this);
		
		c = new Thread(new MovesCalculator(r));
		p = new Thread(new MovesPrinter(r));
		
		c.start();
		p.start();
		repaint();
	}
	
	/**
	 * Initialize the buttons
	 */
	public void initButton() {
		// background color
		btn_start.setBackground(Color.GREEN);
		btn_stop.setBackground(Color.RED);
		btn_restart.setBackground(Color.BLUE);
		btn_pause.setBackground(Color.ORANGE);
		btn_resume.setBackground(Color.YELLOW);
		btn_exit.setBackground(Color.RED);
		
		// button font
		btn_start.setFont(b_f);
		btn_stop.setFont(b_f);
		btn_restart.setFont(b_f);
		btn_pause.setFont(b_f);
		btn_resume.setFont(b_f);
		btn_exit.setFont(b_f);
		
		// foreground color
		btn_start.setForeground(Color.BLACK);
		btn_restart.setForeground(Color.BLACK);
		btn_stop.setForeground(Color.BLACK);
		btn_pause.setForeground(Color.BLACK);
		btn_resume.setForeground(Color.BLACK);
		btn_exit.setForeground(Color.BLACK);

		
		// enable/disable buttons
		btn_start.setEnabled(true);
		btn_restart.setEnabled(false);
		btn_stop.setEnabled(false);
		btn_pause.setEnabled(false);
		btn_resume.setEnabled(false);
		btn_exit.setEnabled(true);

		// action listeners
		btn_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btn_click(btn_start.getLabel());
			}
		});
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btn_click(btn_stop.getLabel());
			}
		});
		btn_exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btn_click(btn_exit.getLabel());
			}
		});
		btn_pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btn_click(btn_pause.getLabel());
			}
		});
		btn_restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btn_click(btn_restart.getLabel());
			}
		});
		btn_resume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent e) {
				btn_click(btn_resume.getLabel());
			}
		});
	}
	
	/**
	 * Draw the finish line
	 * @param g graphics object
	 * @param x x position of the finish line
	 * @param y y poisition of the finish line
	 * @param width width of the finish line
	 * @param height height of the finish line
	 */
	public void drawFinishLine(Graphics g, int x, int y, int width, int height) {
		g.setColor(Color.WHITE);
		int side = width / 2; // side of the square
		for (int i = 0; i <= height; i += side) {
			g.fillRect(x, i+y, side, side);
			// switch color
			if (g.getColor() == Color.WHITE) {
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.WHITE);
			}
			g.fillRect(x + side, i + y, side, side);
		}
		
	}
	
	/**
	 * Handles the button events
	 */
	public void btn_click(String label) {
		if(label.equals("Start")) {
			t = new Thread(this);
			t.start();
			btn_start.setEnabled(false);
			btn_restart.setEnabled(true);
			btn_stop.setEnabled(true);
			btn_pause.setEnabled(true);
			btn_exit.setEnabled(false);
			lbl_msg.setVisible(false);
		} else if(label.equals("Restart")) {
			btn_exit.setEnabled(false);
			t.interrupt();
			c.interrupt();
			p.interrupt();
			hareX = turtleX = 0;
			lbl_msg.setVisible(false);
			//t = null;
			t = new Thread(this);
			t.start();
		} else if(label.equals("Stop")) {
			t.interrupt();
			c.interrupt();
			p.interrupt();
			hareX = turtleX = 0;
			repaint();
			System.out.println("End of run due to bad weather conditions");
			btn_restart.setEnabled(false);
			btn_start.setEnabled(true);
			btn_stop.setEnabled(false);
			btn_resume.setEnabled(false);
			btn_pause.setEnabled(false);
			btn_exit.setEnabled(true);
			lbl_msg.setText(msg_start);
			lbl_msg.setVisible(true);
			
		} else if(label.equals("Pause")) {
			t.interrupt();
			c.interrupt();
			p.interrupt();
			btn_pause.setEnabled(false);
			btn_restart.setEnabled(false);
			btn_stop.setEnabled(false);
			btn_start.setEnabled(false);
			btn_resume.setEnabled(true);
			btn_exit.setEnabled(true);
			lbl_msg.setText(msg_resume);
			lbl_msg.setVisible(true);
		} else if(label.equals("Resume")) {
			t = new Thread(this);
			t.start();
			btn_resume.setEnabled(false);
			btn_pause.setEnabled(true);
			btn_restart.setEnabled(true);
			btn_stop.setEnabled(true);
			btn_exit.setEnabled(false);
			lbl_msg.setVisible(false);
		} else if(label.equals("Exit")) {
			if(t!=null) {
				t.interrupt();
			}
			if(c!=null) {
				c.interrupt();
			}
			if(p!=null){
				p.interrupt();
			}
			// stop applet
			System.exit(0);
		}
	}
}
