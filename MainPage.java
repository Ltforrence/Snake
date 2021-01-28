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


public class MainPage extends JPanel implements ActionListener{

    private Board board;
    private Snake snake;

    private final int B_WIDTH = 460; //these are now base width and base height
    private final int B_HEIGHT = 460; 

    //ints for textBox size on title page
    private int xCorner = B_WIDTH/2-10;
    private int yCorner = 230;
    private int wBox = 20;
    private int hBox = 40;


    private int[] x;
    private int[] y;

    private int dots;


    private JButton[] titleButtons = new JButton[2]; //the settings and scores buttons
    private JButton startTitle; //start button on the title screen

    private JTextField nameBox;

    private int toBeAdded = 0;


    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private Timer timer;



    public MainPage(Board b, Snake s)
    {
        board = b;
        snake = s;

        timer = new Timer(140 - board.getSpeed()*20, this);
        timer.start();

        initTitle();
    }



    public void initTitle()
    {
        
        setBackground(Color.black);
        setFocusable(true);
        board.loadImages();
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        snake.setSize(B_WIDTH, B_HEIGHT);
        snake.pack();
        revalidate();

        
        


        Font KA = new Font("Karmatic Arcade", Font.PLAIN, 20); 
        FontMetrics metr = getFontMetrics(KA);

        Font KA2 = new Font("Karmatic Arcade", Font.PLAIN, 40); 
        FontMetrics metr2 = getFontMetrics(KA2);

        




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
        add(nameBox);
        setFirstDot(metr2);
    

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
                
                board.initBoard();
                nameBox.setVisible(false);
            }
        });
        setLayout(null);
        add(startTitle);
        revalidate();
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

        xCorner = B_WIDTH/2-newWidth/2;
        yCorner = 230;
        wBox = newWidth;
        hBox = 40;
        nameBox.setBounds(xCorner,230,newWidth,40);
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

            add(titleButtons[i]);
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
                    
                    if(j == 0)
                        System.out.println("Here we would init scoreboard!"); //lol nothing yet probably inithighscore or something but not yet!
                    else
                    {
                        //this.setVisible(false);
                        board.initSettings();
                        
                    }

                    nameBox.setVisible(false);
                }
            });
        }
        
        

    }


    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    
    public void doDrawing(Graphics g) {
        drawMain(g);

    }


    public void drawMain(Graphics g)
    {
        Font big = new Font("Karmatic Arcade", Font.PLAIN, 70);
        FontMetrics metr = getFontMetrics(big);
        String s = "Snake!";

        //were trying just a dot first!
        for (int z = 0; z < dots; z++) {
            g.drawImage(board.getBall(), x[z], y[z], this);
        }
        Toolkit.getDefaultToolkit().sync();


        g.setColor(board.getMainColor());
        g.setFont(big);
        g.drawString(s, (B_WIDTH - metr.stringWidth(s)) / 2, 90); 

        Font small = new Font("Karmatic Arcade", Font.PLAIN, 30);
        FontMetrics metr2 = getFontMetrics(small);
        String n = "Enter Name";
        
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(n, (B_WIDTH - metr2.stringWidth(n)) / 2, 170); 

    }


    public void move() {

        //This if statement just checks if any direction is chosen. This will happen if the game is either paused or if it has just started
        if(leftDirection || rightDirection || upDirection || downDirection)
        {
            
            
            if(toBeAdded !=0)
            {
                toBeAdded--;
                dots++;
            }

            for (int z = dots; z > 0; z--) {
                x[z] = x[(z - 1)];
                y[z] = y[(z - 1)];
            }

            if (leftDirection) {
                x[0] -= board.getDotSize();
            }

            if (rightDirection) {
                x[0] += board.getDotSize();
            }

            if (upDirection) {
                y[0] -= board.getDotSize();
            }

            if (downDirection) {
                y[0] += board.getDotSize();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        checkCorner(); //check if we need to change directions
        move();

        repaint();
    }


    //this method is for checking if you have hit or passed the corner of the box!
    public void checkCorner()
    {
        if(downDirection && y[0] >= yCorner+hBox) //bottom left
        {
            downDirection = false;
            rightDirection = true;
        }
        else if(rightDirection && x[0]>= xCorner + wBox -10) //bottom right (minus 10 because that is just added for the box)
        {
            rightDirection = false;
            upDirection = true;
        }
        else if(upDirection && y[0] <= yCorner - 20) //top right
        {
            upDirection = false;
            leftDirection = true;
        }
        else if(leftDirection && x[0] <=xCorner - 20) //topleft
        {
            leftDirection = false;
            downDirection = true;
        }

    }

    public void setFirstDot(FontMetrics metr2) // This method is for setting the first dot in the title page! really need this because it depends on if there is a name inserted!
    {
        x = new int[100]; //uhh setting it to this shouldn't mess it up??? hahah we will see. just need to make sure to call init values pretty often!
        y = new int[100];

        String str = nameBox.getText();
        
        int newWidth = metr2.stringWidth(str);
        if(newWidth ==0)
            newWidth =20;
        else
            newWidth +=10;

        xCorner = B_WIDTH/2-newWidth/2;
        wBox = newWidth;

        x[0] = xCorner-20;
        y[0] = yCorner-20;

        dots = 1;
        toBeAdded = 5;
        downDirection = true;
    }


    

    
}
