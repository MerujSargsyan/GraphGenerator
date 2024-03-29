import java.awt.Point;

public class Vertex extends Shape {
    // number of vertecies connected to this vertex by one edge
    public int neighborhood;

    public Vertex(int x, int y) {
        super(x, y);
    }

    public Vertex(Point p) {
        super(p.x, p.y);
    }
    
    // @return true if x and y values are the same
    public boolean equals(Vertex v) {
        if(v == null) {
            return false;
        }
        return (this.x == v.x && this.y == v.y);
    }
}