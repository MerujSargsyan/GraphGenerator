package org.GraphGenerator;

import java.awt.Point;

public class Vertex extends Shape {
    // number of vertecies connected to this vertex by one edge
    public int neighborhood;
    public int label;
    public boolean visited;

    public Vertex(int x, int y) {
        super(x, y);
    }

    public Vertex(Point p) {
        super(p.x, p.y);
    }
    
    // @return true if x and y values are the same
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Vertex)) return false;
        Vertex v = (Vertex)o;
        return (this.x == v.x && this.y == v.y);
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }

    public String outString() {
        return "\\v" + x + "," + y;
    }
}
