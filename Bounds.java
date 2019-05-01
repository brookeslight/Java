package tree;

public class Bounds {
	
	public float x;
	public float y;
	public float w;
	public float h;
	
	public Bounds(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public boolean contains(float x1, float y1) {
	    return (this.w > 0 && this.h > 0 &&
	            x1 >= this.x &&
	            x1 <= this.x + this.w &&
	            y1 >= this.y &&
	            y1 <= this.y + this.h);
	}

	public boolean intersects(Bounds b) {
	    return (this.w > 0 && this.h > 0 &&
	            b.w > 0 && b.h > 0 &&
	            b.x <= this.x + this.w &&
	            b.x + b.w >= this.x &&
	            b.y <= this.y + this.h &&
	            b.y + b.h >= this.y);
	}
}