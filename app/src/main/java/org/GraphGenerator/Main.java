package org.GraphGenerator;

import java.awt.Color;
import javax.swing.JFrame;

public class Main {
    final public static int WINDOW_SIZE_X = 750;
    final public static int WINDOW_SIZE_Y = 500;
    public static boolean canEdit = true;

    public static void main(String args[]) {
        JFrame window = new JFrame("Graph Generator");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);

        DrawP drawPanel;
        if(args.length == 1) {
            drawPanel = new DrawP(args[0]);
        } else {
            drawPanel = new DrawP();
        }

        drawPanel.setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
        drawPanel.addMouseListener(drawPanel);
        drawPanel.addKeyListener(drawPanel);

        drawPanel.setBackground(Color.BLACK);
        window.add(drawPanel);
        window.setVisible(true);

        drawPanel.requestFocusInWindow();
    }
}
