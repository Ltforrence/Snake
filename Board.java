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

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 460; //these are now base width and base height
    private final int B_HEIGHT = 460; 
    private final int DOT_SIZE = 20; // made the dot half the size! just to check how shit works here
    private final int RAND_POS = 14; // Had to change this to 39 because now the board is 40 x 40

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
    private boolean inMain = false;
    private boolean inSettings = false; //out of game!
    private boolean inGraphicsSettings = false;
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
    private Snake snake;
    private int fullRand;
    private int fullDots;
    private int fullSpeed = 100; //set it to this to start! but it will be reset later

    private int toBeAdded = 0;


    //different pages
    private VisualPage vp;
    private SettingsPage sp;
    private MainPage mp;
    


    //ints for textBox size on title page
    int xCorner = B_WIDTH/2-10;
    int yCorner = 230;
    int wBox = 20;
    int hBox = 40;


    //wanted to do this as an enum but no way to get string values in that case :(
    private String[][] snakeImages = {{"Images/dotbig.png", "Images/orangedot.png", "Images/pinkborder.png", "Images/purpledot.png", "Images/bluedot.png", "Images/yellowdot.png"},{"Images/dotnoborder.png","Images/orangedot2.png", "Images/pinknoborder.png", "Images/purpledotnoborder.png", "Images/bluedot2.png", "Images/yellowdot2.png"}};
    private String[] startImages = {"Images/startgreen.png","Images/startOrange.png", "Images/startpink.png", "Images/startpurple.png", "Images/startblue.png", "Images/startyellow.png"};
    private String[] restartImages = {"Images/restart.png", "Images/restartOrange.png", "Images/restartpink.png", "Images/restartpurple.png", "Images/restartblue.png", "Images/restartyellow.png"};
    private String[] settingsImages = {"Images/SettingsGreen.png", "Images/SettingsOrange.png", "Images/Settingspink.png", "Images/SettingsPurple.png", "Images/SettingsBlue.png", "Images/SettingsYellow.png"};



    //User Settings
    private int bSize = 4;
    private int speed = 2;
    private int dAdded = 2;
    private boolean border = false;
    private int borderInt = 0;
    private String name = "";
    private Color mainColor = Color.green;
    private int mainColorInt = 0; //if this were better coded I wouldn't need this, but I do right now!!!! Ugh hopefully this will not be here forever



    public Board(Snake s) {
        register8BitFont();
        snake = s;

        initPages(); //init all of the files that run the various pages

        initTitle();
        
        timer = new Timer(fullSpeed, this);
        timer.start();
    }

    //eventually we will not need this!
    public void initPages()
    {
        vp = new VisualPage(this, snake);
        sp = new SettingsPage(this, snake);
        mp = new MainPage(this, snake);
    }

    public void initSettings()
    {
        sp.initSettings();
    }

    public void initTitle()
    {
        mp.initTitle();
    }
    public void initGraphicsSettings()
    {
        vp.initGraphicsSettings();
    }
    
    public boolean getBoardBorder() //This makes no sense name wise. but get if snake is bordered
    {
        return border;
    }

    public void setBoardBorder(boolean b) //This also makes no sense but same as above 
    {
        border = b;
    }
    public int getBorderInt()// get snake bordered or not
    {
        return borderInt;
    }
    public void setBorderInt(int bi) //set if snake is bordered or borderless the int at least(I know this naming isn't great!)
    {
        borderInt = bi;
    }
    public int getBWidth() //get base board width
    {
        return B_WIDTH;
    }
    public int getBHeight() //get base board height
    {
        return B_HEIGHT;
    }
    public Color getMainColor() //get the main color setting
    {
        return mainColor;
    }
    public void setMainColor(Color c) //set main color so all buttons are the same
    {
        mainColor = c;
    }
    public int getMainColorInt() //get the main color int (these are needed for the button colors and stuff)
    {
        return mainColorInt;
    }
    public void setMainColorInt(int mci) //set the int of the main color
    {
        mainColorInt = mci;
    }
    public void setSpeed(int s) //set snake speed from settings
    {
        speed = s;
    }
    public int getSpeed() //get snake speed
    {
        return speed;
    }
    public void setBSize(int bs) //set size of game board from settings
    {
        bSize = bs;
    }
    public int getBSize() //get size of board
    {
        return bSize;
    }
    public void setDAdded(int da) //set how many dots should be added from settings page
    {
        dAdded = da;
    }
    public int getDAdded() //get how many dots should be added
    {
        return dAdded;
    }
    public void setName(String n) //setting user name for title screen
    {
        name = n;
    }
    public String getName() //get user name 
    {
        return name;
    }
    public Image getimg5()// should rename this but getting the main colored start image
    {
        return img5;
    }
    public Image getimg6() // I should rename this but this is getting the red start image
    {
        return img6;
    }
    public void inGS() //setting boolean whe you enter Graphics settings
    {
        inGraphicsSettings = true;
        inSettings = false;
    }
    public void inS() //setting booleans when you enter Settings
    {
        inGraphicsSettings = false;
        inSettings = true;
        inMain = false;
    }
    public void inT() // setting booleans when you enter title screen
    {
        inSettings = false;
        inMain = true;
    }
    public void inG() //setting booleans when you enter the game
    {
        inGame = true;
        inMain = false;
        inSettings = false;
    }
    public int getXCorner() //getting x corner of name box in title
    {
        return xCorner;
    }
    public void setXCorner(int xc) //setting x corner of name box in title
    {
        xCorner = xc;
    }
    public int getYCorner() //get y corner of name box in title
    {
        return yCorner;
    }
    public void setYCorner(int xc) //setting where the ycorner is of the name box in title
    {
        yCorner = xc;
    }
    public int getWBox() //getting the width of box in title
    {
        return wBox;
    }
    public void setWBox(int xc) //setting the width of the box in title
    {
        wBox = xc;
    }
    public int getHBox() //getting height of name box in title
    {
        return hBox;
    }
    public void setHBox(int xc) //Setting the height of the name box in Title
    {
        hBox = xc;
    }
    public void setFirstDot() // This method is for setting the first dot in the title page! really need this because it depends on if there is a name inserted!
    {
        x = new int[100]; //uhh setting it to this shouldn't mess it up??? hahah we will see. just need to make sure to call init values pretty often!
        y = new int[100];
        x[0] = xCorner-20;
        y[0] = yCorner-20;

        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = true;
        dots = 1;
        toBeAdded = 5;
    }
        

    

    public void setVals() //So currently this is needed to set the vals for the real board
    {
        fullWidth = 300 + 40*(bSize);
        fullHeight = 300 + 40*(bSize);
        fullRand = RAND_POS + 2*(bSize);
        fullDots = (fullRand + 1)*(fullRand+1);
        fullSpeed = 140 - speed*20;
        x = new int[fullDots];
        y = new int[fullDots];
    }
    
    //actually init the board and start the game here!
    public void initBoard() {
        
        inG(); //call in g to set all of the booleans
        //These need to be reset for when I restart the game
        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = false;
        nextDir = new LinkedList<>(); 
        gameNum++;
        this.revalidate();
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

    public void loadImages() {

        ImageIcon iid = new ImageIcon(snakeImages[borderInt][mainColorInt]);
        ball = iid.getImage();

        ImageIcon iih = new ImageIcon(snakeImages[borderInt][mainColorInt]);
        head = iih.getImage();

        if(border)
        {
            ImageIcon iia = new ImageIcon("Images/headnoborder.png");
            apple = iia.getImage();
        }
        else
        {
            ImageIcon iia = new ImageIcon("Images/headbig.png");
            apple = iia.getImage();
        }

        ImageIcon icon = new ImageIcon(restartImages[mainColorInt]);
        img = icon.getImage();  

        ImageIcon icon2 = new ImageIcon("Images/restartred.png");
        img2 = icon2.getImage(); 

        ImageIcon icon3 = new ImageIcon(settingsImages[mainColorInt]);
        img3 = icon3.getImage();  

        ImageIcon icon4 = new ImageIcon("Images/Settingsred.png");
        img4 = icon4.getImage();

        ImageIcon icon5 = new ImageIcon(startImages[mainColorInt]);
        img5 = icon5.getImage();  

        ImageIcon icon6 = new ImageIcon("Images/startred.png");
        img6 = icon6.getImage();
    }

    public void initGame() {
        dots = 1;
        toBeAdded = 2; //so this is just so that you can go any direction to start!
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

        

        timer.setDelay(fullSpeed);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
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
        else if(inSettings)
        {
            //for now do nothing? I will figure something out to do here. But for now we need to clear everything
            drawSettings(g);
        }
        else if(inGraphicsSettings)
        {
            drawGraphicsSettings(g);
        }
        else if(inMain)
        {
            drawMain(g);
        }
        else if(!inGame) {
            gameOver(g);
        } 
    }


    public void drawMain(Graphics g)
    {
        Font big = new Font("Karmatic Arcade", Font.PLAIN, 70);
        FontMetrics metr = getFontMetrics(big);
        String s = "Snake!";

        //were trying just a dot first!
        for (int z = 0; z < dots; z++) {
            g.drawImage(ball, x[z], y[z], this);
        }
        Toolkit.getDefaultToolkit().sync();


        g.setColor(mainColor);
        g.setFont(big);
        g.drawString(s, (B_WIDTH - metr.stringWidth(s)) / 2, 90); 

        Font small = new Font("Karmatic Arcade", Font.PLAIN, 30);
        FontMetrics metr2 = getFontMetrics(small);
        String n = "Enter Name";
        
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(n, (B_WIDTH - metr2.stringWidth(n)) / 2, 170); 

    }

    public void drawGraphicsSettings(Graphics g)
    {
        Font big = new Font("Karmatic Arcade", Font.PLAIN, 30);
        FontMetrics metr2 = getFontMetrics(big);
        String c = "Color";
        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(c, (B_WIDTH - metr2.stringWidth(c)) / 2, 30); // lol java compiler broke... This is literally insane hahaha never found a bug like this. 

        String sp = "Styles"; // under here is where connected/unconnected goes!
        g.drawString(sp, (B_WIDTH - metr2.stringWidth(sp)) / 2, 190);
    }

    public void drawSettings(Graphics g)
    {

        Font big = new Font("Karmatic Arcade", Font.PLAIN, 30);
        FontMetrics metr2 = getFontMetrics(big);
        String si = "Size";
        g.setColor(Color.white);
        g.setFont(big);
        g.drawString(si, (B_WIDTH - metr2.stringWidth(si)) / 2, 30); //30 is where it puts the middle of the string vertical wise. Only needs to be 15 I guess?

        String sp = "Speed";
        g.drawString(sp, (B_WIDTH - metr2.stringWidth(sp)) / 2, 130);

        String d = "Dots";
        g.drawString(d, (B_WIDTH - metr2.stringWidth(d)) / 2, 230);

        
    }
    public void gameOver(Graphics g) {
        
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

    public void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            //dots = dots;
            toBeAdded = dAdded+1; // if we don't do this, it adds all dots at once which causes issues. 
            locateApple();
        }
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
                    inMain = false;
                    inGraphicsSettings = false;
                    inSettings = true;
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

    public void locateApple() {

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
        else if(inMain)
        {
            checkCorner(); //check if we need to change directions
            move();
        }

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
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/KarmaticArcade.ttf"))); //I really did just edit this font so I could use it still lol. 
       } catch (IOException|FontFormatException e) {
            //Handle exception
            System.out.println("Exception \nlol");
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