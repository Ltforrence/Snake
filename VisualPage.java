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
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*; // for file stuff lol

public class VisualPage {

    private String[] colorOptions = {"Green", "Orange", "Pink", "Purple", "Blue", "Yellow"}; //need to make these custom objects I guess? not 100 percent though lol. Then I can pass it into the button and have the object also contain the color object? maybe I could just make this whole thing a dict/hash table there are better ways than what I am doing.
    private Color[] colorObjs = {Color.green, new Color(250,161,95), new Color(247,123,225), new Color(204,100,247), new Color(100,245,223), new Color(253,255,95)};

    private JButton[] colorButtons = new JButton[6]; //color buttons
    private JButton[] setBorder = new JButton[2]; // connected or not connected on visual screen
    private JButton gSettings; //settings button in graphics settings
    private Board board;
    private Snake snake;

    private int B_WIDTH;
    private int B_HEIGHT;



    public VisualPage(Board b, Snake s)
    {
        snake = s;
        board = b;
        //initGraphicsSettings();
    }

    public void initGraphicsSettings()
    {
        board.inGS();
        //will have a call here to set where we are so that the graphics paints
        //inGraphicsSettings = true;
        //inSettings = false;
        //if you don't add the next 3 lines it wont change the top line to say color lol. Some sort of java compiler issue goin on here. Never seen this before.
        //setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        //snake.setSize(B_WIDTH, B_HEIGHT);
        B_HEIGHT = board.getBHeight();
        B_WIDTH = board.getBWidth();
        snake.pack();

        Font KA = new Font("Karmatic Arcade", Font.PLAIN, 20); 
        FontMetrics metr = board.getFontMetrics(KA);

        addColorButtons(KA, metr);
        addBorderButtons(KA, metr);

        String text = "< Settings";

        gSettings = new JButton(text);

        gSettings.setContentAreaFilled(false);
        gSettings.setBorder(BorderFactory.createEmptyBorder());
        gSettings.setFocusable(false);

        gSettings.setFont(KA);
        
        gSettings.setBounds(B_WIDTH/4 - metr.stringWidth(text)/2, B_HEIGHT - 50, metr.stringWidth(text), 20);

        gSettings.setForeground(Color.white);

        board.add(gSettings);
        gSettings.setVisible(true);

        gSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                gSettings.setForeground(board.getMainColor());

            }
        
            public void mouseExited(java.awt.event.MouseEvent evt) {
                gSettings.setForeground(Color.white);
            }
            });

            
        
        gSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //need to kill everything currently open 
                gSettings.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                for(int i=0; i<6; i++)
                {
                    colorButtons[i].setVisible(false);
                    if(i<2)
                        setBorder[i].setVisible(false);
                        
                }
                //inGraphicsSettings = false;
                board.setVals();
                board.loadImages();
                SettingsPage sp = new SettingsPage(board, snake);
                sp.initSettings();
            }
        });
    }

    private void addColorButtons(Font KA, FontMetrics metr)
    {
        for(int i = 0; i <6; i++) //lol I tried to do this but it did uhhh not work! will fix it in a min
        {
            colorButtons[i] = new JButton(colorOptions[i]);

            colorButtons[i].setContentAreaFilled(false);
            colorButtons[i].setBorder(BorderFactory.createEmptyBorder());
            colorButtons[i].setFocusable(false);

            colorButtons[i].putClientProperty("color", i); // eventually putting this will also contain a color object or there will be a dict to just search for the string so this will be useful


            colorButtons[i].setFont(KA);
            
            //I could do this a lil better for sure lol
            int r = i%3;
            int t = 0;
            if(i>2) // this is just for making a second row lol
            {
                t = 1;
            }
            
            colorButtons[i].setBounds(B_WIDTH/6*(2*r+1) - metr.stringWidth(colorOptions[i])/2, 60+(60*t), metr.stringWidth(colorOptions[i]), 20);

            if(i == board.getMainColorInt())
                colorButtons[i].setForeground(board.getMainColor());
            else
                colorButtons[i].setForeground(Color.white);


            board.add(colorButtons[i]);
            colorButtons[i].setVisible(true);

            colorButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    int j = (Integer)((JButton)e.getSource()).getClientProperty( "color" );
                    board.setMainColor(colorObjs[j]);
                    colorButtons[j].setForeground(board.getMainColor());
                    colorButtons[board.getMainColorInt()].setForeground(Color.white);
                    setBorder[board.getBorderInt()].setForeground(board.getMainColor());
                    board.setMainColorInt(j);
                    
                    

                }
            });
        }
    }

    private void addBorderButtons(Font KA, FontMetrics metr)
    { //I know for this one there are only 2 but it is still easier to not have to do this twice
        for(int i = 0; i<2; i++)
        {
            String text = "";
            if(i == 0)
                text = "Connected";
            else
                text = "Unconnected";

            setBorder[i] = new JButton(text);

            setBorder[i].setContentAreaFilled(false);
            setBorder[i].setBorder(BorderFactory.createEmptyBorder());
            setBorder[i].setFocusable(false);


            setBorder[i].setFont(KA);
            
            setBorder[i].setBounds(B_WIDTH/4*(2*i+1) - metr.stringWidth(text)/2, 220, metr.stringWidth(text), 20);

            board.add(setBorder[i]);
            setBorder[i].setVisible(true);

            
        }
        if(board.getBoardBorder())
        {
            setBorder[0].setForeground(Color.white);
            setBorder[1].setForeground(board.getMainColor());
        }
        else
        {
            setBorder[0].setForeground(board.getMainColor());
            setBorder[1].setForeground(Color.white);
        }
        setBorder[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                setBorder[1].setForeground(Color.white);
                board.setBoardBorder(false);
                setBorder[0].setForeground(board.getMainColor());
                board.setBorderInt(0);
                
            }
        });
        setBorder[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                setBorder[0].setForeground(Color.white);
                board.setBoardBorder(true);
                setBorder[1].setForeground(board.getMainColor());
                board.setBorderInt(1);
                
            }
        });


    }

    
}
