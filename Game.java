package tree;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3278378997438038606L;
	private Thread thread;
	private boolean running = false;
	//
	private JFrame frame;
	private ArrayList<Entity> e = new ArrayList<Entity>();
	private QuadTree tree;
	
	public static void main(String[]args) {
		new Game().start();
	}
	
	public synchronized void start() {
		if(this.running == true) {
			return;
		}
		this.running = true;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	private void init() {
		this.frame = new JFrame("2D Engine");
		this.setPreferredSize(new Dimension(800, 600));
		this.setMaximumSize(new Dimension(800, 600));
		this.setMinimumSize(new Dimension(800, 600));
		this.frame.setPreferredSize(new Dimension(800, 600));
		this.frame.setMaximumSize(new Dimension(800, 600));
		this.frame.setMinimumSize(new Dimension(800, 600));
		this.frame.add(this);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(true);
		this.frame.setVisible(true);
		
		
		tree = new QuadTree(new Bounds(0,0,100,100));

	}

	@Override
	public void run() {
		this.init(); 
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(this.running == true){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				this.tick();
				updates++;
				delta--;
			}
			this.render();
			frames++;
					
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	private void tick() {
		Entity e1 = new Entity((float)(Math.random()*100),(float)(Math.random()*100));
		this.e.add(e1);
		tree.insert(e1);
		
		
		ArrayList<Entity> re = new ArrayList<Entity>();
		tree.query(new Bounds(25, 25, 50,50), re);
		for(Entity e3: re) {
			this.e.remove(e3);
		}
		
	}
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform af = g2d.getTransform();
		//start
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		//
		g2d.scale(8, 6);
		
		g.setColor(Color.white);
		tree.render(g2d);
		
		g.setColor(Color.BLUE);
		g.drawRect(25, 25, 50, 50);
		
		g.setColor(Color.RED);
		for(Entity e3: e) {
			g.drawRect((int)e3.x,(int)e3.y, 1, 1);
		}
		
		//finish
		g2d.setTransform(af);
		g.dispose();
		bs.show();
	}

}
