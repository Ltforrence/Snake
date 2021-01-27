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


public class MainPage {

    private Board board;
    private Snake snake;

    private int B_WIDTH;
    private int B_HEIGHT;


    private JButton[] titleButtons = new JButton[2]; //the settings and scores buttons
    private JButton startTitle; //start button on the title screen

    private JTextField nameBox;



    public MainPage(Board b, Snake s)
    {
        board = b;
        snake = s;
    }



    public void initTitle()
    {
        board.inT();

        B_WIDTH = board.getBWidth();
        B_HEIGHT = board.getBHeight();

        board.setVals();
        board.setBackground(Color.black);
        board.setFocusable(true);
        board.loadImages();
        board.setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        snake.setSize(B_WIDTH, B_HEIGHT);
        snake.pack();

        
        board.setFirstDot();


        Font KA = new Font("Karmatic Arcade", Font.PLAIN, 20); 
        FontMetrics metr = board.getFontMetrics(KA);

        Font KA2 = new Font("Karmatic Arcade", Font.PLAIN, 40); 
        FontMetrics metr2 = board.getFontMetrics(KA2);




        nameBox = new JTextField(board.getName());
        if(board.getName().equals(""))
        {
            nameBox.setBounds(B_WIDTH/2-10,230,20,40);
        }
        else
        {
            int sw = metr2.stringWidth(board.getName());
            nameBox.setBounds(B_WIDTH/2-sw/2,230, sw,40);
        }
        nameBox.setBorder(BorderFactory.createEmptyBorder());
        //nameBox.setContentAreaFilled(false);
        nameBox.setFont(KA2);
        nameBox.setOpaque(false);
        nameBox.setForeground(Color.white);

        nameBox.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e)
            {   
                resizeBox(metr2);
            }
            @Override
            public void keyReleased(KeyEvent event) {
                
                resizeBox(metr2);
            }
            @Override
            public void keyTyped(KeyEvent event) {
                resizeBox(metr2);
            }
        });
        board.add(nameBox);
    

        addScreenButtonsTitle(KA, metr);
        
        Image newimg = board.getimg5().getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;   
        Image newimg2 = board.getimg6().getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;  

        startTitle = new JButton(new ImageIcon(newimg));
        startTitle.setRolloverIcon(new ImageIcon(newimg2));
        startTitle.setContentAreaFilled(false);
        startTitle.setBorder(BorderFactory.createEmptyBorder());
        startTitle.setFocusable(false);
        startTitle.setBounds(B_WIDTH/2-B_WIDTH/4, B_HEIGHT-B_HEIGHT/5, B_WIDTH/2, B_HEIGHT/6);
        startTitle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                startTitle.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                for(int i = 0; i<2; i++)
                {
                    titleButtons[i].setVisible(false);
                }
                board.setVals();
                board.initBoard();
                nameBox.setVisible(false);
            }
        });
        board.setLayout(null);
        board.add(startTitle);
        board.revalidate();
    }

    public void resizeBox(FontMetrics metr2)
    {
        String str = nameBox.getText();
        //System.out.println(str);
        int newWidth = metr2.stringWidth(str);
        if(newWidth ==0)
            newWidth =20;
        else
            newWidth +=10;

        board.setXCorner(B_WIDTH/2-newWidth/2);
        board.setYCorner(230);
        board.setWBox(newWidth);
        board.setHBox(40);
        nameBox.setBounds(board.getXCorner(),230,newWidth,40);
        board.setName(nameBox.getText());
    }

    

    public void addScreenButtonsTitle(Font KA, FontMetrics metr)
    {
        //I know for this one there are only 2 but it is still easier to not have to do this twice
        for(int i = 0; i<2; i++)
        {
            String text = "";
            if(i == 0)
                text = "< Scores";
            else
                text = "Settings >";

            titleButtons[i] = new JButton(text);

            titleButtons[i].setContentAreaFilled(false);
            titleButtons[i].setBorder(BorderFactory.createEmptyBorder());
            titleButtons[i].setFocusable(false);

            titleButtons[i].putClientProperty("num", i );


            titleButtons[i].setFont(KA);
            
            titleButtons[i].setBounds(B_WIDTH/4*(2*i+1) - metr.stringWidth(text)/2, 320, metr.stringWidth(text), 20);

            titleButtons[i].setForeground(Color.white);

            board.add(titleButtons[i]);
            titleButtons[i].setVisible(true);

            titleButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    int j = (Integer)((JButton)evt.getSource()).getClientProperty( "num" );
                    titleButtons[j].setForeground(board.getMainColor());

                }
            
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    int j = (Integer)((JButton)evt.getSource()).getClientProperty( "num" );
                    titleButtons[j].setForeground(Color.white);
                }
            });

            titleButtons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {

                    int j = (Integer)((JButton)e.getSource()).getClientProperty( "num" );
                    for(int i = 0; i<2; i++)
                    {
                        titleButtons[i].setVisible(false);
                    }
                    
                    startTitle.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                    board.setVals();
                    if(j == 0)
                        System.out.println("Here we would init scoreboard!"); //lol nothing yet probably inithighscore or something but not yet!
                    else
                    {
                        SettingsPage sp = new SettingsPage(board, snake);
                        sp.initSettings();//only difference from above. Coulda put this in the for loop lol
                    }

                    nameBox.setVisible(false);
                }
            });
        }
        
        

    }

    
}
