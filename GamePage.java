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


public class GamePage extends JPanel implements ActionListener{
    private final int B_WIDTH = 460; //these are now base width and base height
    private final int B_HEIGHT = 460;
    private final int RAND_POS = 14; // Had to change this to 39 because now the board is 40 x 40


    private Snake snake;
    private Board board;

    //adding this for better movement. This will allow users to buffer moves and once the timer is up then it will check the move that is next in the queue. If possible then it will do it and if not it will just go to next dir
    private Queue<Integer> nextDir;


    private int x[];
    private int y[];


    private int fullRand;
    private int fullDots;
    private int fullSpeed = 100; //set it to this to start! but it will be reset later

    private int toBeAdded = 0;


    //images we need here
    private Image ball;
    private Image apple;
    private Image head;
    private Image img;
    private Image img2;
    private Image img3;
    private Image img4;

    private boolean leftDirection = false;
    private boolean rightDirection = false;
    private boolean upDirection = false;
    private boolean downDirection = false;


    private int dots;
    private int apple_x;
    private int apple_y;
    private int fullWidth;
    private int fullHeight;
    private int gameNum = 0;

    private boolean inGame = false;

    private String[][] snakeImages = {{"Images/dotbig.png", "Images/orangedot.png", "Images/pinkborder.png", "Images/purpledot.png", "Images/bluedot.png", "Images/yellowdot.png"},{"Images/dotnoborder.png","Images/orangedot2.png", "Images/pinknoborder.png", "Images/purpledotnoborder.png", "Images/bluedot2.png", "Images/yellowdot2.png"}};
    private String[] restartImages = {"Images/restart.png", "Images/restartOrange.png", "Images/restartpink.png", "Images/restartpurple.png", "Images/restartblue.png", "Images/restartyellow.png"};
    private String[] settingsImages = {"Images/SettingsGreen.png", "Images/SettingsOrange.png", "Images/Settingspink.png", "Images/SettingsPurple.png", "Images/SettingsBlue.png", "Images/SettingsYellow.png"};

    private Timer timer;

    public GamePage(Board b, Snake s)
    {
        snake = s;
        board = b;

        setVals();

        timer = new Timer(fullSpeed, this);
        timer.start();

        initBoard();
    }


    public void initBoard() {
        inGame = true;
        
        
        
        
        //These need to be reset for when I restart the game
        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = false;
        nextDir = new LinkedList<>(); 
        gameNum++;
        loadImages(); //reload images because settings have been set now!

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


    public void initGame() {
        dots = 1;
        toBeAdded = 2; //so this is just so that you can go any direction to start!
        int start = fullHeight/2;
        if(start%20 != 0) //if is not a multiple of 20
        {
            start = start + 10; //just adds 10 cause it has to start on a multiple of 20 or else it will die!!!
        }


        for (int z = 0; z < dots; z++) {
            x[z] = start - z * board.getDotSize();
            y[z] = start;
        }
        
        locateApple();
    }


    public void loadImages() {

        ImageIcon iid = new ImageIcon(snakeImages[board.getBorderInt()][board.getMainColorInt()]);
        ball = iid.getImage();

        ImageIcon iih = new ImageIcon(snakeImages[board.getBorderInt()][board.getMainColorInt()]);
        head = iih.getImage();

        if(board.getBoardBorder())
        {
            ImageIcon iia = new ImageIcon("Images/headnoborder.png");
            apple = iia.getImage();
        }
        else
        {
            ImageIcon iia = new ImageIcon("Images/headbig.png");
            apple = iia.getImage();
        }

        ImageIcon icon = new ImageIcon(restartImages[board.getMainColorInt()]);
        img = icon.getImage();  

        ImageIcon icon2 = new ImageIcon("Images/restartred.png");
        img2 = icon2.getImage(); 

        ImageIcon icon3 = new ImageIcon(settingsImages[board.getMainColorInt()]);
        img3 = icon3.getImage();  

        ImageIcon icon4 = new ImageIcon("Images/Settingsred.png");
        img4 = icon4.getImage();
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }


    public void gameOver(Graphics g) {
        
        String msg = "Game Over";
        String msg2 = "Score ... "+dots;
        
        Font big = new Font("Karmatic Arcade", Font.PLAIN, 20 + board.getBSize()*4);
        Font small = new Font("Karmatic Arcade", Font.PLAIN, 10 + board.getBSize()*2);
        FontMetrics metr = getFontMetrics(big);
        FontMetrics metr2 = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(msg, (fullWidth - metr.stringWidth(msg)) / 2, fullHeight / 2);

        //28 should be double the font size and make it just doublespaced above it
        g.setFont(small);
        g.drawString(msg2, (fullWidth - metr2.stringWidth(msg2)) / 2, fullHeight / 2 + 56);
    }

    public void doDrawing(Graphics g) {
        
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
        else {
            gameOver(g);
        } 
    }


    public void checkApple() 
    {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            //dots = dots;
            toBeAdded = board.getDAdded()+1; // if we don't do this, it adds all dots at once which causes issues. 
            locateApple();
        }
    }


    public void locateApple() {

        int r = (int) (Math.random() * fullRand);
        apple_x = ((r * board.getDotSize()));

        r = (int) (Math.random() * fullRand);
        apple_y = ((r * board.getDotSize()));
        //need to do this check because if you don't then you will have the apple be placed inside the snake
        for(int z = 0; z<dots; z++)
        {
            if(x[z] == apple_x && y[z] == apple_y) //just looked it up because I forgot that && stopped evaluating after the first returns false which I thought it didn't do
            {
                locateApple();
            }
        }
    }


    public void checkCollision() {

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
                    board.initSettings();
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
            setLayout(null);
            add(button);
            add(button2);
            revalidate();
        }
    }



    public void setVals() //So currently this is needed to set the vals for the real board
    {
        fullWidth = 300 + 40*(board.getBSize());
        fullHeight = 300 + 40*(board.getBSize());
        fullRand = RAND_POS + 2*(board.getBSize());
        fullDots = (fullRand + 1)*(fullRand+1);
        fullSpeed = 140 - board.getSpeed()*20;
        x = new int[fullDots];
        y = new int[fullDots];
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
