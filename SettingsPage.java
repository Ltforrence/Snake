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

public class SettingsPage {

    private Board board;
    private Snake snake;

    //buttons for this page
    private JButton[] setSize = new JButton[10];//size buttons main settings
    private JButton[] setSpeed = new JButton[5]; //speed buttons main settings
    private JButton[] setDots = new JButton[5]; //dots buttons main settings
    private JButton[] moreSettings = new JButton[2]; // visual settings and title from settings screen
    private JButton startSettings; //this is the start button in settings

    //variables for this
    private int B_WIDTH;
    private int B_HEIGHT;

    public SettingsPage(Board b, Snake s)
    {
        board = b;
        snake = s;
    }

    public void initSettings()
    {

        board.inS();

        B_HEIGHT = board.getBHeight();
        B_WIDTH = board.getBWidth();
        //setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        snake.setSize(B_WIDTH, B_HEIGHT);
        snake.pack();
        
        
        Font KA = new Font("Karmatic Arcade", Font.PLAIN, 20); 
        FontMetrics metr = board.getFontMetrics(KA);

        addSizeButtons(KA, metr);
        addSpeedButtons(KA, metr);
        addDotsButtons(KA, metr);
        addScreenButtons(KA, metr); //changing screen buttons? idk 
        
        
        Image newimg = board.getimg5().getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;   
        Image newimg2 = board.getimg6().getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;  

        startSettings = new JButton(new ImageIcon(newimg));
        startSettings.setRolloverIcon(new ImageIcon(newimg2));
        startSettings.setContentAreaFilled(false);
        startSettings.setBorder(BorderFactory.createEmptyBorder());
        startSettings.setFocusable(false);
        startSettings.setBounds(B_WIDTH/2-B_WIDTH/4, B_HEIGHT-B_HEIGHT/5, B_WIDTH/2, B_HEIGHT/6);

        startSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) // @HERE: I need to make most of this a method since it there are 3 different button that clear all this
            {
                for(int i = 0; i<10; i++)
                {
                    setSize[i].setVisible(false);
                    if(i<5)
                    {
                        setSpeed[i].setVisible(false);
                        setDots[i].setVisible(false);
                        if(i<2)
                        {
                            //setBorder[i].setVisible(false); not in this anymore!
                            moreSettings[i].setVisible(false);
                        }
                    }
                }
                startSettings.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                board.setVals();
                board.initBoard();

            }
        });
        board.setLayout(null);
        board.add(startSettings);
        startSettings.setVisible(true);
        board.revalidate();
        
    }


    public void addSizeButtons(Font KA, FontMetrics metr)
    {
        for(int i = 0; i<10; i++)
        {
            setSize[i] = new JButton(""+i);
            setSize[i].setContentAreaFilled(false);
            setSize[i].setBorder(BorderFactory.createEmptyBorder());
            setSize[i].setFocusable(false);

            //this is pretty nice tbh
            setSize[i].putClientProperty("size", i );

            String num = ""+(i+1);
            setSize[i].setFont(KA);
            if(board.getBSize() == i)
            {
                setSize[i].setForeground(board.getMainColor());
            }
            else
            {
                setSize[i].setForeground(Color.white);
            }

            setSize[i].setText(num);
            setSize[i].setBounds(B_WIDTH/40 * (3*i + 6), 60, metr.stringWidth(num), 20);

            board.add(setSize[i]);
            setSize[i].setVisible(true);

            setSize[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    setSize[board.getBSize()].setForeground(Color.white);
                    // From this stackoverflow article https://stackoverflow.com/questions/11037622/pass-variables-to-actionlistener-in-java
                    board.setBSize((Integer)((JButton)e.getSource()).getClientProperty( "size" ));
                    setSize[board.getBSize()].setForeground(board.getMainColor());
                    
                }
            });
        }
    
    }

    public void addDotsButtons(Font KA, FontMetrics metr)
    {
        for(int i = 0; i<5; i++)
        {
            setDots[i] = new JButton(""+i);
            setDots[i].setContentAreaFilled(false);
            setDots[i].setBorder(BorderFactory.createEmptyBorder());
            setDots[i].setFocusable(false);

            //this is pretty nice tbh
            setDots[i].putClientProperty("dots", i );

            String num = ""+(i+1);
            setDots[i].setFont(KA);
            if(board.getDAdded() == i)
            {
                setDots[i].setForeground(board.getMainColor());
            }
            else
            {
                setDots[i].setForeground(Color.white);
            }

            setDots[i].setText(num);
            setDots[i].setBounds(B_WIDTH/40 * (4*i + 12), 260, metr.stringWidth(num), 20);

            board.add(setDots[i]);
            setDots[i].setVisible(true);

            setDots[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    setDots[board.getDAdded()].setForeground(Color.white);
                    // From this stackoverflow article https://stackoverflow.com/questions/11037622/pass-variables-to-actionlistener-in-java
                    board.setDAdded((Integer)((JButton)e.getSource()).getClientProperty( "dots" ));
                    setDots[board.getDAdded()].setForeground(board.getMainColor());         
                }
            });
        }
    }

    public void addSpeedButtons(Font KA, FontMetrics metr)
    {
        for(int i = 0; i<5; i++)
        {
            setSpeed[i] = new JButton(""+i);
            setSpeed[i].setContentAreaFilled(false);
            setSpeed[i].setBorder(BorderFactory.createEmptyBorder());
            setSpeed[i].setFocusable(false);

            //this is pretty nice tbh
            setSpeed[i].putClientProperty("speed", i );

            String num = ""+(i+1);
            setSpeed[i].setFont(KA);
            if(board.getSpeed() == i)
            {
                setSpeed[i].setForeground(board.getMainColor());
            }
            else
            {
                setSpeed[i].setForeground(Color.white);
            }

            setSpeed[i].setText(num);
            setSpeed[i].setBounds(B_WIDTH/40 * (4*i + 12), 160, metr.stringWidth(num), 20);

            board.add(setSpeed[i]);
            setSpeed[i].setVisible(true);

            setSpeed[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    setSpeed[board.getSpeed()].setForeground(Color.white);
                    // From this stackoverflow article https://stackoverflow.com/questions/11037622/pass-variables-to-actionlistener-in-java
                    board.setSpeed((Integer)((JButton)e.getSource()).getClientProperty( "speed" ));
                    setSpeed[board.getSpeed()].setForeground(board.getMainColor());
                    
                }
            });
        }
    }



    public void addScreenButtons(Font KA, FontMetrics metr)
    {
        //I know for this one there are only 2 but it is still easier to not have to do this twice
        for(int i = 0; i<2; i++)
        {
            String text = "";
            if(i == 0)
                text = "< Title";
            else
                text = "Visual >";

            moreSettings[i] = new JButton(text);

            moreSettings[i].setContentAreaFilled(false);
            moreSettings[i].setBorder(BorderFactory.createEmptyBorder());
            moreSettings[i].setFocusable(false);

            moreSettings[i].putClientProperty("num", i );


            moreSettings[i].setFont(KA);
            
            moreSettings[i].setBounds(B_WIDTH/4*(2*i+1) - metr.stringWidth(text)/2, 320, metr.stringWidth(text), 20);

            moreSettings[i].setForeground(Color.white);

            board.add(moreSettings[i]);
            moreSettings[i].setVisible(true);

            moreSettings[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    int j = (Integer)((JButton)evt.getSource()).getClientProperty( "num" );
                    moreSettings[j].setForeground(board.getMainColor());

                }
            
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    int j = (Integer)((JButton)evt.getSource()).getClientProperty( "num" );
                    moreSettings[j].setForeground(Color.white);
                }
            });

            moreSettings[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {

                    int j = (Integer)((JButton)e.getSource()).getClientProperty( "num" );
                    //need to kill everything currently open 
                    for(int i = 0; i<10; i++)
                    {
                        setSize[i].setVisible(false);
                        if(i<5)
                        {
                            setSpeed[i].setVisible(false);
                            setDots[i].setVisible(false);
                            if(i<2)
                            {
                                //setBorder[i].setVisible(false);
                                moreSettings[i].setVisible(false);
                            }
                        }
                    }
                    startSettings.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                    board.setVals();
                    if(j == 0)
                    {
                        board.initTitle();
                    }
                    else
                    {
                        board.initGraphicsSettings();
                    }
                }
            });
        }
        
        

    }

    
}
