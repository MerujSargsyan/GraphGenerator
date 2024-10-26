package org.GraphGenerator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.geom.Path2D;

//TO-DO: implement MouseListener
public class DrawP extends JPanel implements MouseListener, KeyListener {
    // Keeps track of objects to draw
    private ArrayList<Shape> shapes;
    private ArrayList<Vertex> vertecies;
    private ArrayList<Line> lines;

    // Keeps track of current and previous point created
    private Vertex current;
    private Vertex previous;

    // Grid constants can be manipulated to change point positions
    private final int X_SIZE = 25;
    private final int Y_SIZE = 25;
    private final int ARC_SIZE = 25;
    private final int CENTER_ADJUSTMENT = 12;
    private Grid grid = new Grid();

    // Standard keybaord inputs
    private final int KEY_DELETE = 8;
    private final int KEY_SPACE = 32;
    private final int KEY_S = 83;
    private final int KEY_A = 65;

    private int vertexCount;
    private int edgeCount;

    // Textbox to display vertex and edge count
    private JLabel text;
    private final int TEXT_WIDTH = 100;
    private final int TEXT_HEIGHT = 50;
    private final int TEXT_POSITION = 50; //X and Y offset

    private boolean darkMode;

    // Initializes text Jlabel, ArrayLists and variables
    public DrawP() {
        text = new JLabel();
        text.setSize(TEXT_HEIGHT, TEXT_WIDTH);
        text.setLocation(TEXT_POSITION, TEXT_POSITION);
        text.setVisible(true);
        text.setForeground(Color.WHITE);
        add(text);
        current = null;
        previous = null;
        vertexCount = 0;
        edgeCount = 0;
        shapes = new ArrayList<>();
        vertecies = new ArrayList<>();
        lines = new ArrayList<>();
        darkMode = true;
    }

    public DrawP(String filename) {
        this();

        ArrayList<Shape> tempShapes = FileManager.parseInput(filename);
        if(tempShapes != null) {
            shapes = tempShapes;
        } 

        tempShapes.forEach((s) -> {
            if(s instanceof Line) {
                lines.add((Line)s);
            } else {
                // TODO: manage hashtable for grid
                Vertex v = (Vertex)s;
                vertecies.add(v);
                grid.usePoint(v.x, v.y);
            }
        });

        vertexCount = vertecies.size();
        edgeCount = lines.size();
        current = vertecies.get(vertecies.size() - 1);
    }


