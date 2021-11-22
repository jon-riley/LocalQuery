package GUI;

import javax.swing.*;

public class testGUI extends JFrame {
    testGUI() {
        super("Local Query");
        add(new lqGUI(), "Center");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 750);
        setVisible(true);
    }

    public static void main(String[] args) {
        new testGUI();
    }
}