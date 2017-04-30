package org.tuomilabs.squarebot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.tuomilabs.squarebot.Values.X_DIM;
import static org.tuomilabs.squarebot.Values.Y_DIM;

public class GridDisplayPane extends JPanel {
    private int[][] grid;
    private List<Rectangle> cells;

    public GridDisplayPane(int[][] grid) {
        this.grid = grid.clone();
        cells = new ArrayList<>(grid.length * grid[0].length);
    }

    public void draw(int[][] grid) {
        this.grid = grid.clone();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 1000);
    }

    @Override
    public void invalidate() {
        cells.clear();
        super.invalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        int cellWidth = width / X_DIM;
        int cellHeight = height / Y_DIM;

        int xOffset = (width - (X_DIM * cellWidth)) / 2;
        int yOffset = (height - (Y_DIM * cellHeight)) / 2;

        if (cells.isEmpty()) {
            for (int row = 0; row < X_DIM; row++) {
                for (int col = 0; col < Y_DIM; col++) {
                    Rectangle cell = new Rectangle(
                            xOffset + (col * cellWidth),
                            yOffset + (row * cellHeight),
                            cellWidth,
                            cellHeight);
                    cells.add(cell);
                }
            }
        }

        for (int i = 0; i < X_DIM; i++) {
            for (int j = 0; j < Y_DIM; j++) {
                int index = i + (j * Y_DIM);
                Rectangle cell = cells.get(index);
                g2d.setColor(new Color(grid[i][j]));
                g2d.fill(cell);
            }
        }

        for (Rectangle cell : cells) {
            g2d.draw(cell);
        }

        g2d.dispose();
    }

}