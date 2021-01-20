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

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 800;
    private final int DOT_SIZE = 20; // made the dot half the size! just to check how shit works here
    private final int ALL_DOTS = 1600; // now this many dots fit
    private final int RAND_POS = 39; // Had to change this to 39 because now the board is 40 x 40
    private final int DELAY = 100; //I changed this to be shorter because I assume it is what makes it go faster

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    //adding this for better movement. This will allow users to buffer moves and once the timer is up then it will check the move that is next in the queue. If possible then it will do it and if not it will just go to next dir
    private Queue<Integer> nextDir;


    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private int gameNum = 0;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    private Dimension d = new Dimension(B_WIDTH, B_HEIGHT);



    public Board(int bSize, int speed, boolean border, String name, int dAdded) {
        register8BitFont();

        initSettings();
        //initBoard();
    }

    private void initSettings()
    {
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(d);

        
        ImageIcon icon = new ImageIcon("Images/restart.png");
        Image img = icon.getImage() ;  
        Image newimg = img.getScaledInstance( B_WIDTH/4, B_HEIGHT/10,  java.awt.Image.SCALE_SMOOTH ) ;  
        ImageIcon icon2 = new ImageIcon("Images/restartred.png");
        Image img2 = icon2.getImage() ;  
        Image newimg2 = img2.getScaledInstance( B_WIDTH/4, B_HEIGHT/10,  java.awt.Image.SCALE_SMOOTH ) ;  

        JButton button = new JButton(new ImageIcon(newimg));
        button.setRolloverIcon(new ImageIcon(newimg2));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusable(false);
        button.setBounds(B_WIDTH/128, 0, B_WIDTH/2, B_HEIGHT/2);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                initBoard();
                button.setVisible(false); //gotta make it invisible so that after next game another can be added instead
            }
        });
        this.setLayout(null);
        this.add(button);
        this.revalidate();
        
    }
    
    private void initBoard() {
        //These need to be reset for when I restart the game
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
        inGame = true;
        nextDir = new LinkedList<>(); 
        gameNum++;

        //Now onto restarting game
        // Hahahaha I found this bug really fast which made me really happy so I am gonna spend a couple lines talking about it
        //Basically if you allow the game to make a new key listener everytime, it adds another of the same input to the queue. Which is bad! 
        //And the delay takes double the length.
        //I will fix the other part of this (The fact that if you recieve the same direction twice it doesn't just pop the next thing off the queue), but this woulda caused issues in the future too. So I fixed this first
        if(gameNum == 1)
            addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        this.revalidate();
        loadImages();
        initGame();
        
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("Images/dotnoborder.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("Images/headnoborder.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("Images/dotnoborder.png");
        head = iih.getImage();
    }

    private void initGame() {
        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 400 - z * DOT_SIZE;
            y[z] = 400;
        }
        
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
            
            gameOver(g);

            
        } 
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        String msg2 = "Score   "+dots;
        
        Font big = new Font("Karmatic Arcade", Font.PLAIN, 54);
        Font small = new Font("Karmatic Arcade", Font.PLAIN, 27);
        FontMetrics metr = getFontMetrics(big);
        FontMetrics metr2 = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);

        //28 should be double the font size and make it just doublespaced above it
        g.setFont(small);
        g.drawString(msg2, (B_WIDTH - metr2.stringWidth(msg2)) / 2, B_HEIGHT / 2 + 56);

        
        

    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            timer.stop();
            //Eventually I would like the user to be able to scale game size, so I would like to scale this image with the game
            //JButton button = new JButton(new ImageIcon(((new ImageIcon("images/pic.jpg")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH))); //(in one line if you want to do that later)
            //scale images (from this thread. plus there is a link to another site in the thread I used https://stackoverflow.com/questions/2856480/resizing-a-imageicon-in-a-jbutton)
            //ideally the restart button would be a gif of the snake goin around the screen which i would like, but that would be more effort to make that gif
            ImageIcon icon = new ImageIcon("Images/restart.png");
            Image img = icon.getImage() ;  
            Image newimg = img.getScaledInstance( B_WIDTH/2, B_HEIGHT/5,  java.awt.Image.SCALE_SMOOTH ) ;  
            ImageIcon icon2 = new ImageIcon("Images/restartred.png");
            Image img2 = icon2.getImage() ;  
            Image newimg2 = img2.getScaledInstance( B_WIDTH/2, B_HEIGHT/5,  java.awt.Image.SCALE_SMOOTH ) ;  


            ImageIcon icon3 = new ImageIcon("Images/Settingsgreen.png");
            Image img3 = icon3.getImage() ;  
            Image newimg3 = img3.getScaledInstance( B_WIDTH/2, B_HEIGHT/5,  java.awt.Image.SCALE_SMOOTH ) ;  
            ImageIcon icon4 = new ImageIcon("Images/Settingsred.png");
            Image img4 = icon4.getImage() ;  
            Image newimg4 = img4.getScaledInstance( B_WIDTH/2, B_HEIGHT/5,  java.awt.Image.SCALE_SMOOTH ) ; 

            //button stuff in general https://stackoverflow.com/questions/5751311/creating-a-custom-button-in-java-with-jbutton
            //added this stuff to allow for a new game
            JButton button = new JButton(new ImageIcon(newimg));
            button.setRolloverIcon(new ImageIcon(newimg2));
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusable(false);
            button.setBounds(B_WIDTH/4, 0, B_WIDTH/2, B_HEIGHT/5);
            

            JButton button2 = new JButton(new ImageIcon(newimg3));
            button2.setRolloverIcon(new ImageIcon(newimg4));
            button2.setContentAreaFilled(false);
            button2.setBorder(BorderFactory.createEmptyBorder());
            button2.setFocusable(false);
            button2.setBounds(B_WIDTH/4, B_HEIGHT-B_HEIGHT/5, B_WIDTH/2, B_HEIGHT/5);
            button2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    initSettings();
                    button.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                    button2.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                }
            });
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    initBoard();
                    button.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                    button2.setVisible(false);
                }
            });
            this.setLayout(null);
            this.add(button);
            this.add(button2);
            this.revalidate();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
        //need to do this check because if you don't then you will have the apple be placed inside the snake
        for(int z = 0; z<dots; z++)
        {
            if(x[z] == apple_x && y[z] == apple_y) //just looked it up because I forgot that && stopped evaluating after the first returns false which I thought it didn't do
            {
                locateApple();
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkMovement();
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    public void checkMovement()
    {
        if (nextDir.peek() != null)
        {
            int nxt = nextDir.remove();
            if(nxt == 1) //left
            {
                if(!rightDirection && !leftDirection)
                {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                else
                {
                    checkMovement();
                }
            }
            else if(nxt == 2) //right
            {
                if((!leftDirection) && (!rightDirection))
                {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }
                else
                {
                    checkMovement();
                }
            }
            else if(nxt == 3) //up
            {
                if(!downDirection && !upDirection)
                {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
                else
                {
                    checkMovement();
                }
            }
            else if(nxt == 4) //down
            {
                if(!upDirection && !downDirection)
                {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
                else
                {
                    checkMovement();
                }
            }
        }
    }

    public void register8BitFont()
    {
        //refd this website https://www.programcreek.com/java-api-examples/?class=java.awt.GraphicsEnvironment&method=getAvailableFontFamilyNames
        //This tells you how to check if you actually got the font loaded
        try {
            GraphicsEnvironment ge = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ka1.ttf")));
       } catch (IOException|FontFormatException e) {
            //Handle exception
            System.out.println("Exception");
       }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            char c = e.getKeyChar();
            

            if ((key == KeyEvent.VK_LEFT || c == 'a')) {
                nextDir.add(1);
                
            }

            if ((key == KeyEvent.VK_RIGHT || c == 'd')) {
                nextDir.add(2);
            }

            if ((key == KeyEvent.VK_UP ||  c == 'w') ) {
                nextDir.add(3);
            }

            if ((key == KeyEvent.VK_DOWN ||  c == 's') ) {
                nextDir.add(4);
            }
        }
    }
}