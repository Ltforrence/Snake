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

    private final int B_WIDTH = 460; //these are now base width and base height
    private final int B_HEIGHT = 460; 
    private final int DOT_SIZE = 20; // made the dot half the size! just to check how shit works here
    private final int ALL_DOTS = 225; // now this many dots will fit. This is now defunct but I leave it here for now
    private final int RAND_POS = 14; // Had to change this to 39 because now the board is 40 x 40
    private final int DELAY = 100; //I changed this to be shorter because I assume it is what makes it go faster

    private int x[];
    private int y[];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int fullWidth;
    private int fullHeight;

    //adding this for better movement. This will allow users to buffer moves and once the timer is up then it will check the move that is next in the queue. If possible then it will do it and if not it will just go to next dir
    private Queue<Integer> nextDir;


    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = false;
    private boolean inSettings = true; //out of game!
    private int gameNum = 0;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;
    private Image img;
    private Image img2;
    private Image img3;
    private Image img4;
    private Image img5;
    private Image img6;
    private JFrame snake;
    private int fullRand;
    private int fullDots;
    private int fullSpeed;


    private int bSize = 4;
    private int speed = 2;

    //buttons
    private JButton[] setSize = new JButton[10];
    private JButton[] setSpeed = new JButton[5];



    public Board(Snake s) {
        register8BitFont();
        snake = s;

        initSettings();
        //initBoard();
    }

    private void initSettings()
    {
        inSettings = true;
        setBackground(Color.black);
        setFocusable(true);
        loadImages();
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        snake.setSize(B_WIDTH, B_HEIGHT);
        snake.pack();
        

        //These will be set by settings
        //bSize = 5; // top size
        int speed = 3; // mid speed
        boolean border = true; //border on or off
        String name = "Luke"; // You will be able to send in your name from the main screen too.
        int dAdded = 1; //Dots added
        //end of settings here
        Font KA = new Font("Karmatic Arcade", Font.PLAIN, 20); 
        FontMetrics metr = getFontMetrics(KA);

        addSizeButtons(KA, metr);
        addSpeedButtons(KA, metr);
        
        
        Image newimg = img5.getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;   
        Image newimg2 = img6.getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;  

        JButton button = new JButton(new ImageIcon(newimg));
        button.setRolloverIcon(new ImageIcon(newimg2));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusable(false);
        button.setBounds(B_WIDTH/2-B_WIDTH/4, B_HEIGHT-B_HEIGHT/5, B_WIDTH/2, B_HEIGHT/6);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                for(int i = 0; i<10; i++)
                {
                    setSize[i].setVisible(false);
                    if(i<5)
                    {
                        setSpeed[i].setVisible(false);
                    }
                }
                button.setVisible(false); //gotta make it invisible so that after next game another can be added instead
                setVals();
                initBoard();

            }
        });
        this.setLayout(null);
        this.add(button);
        button.setVisible(true);
        this.revalidate();
        
    }

    private void addSizeButtons(Font KA, FontMetrics metr)
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
            if(bSize == i)
            {
                setSize[i].setForeground(Color.green);
            }
            else
            {
                setSize[i].setForeground(Color.white);
            }

            setSize[i].setText(num);
            setSize[i].setBounds(B_WIDTH/40 * (3*i + 6), 60, metr.stringWidth(num), 20);

            this.add(setSize[i]);
            setSize[i].setVisible(true);

            setSize[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    setSize[bSize].setForeground(Color.white);
                    // From this stackoverflow article https://stackoverflow.com/questions/11037622/pass-variables-to-actionlistener-in-java
                    bSize = (Integer)((JButton)e.getSource()).getClientProperty( "size" );
                    setSize[bSize].setForeground(Color.green);
                    
                }
            });
        }
    
    }

    private void addSpeedButtons(Font KA, FontMetrics metr)
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
            if(speed == i)
            {
                setSpeed[i].setForeground(Color.green);
            }
            else
            {
                setSpeed[i].setForeground(Color.white);
            }

            setSpeed[i].setText(num);
            setSpeed[i].setBounds(B_WIDTH/40 * (4*i + 12), 160, metr.stringWidth(num), 20);

            this.add(setSpeed[i]);
            setSpeed[i].setVisible(true);

            setSpeed[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    setSpeed[speed].setForeground(Color.white);
                    // From this stackoverflow article https://stackoverflow.com/questions/11037622/pass-variables-to-actionlistener-in-java
                    speed = (Integer)((JButton)e.getSource()).getClientProperty( "speed" );
                    setSpeed[speed].setForeground(Color.green);
                    
                }
            });
        }
    }

    private void setVals()
    {
        fullWidth = 300 + 40*(bSize);
        fullHeight = 300 + 40*(bSize);
        fullRand = RAND_POS + 2*(bSize);
        fullDots = (fullRand + 1)*(fullRand+1);
        fullSpeed = 140 - speed*20;
        x = new int[fullDots];
        y = new int[fullDots];
    }
    
    private void initBoard() {
        

        //These need to be reset for when I restart the game
        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = false;
        inGame = true;
        inSettings = false;
        nextDir = new LinkedList<>(); 
        gameNum++;
        this.revalidate();

        //Now onto restarting game
        // Hahahaha I found this bug really fast which made me really happy so I am gonna spend a couple lines talking about it
        //Basically if you allow the game to make a new key listener everytime, it adds another of the same input to the queue. Which is bad! 
        //And the delay takes double the length.
        //I will fix the other part of this (The fact that if you recieve the same direction twice it doesn't just pop the next thing off the queue), but this woulda caused issues in the future too. So I fixed this first
        if(gameNum == 1)
            addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(fullWidth, fullHeight));
        snake.setSize(fullWidth, fullHeight);
        snake.pack();
        this.revalidate();
        initGame();
        
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("Images/dotnoborder.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("Images/headnoborder.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("Images/dotnoborder.png");
        head = iih.getImage();

        ImageIcon icon = new ImageIcon("Images/restart.png");
        img = icon.getImage();  

        ImageIcon icon2 = new ImageIcon("Images/restartred.png");
        img2 = icon2.getImage(); 

        ImageIcon icon3 = new ImageIcon("Images/Settingsgreen.png");
        img3 = icon3.getImage();  

        ImageIcon icon4 = new ImageIcon("Images/Settingsred.png");
        img4 = icon4.getImage();

        ImageIcon icon5 = new ImageIcon("Images/startgreen.png");
        img5 = icon5.getImage();  

        ImageIcon icon6 = new ImageIcon("Images/startred.png");
        img6 = icon6.getImage();
    }

    private void initGame() {
        dots = 3;
        int start = fullHeight/2;
        if(start%20 != 0)
        {
            start = start + 10; //just adds 10 cause it has to start on a multiple of 20 or else it will die!!!
        }


        for (int z = 0; z < dots; z++) {
            x[z] = start - z * DOT_SIZE;
            y[z] = start;
        }
        
        locateApple();
        if(gameNum == 1)
        {
            timer = new Timer(fullSpeed, this);
            timer.start();
        }
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

        } 
        else if(inSettings)
        {
            //for now do nothing? I will figure something out to do here. But for now we need to clear everything
            drawSettings(g);
        }
        else if(!inGame) {
            gameOver(g);
        } 
    }

    private void drawSettings(Graphics g)
    {

        Font big = new Font("Karmatic Arcade", Font.PLAIN, 30);
        FontMetrics metr2 = getFontMetrics(big);
        String si = "Size";
        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(si, (B_WIDTH - metr2.stringWidth(si)) / 2, 30); //30 is where it puts the middle of the string vertical wise. Only needs to be 15 I guess?

        String sp = "Speed";
        g.drawString(sp, (B_WIDTH - metr2.stringWidth(sp)) / 2, 130);

        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        String msg2 = "Score ... "+dots;
        
        Font big = new Font("Karmatic Arcade", Font.PLAIN, 20 + bSize*4);
        Font small = new Font("Karmatic Arcade", Font.PLAIN, 10 + bSize*2);
        FontMetrics metr = getFontMetrics(big);
        FontMetrics metr2 = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(msg, (fullWidth - metr.stringWidth(msg)) / 2, fullHeight / 2);

        //28 should be double the font size and make it just doublespaced above it
        g.setFont(small);
        g.drawString(msg2, (fullWidth - metr2.stringWidth(msg2)) / 2, fullHeight / 2 + 56);

        
        

    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

        //This if statement just checks if any direction is chosen. This will happen if the game is either paused or if it has just started
        if(leftDirection || rightDirection || upDirection || downDirection)
        {
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
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= fullHeight) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= fullWidth) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }
        
        if (!inGame) {
            //timer.stop();
            //Eventually I would like the user to be able to scale game size, so I would like to scale this image with the game
            //JButton button = new JButton(new ImageIcon(((new ImageIcon("images/pic.jpg")).getImage()).getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH))); //(in one line if you want to do that later)
            //scale images (from this thread. plus there is a link to another site in the thread I used https://stackoverflow.com/questions/2856480/resizing-a-imageicon-in-a-jbutton)
            //ideally the restart button would be a gif of the snake goin around the screen which i would like, but that would be more effort to make that gif 
            Image newimg  =  img.getScaledInstance( fullWidth/2, fullHeight/5,  java.awt.Image.SCALE_SMOOTH ) ;  
            Image newimg2 = img2.getScaledInstance( fullWidth/2, fullHeight/5,  java.awt.Image.SCALE_SMOOTH ) ;  
            Image newimg3 = img3.getScaledInstance( fullWidth/2, fullHeight/5,  java.awt.Image.SCALE_SMOOTH ) ;    
            Image newimg4 = img4.getScaledInstance( fullWidth/2, fullHeight/5,  java.awt.Image.SCALE_SMOOTH ) ; 

            //button stuff in general https://stackoverflow.com/questions/5751311/creating-a-custom-button-in-java-with-jbutton
            //added this stuff to allow for a new game
            JButton button = new JButton(new ImageIcon(newimg));
            button.setRolloverIcon(new ImageIcon(newimg2));
            button.setContentAreaFilled(false);
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setFocusable(false);
            button.setBounds(fullWidth/4, 0, fullWidth/2, fullHeight/5);
            

            JButton button2 = new JButton(new ImageIcon(newimg3));
            button2.setRolloverIcon(new ImageIcon(newimg4));
            button2.setContentAreaFilled(false);
            button2.setBorder(BorderFactory.createEmptyBorder());
            button2.setFocusable(false);
            button2.setBounds(fullWidth/4, fullHeight-fullHeight/5, fullWidth/2, fullHeight/5);
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

        int r = (int) (Math.random() * fullRand);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * fullRand);
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
            else if(nxt == 5) //pause
            {
                //added this in here because if you happened to have clicked it right after you queued a move then it will just ignore the pause
                upDirection = false;
                downDirection = false;
                rightDirection = false;
                leftDirection = false;
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
            if(c == 'p')
            {
                nextDir.add(5);
            }
        }
    }
}