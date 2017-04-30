package org.tuomilabs.squarebot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.tuomilabs.squarebot.Values.*;

public class Main {
    private static Robot robot;

    private int h;
    private int w;

    private int myColor;

    private List<int[][]> colorList = new ArrayList<int[][]>();

    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        new Main().run();
    }

    private void run() throws AWTException, IOException, InterruptedException {
        robot = new Robot();

        sleep(5000);

        h = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        w = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        System.out.println(h + ", " + w);

        mouse(0, 0);
        mouse(w, 0);
        mouse(0, h);
        mouse(w, h);

        sleep(1000);

        enterGame();


//        ImageIO.write(capture, "bmp", new File("hi.bmp"));

        mouse(CENTER_X, CENTER_Y);
        myColor = capture().getRGB(CENTER_X, CENTER_Y);

        System.out.println("My color: " + myColor);

        for (int i = 0; i < STEPS; i++) {
            step();
        }

        // Display game played
        JFrame frame = new JFrame("SquareBot!");
        GridDisplayPane g = new GridDisplayPane(colorList.get(0).clone());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(g);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        for (int i = 0; i < colorList.size(); i++) {
            Thread.sleep(333);
            frame.setTitle("SquareBot!");
            g.draw(colorList.get(i));
        }
    }

    private int s = 0;

    private void step() {
        s++;
        BufferedImage capture = capture();
        System.out.println(capture.getRGB(TOP_X + 5, TOP_Y + 5));

        int[][] colors = new int[X_DIM][Y_DIM];

        for (int i = TOP_X, ci = 0; i < X_DIM * SIZE; i += SIZE, ci++) {
            for (int j = TOP_Y, cj = 0; j < Y_DIM * SIZE; j += SIZE, cj++) {
                int x = i + SIZE / 2;
                int y = j + SIZE / 2;

                colors[ci][cj] = capture.getRGB(x, y);
            }
        }

        colorList.add(colors);
    }

    private void mouse(int x, int y, int i) {
        mouse(x, y);
        sleep(i);
    }

    private void enterGame() {
        down(VK_CONTROL);
        press(65);
        up(VK_CONTROL);
        press(VK_BACK_SPACE);

        writeString("SquareBot!");

        press(LEFT);
        press(KeyEvent.VK_ENTER);
    }


    private void press(int keycode) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
        robot.delay(100);
    }

    private void press(int keycode, int delay) {
        robot.keyPress(keycode);
        robot.keyRelease(keycode);
        robot.delay(delay);
    }

    private void down(int keycode) {
        robot.keyPress(keycode);
        robot.delay(100);
    }

    private void up(int keycode) {
        robot.keyRelease(keycode);
        robot.delay(100);
    }

    private void press(int[] keycodes, int delay) {
        for (int keycode : keycodes) {
            press(keycode);
            sleep(delay);
        }
    }

    private void mouse(int x, int y) {
        robot.mouseMove(x, y);
        robot.delay(100);
    }

    private void sleep(int millis) {
        robot.delay(millis);
    }

    private void writeString(String s) {
        writeString(s, 100);
    }

    private void writeString(String s, int delay) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isUpperCase(c)) {
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.keyPress(Character.toUpperCase(c));
            robot.keyRelease(Character.toUpperCase(c));

            if (Character.isUpperCase(c)) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }

            robot.delay(delay);
        }
    }

    private BufferedImage capture() {
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        return robot.createScreenCapture(screenRect);
    }
}
