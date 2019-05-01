package tree;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class QuadTree {
	
	private Bounds bounds;
	private QuadTree[] nodes;
	private Entity data;
	
	public QuadTree(Bounds bounds) {
		this.bounds = bounds;
		this.data = null;
		this.nodes = null;
	}
	
	public boolean insert(Entity e) {
		
		if(this.bounds.contains(e.x, e.y) == false) { // if the bounds doesn't have the point then return
			return false;
		}
		
		if(this.data == null && this.nodes == null) { //case 1: empty node
			this.data = e;
			return true;
		}else {
			if(this.nodes == null && this.data != null) { //case 2: full node
				this.split();
				if(this.nodes[0].insert(this.data)) { //case 3: parent node
					this.data = null;
				}else if(this.nodes[1].insert(this.data)) {
					this.data = null;
				}else if(this.nodes[2].insert(this.data)) {
					this.data = null;
				}else if(this.nodes[3].insert(this.data)) {
					this.data = null;
				}else {
					System.out.println("somehow my kids don't contain my point?!?!?!");
				}
			}
			
			if(this.nodes[0].insert(e)) { //case 3: parent node
				return true;
			}
			if(this.nodes[1].insert(e)) {
				return true;
			}
			if(this.nodes[2].insert(e)) {
				return true;
			}
			if(this.nodes[3].insert(e)) {
				return true;
			}
			return false;
		}
	}
	
	public void query(Bounds r, ArrayList<Entity> result) {
		if(this.bounds.intersects(r) == false) { //i am not in the search area
			return;
		}else {
			if(this.nodes != null && this.data == null) { //has nodes
				this.nodes[0].query(r, result);
				this.nodes[1].query(r, result);
				this.nodes[2].query(r, result);
				this.nodes[3].query(r, result);
			}else if(this.data != null && this.nodes == null && r.contains(this.data.x, this.data.y) == true) { //has data
				result.add(this.data);
			}
		}
	}
	
	private void split() {
	    this.nodes = new QuadTree[4];
	    this.nodes[0] = new QuadTree(new Bounds(this.bounds.x, this.bounds.y, this.bounds.w/2, this.bounds.h/2)); //nw
	    this.nodes[1] = new QuadTree(new Bounds(this.bounds.x + this.bounds.w/2, this.bounds.y, this.bounds.w/2, this.bounds.h/2)); //ne
	    this.nodes[2] = new QuadTree(new Bounds(this.bounds.x + this.bounds.w/2, this.bounds.y+this.bounds.h/2, this.bounds.w/2, this.bounds.h/2)); //se
	    this.nodes[3] = new QuadTree(new Bounds(this.bounds.x, this.bounds.y+this.bounds.h/2, this.bounds.w/2, this.bounds.h/2)); //sw
	}
	
	public void render(Graphics g) {
		if(this.nodes != null) { //has nodes
			this.nodes[0].render(g);
			this.nodes[1].render(g);
			this.nodes[2].render(g);
			this.nodes[3].render(g);
		}else {
			g.setColor(Color.white);
			g.drawRect((int)this.bounds.x, (int)this.bounds.y, (int)this.bounds.w, (int)this.bounds.h);
		}
	}

}