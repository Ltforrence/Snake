import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton; //Added this for the buttons and shtuff!
import javax.swing.Timer;
import java.util.Queue; //Added this for the nextdir queue!
import java.util.LinkedList;//Same as above!

//lol this is dumb. I needed borders
import javax.swing.*;
import java.awt.*;
import java.io.*; // for file stuff lol



//Gonna make this a JPanel as well!
public class UserInput extends JPanel {


    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 800;

    public UserInput()
    {
        initPanel();
    }

    private void initPanel()
    {

        //Here is where we will ask for the stuff! then pass all that into initUI
        int bSize = 10; // top size
        int speed = 10; // top speed
        boolean border = true; //border on or off
        String name = "Luke"; // You will be able to send in your name from the main screen too.
        int dAdded = 1;
        // All this will be returned from initSettings eventually

        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        //Snake.initUI(bSize, speed, border, name, dAdded);
    }

    //basically I just want to add a button that when clicked it calls snake





}