    // Custom way to paint on JPanel
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);

        for(int e = 0; e < lines.size(); e++) {
            paintLine(lines.get(e), g);            
        }

        for(int v = 0; v < vertecies.size(); v++) {
            paintVertex(vertecies.get(v), v+1, g);
        }
    }

    // Adds a vertex to the panel and draws a line to it from the current vertex
    // @param Vertex v to be added
    public void addVertex(Vertex v, boolean isDirected) {
        shapes.add(v);
        vertecies.add(v);
        vertexCount++;
        updateCurrent(v);   
        addLine(isDirected);
        repaint();
    }

    // Updates the current and previous verticies
    // @param Vertex v to set as current vertex
    public void updateCurrent(Vertex v) {
        previous = current;
        current = v;
    }

    // Used if a point already exists on the graph so that instead of creating a
    // new one, an edge is connected to it.
    // @param Point p to connect the edge to
    public void connectVertex(Point p, boolean isDirected) {
        Vertex newV = new Vertex(grid.roundValue(p.x), grid.roundValue(p.y));
        if(newV.equals(current)) {
            updateCurrent(newV);
            return;
        }
        Line newLine = new Line(current, newV);
        if(isDirected) newLine = new DirectedLine(current, newV);
        if(!lines.contains(newLine)) {
            shapes.add(newLine);
            lines.add(newLine);
            edgeCount++;
        }
        updateCurrent(newV);
        repaint();
    }   

    // helper method to connect edges to vertecies
    public void addLine(boolean isDirected) {
        if(current.equals(previous) || current == null || previous == null) {
            return;
        }

        Line newLine = new Line(previous, current);
        if(isDirected) newLine = new DirectedLine(previous, current);
        if(!lines.contains(newLine)) {
            shapes.add(newLine);
            lines.add(newLine); 
            edgeCount++;
        }
    }

    // helper method for painting vertex Graphics on JPanel
    // @param Vertex v to paint using JPanel Graphics component
    public void paintVertex(Vertex v, int label, Graphics g) {
        if(v.equals(current)) {
            if(darkMode) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
        } else {
            if(darkMode) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }
        }
        g.fillRoundRect(v.x - CENTER_ADJUSTMENT, v.y - CENTER_ADJUSTMENT, X_SIZE, 
            Y_SIZE, ARC_SIZE, ARC_SIZE);
        g.drawString(label + "", v.x - 20, v.y + 20);
    }

    // helper method for painting edge Graphics on JPanel
    // @param Line l to paint using JPanel Graphics component
    public void paintLine(Line l, Graphics g) {
        if(darkMode) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.BLACK);
        }

        g.drawLine(l.startingPoint.x, l.startingPoint.y, l.endingPoint.x, 
            l.endingPoint.y);
        if(l instanceof DirectedLine) {
            Graphics2D g2D = (Graphics2D)g;
            Path2D.Float triangle = new Path2D.Float();

            g2D.setStroke(new BasicStroke(5));

            float[] points = ((DirectedLine)l).getPolygon();
            triangle.moveTo(points[0], points[1]);
            triangle.lineTo(points[2], points[3]);
            triangle.lineTo(points[4], points[5]);
            triangle.lineTo(points[0], points[1]);
            triangle.closePath();

            g2D.draw(triangle);

            g2D.setStroke(new BasicStroke(1));
        }
    }

    // deletes the most recent edge or graph that was painted using the
    // "backspace" key. 
    private void deleteRecent() {
        Shape s = shapes.get(shapes.size() - 1);
        if(s instanceof Vertex) {
            grid.deletePoint((Vertex)s);
            vertecies.remove((Vertex)s);
            vertexCount--;
        }
        if(s instanceof Line) {
            lines.remove(s);
            edgeCount--;
        }
        if(vertecies.size() >= 1) {
            current = vertecies.get(vertecies.size()-1);
            previous = null;
        } else if(vertecies.size() >= 2) {
            current = vertecies.get(vertecies.size()-1);
            previous = vertecies.get(vertecies.size()-2);
        } else {
            current = null;
            previous = null;
        }
        updateText(); 
        shapes.remove(s);
        repaint();
    }

    // updates the text of vertex and edge count
    public void updateText() {
        text.setText("|V| = " + vertexCount + " |E| = " + edgeCount);
    }

    // changes the color mode of the JPanel, activated by "space" key
    public void changeDarkMode() {
        if(darkMode) {
            text.setForeground(Color.BLACK);
            this.setBackground(Color.WHITE);
        } else {
            text.setForeground(Color.WHITE);
            this.setBackground(Color.BLACK);
        }
        darkMode = !darkMode;
    }
    // Overrides all MouseListener methods
    @Override
    public void mouseClicked(MouseEvent e) {}; //nothing happens

    @Override
    public void mouseReleased(MouseEvent e) {}; //nothing happens

    @Override
    public void mouseEntered(MouseEvent e) {}; //nothing happens

    @Override
    public void mouseExited(MouseEvent e) {}; //nothing happens

    // creates a new line if the vertex is available on the grid, otherwise
    // connects it to an existing vertex
    @Override
    public void mousePressed(MouseEvent e) {
        if(!Main.canEdit) return;
        Point p = getMousePosition(false);
        Vertex v = grid.usePoint(p);
        if(v == null) {
            connectVertex(p, e.isShiftDown());
        } else {
            addVertex(v, e.isShiftDown());
        }
        updateText();
    } 

    @Override
    public void keyTyped(KeyEvent e) {}; //does nothing

    @Override
    public void keyPressed(KeyEvent e) {
        if(!Main.canEdit) return;
        if(e.getKeyCode() == KEY_DELETE && shapes.size() > 0) {
            deleteRecent();
        } else if(e.getKeyCode() == KEY_SPACE) {
            changeDarkMode();
        } else if(e.getKeyCode() == KEY_S) {
            FileManager.outputToFile(shapes);        
        } else if(e.getKeyCode() == KEY_A) {
            Analyzer.BFS(lines);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}; //does nothing
}