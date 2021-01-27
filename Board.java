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

    //buttons
    private JButton[] setSize = new JButton[10];//size buttons main settings
    private JButton[] setSpeed = new JButton[5]; //speed buttons main settings
    private JButton[] setDots = new JButton[5]; //dots buttons main settings
    private JButton[] setBorder = new JButton[2]; // connected or not connected on visual screen
    private JButton[] moreSettings = new JButton[2]; // visual settings and title from settings screen
    private JButton[] titleButtons = new JButton[2]; //the settings and scores buttons
    private JButton[] colorButtons = new JButton[6]; // just 2 for now but will add more soon!
    private JButton startSettings; //this is the start button in settings
    private JButton gSettings; //settings button in graphics settings
    private JButton startTitle; //start button on the title screen

    //different pages
    private VisualPage vp;
    private SettingsPage sp;
    



    private JTextField nameBox;
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

        initTitle();
        initPages(); //init all of the files that run the various pages

        timer = new Timer(fullSpeed, this);
        timer.start();
    }

    public void initPages()
    {
        vp = new VisualPage(this, snake);
        sp = new SettingsPage(this, snake);
    }

    public void initTitle()
    {
        setVals();
        inSettings = false; //in case were coming from settings
        inMain = true; //just setting us in main screen so we can draw the main shtuff!
        setBackground(Color.black);
        setFocusable(true);
        loadImages();
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        snake.setSize(B_WIDTH, B_HEIGHT);
        snake.pack();

        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = true;
        dots = 1;
        toBeAdded = 5;
        x = new int[100]; //uhh setting it to this shouldn't mess it up??? hahah we will see. just need to make sure to call init values pretty often!
        y = new int[100];
        x[0] = xCorner-20;
        y[0] = yCorner-20;


        Font KA = new Font("Karmatic Arcade", Font.PLAIN, 20); 
        FontMetrics metr = getFontMetrics(KA);

        Font KA2 = new Font("Karmatic Arcade", Font.PLAIN, 40); 
        FontMetrics metr2 = getFontMetrics(KA2);




        nameBox = new JTextField(name);
        if(name.equals(""))
        {
            nameBox.setBounds(B_WIDTH/2-10,230,20,40);
        }
        else
        {
            int sw = metr2.stringWidth(name);
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
        this.add(nameBox);
    

        addScreenButtonsTitle(KA, metr);
        
        Image newimg = img5.getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;   
        Image newimg2 = img6.getScaledInstance( B_WIDTH/2, B_HEIGHT/6,  java.awt.Image.SCALE_SMOOTH ) ;  

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
                setVals();
                initBoard();
                nameBox.setVisible(false);
            }
        });
        this.setLayout(null);
        this.add(startTitle);
        this.revalidate();
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
        name = nameBox.getText();
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

            this.add(titleButtons[i]);
            titleButtons[i].setVisible(true);

            titleButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    int j = (Integer)((JButton)evt.getSource()).getClientProperty( "num" );
                    titleButtons[j].setForeground(mainColor);

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
                    setVals();
                    if(j == 0)
                        System.out.println("Here we would init scoreboard!"); //lol nothing yet probably inithighscore or something but not yet!
                    else
                    {
                        inMain = false;
                        inGraphicsSettings = false;
                        inSettings = true;
                        sp.initSettings();//only difference from above. Coulda put this in the for loop lol
                    }

                    nameBox.setVisible(false);
                }
            });
        }
        
        

    }

    public boolean getBoardBorder()
    {
        return border;
    }

    public void setBoardBorder(boolean b)
    {
        border = b;
    }
    public int getBorderInt()
    {
        return borderInt;
    }
    public void setBorderInt(int bi)
    {
        borderInt = bi;
    }
    public int getBWidth()
    {
        return B_WIDTH;
    }
    public int getBHeight()
    {
        return B_HEIGHT;
    }
    public Color getMainColor()
    {
        return mainColor;
    }
    public void setMainColor(Color c)
    {
        mainColor = c;
    }
    public int getMainColorInt()
    {
        return mainColorInt;
    }
    public void setMainColorInt(int mci)
    {
        mainColorInt = mci;
    }
    public void setSpeed(int s)
    {
        speed = s;
    }
    public int getSpeed()
    {
        return speed;
    }
    public void setBSize(int bs)
    {
        bSize = bs;
    }
    public int getBSize()
    {
        return bSize;
    }
    public void setDAdded(int da)
    {
        dAdded = da;
    }
    public int getDAdded()
    {
        return dAdded;
    }
    public void setName(String n)
    {
        name = n;
    }
    public String getName()
    {
        return name;
    }
    public Image getimg5()
    {
        return img5;
    }
    public Image getimg6()
    {
        return img6;
    }
    public void inGS() //call when entering graphics settings
    {
        inGraphicsSettings = true;
        inSettings = false;
    }
    public void inS()
    {
        inGraphicsSettings = false;
        inSettings = true;
        inMain = false;

    }
    public void inT()
    {
        inSettings = false;
        inMain = true;
    }
        

    

    public void setVals()
    {
        fullWidth = 300 + 40*(bSize);
        fullHeight = 300 + 40*(bSize);
        fullRand = RAND_POS + 2*(bSize);
        fullDots = (fullRand + 1)*(fullRand+1);
        fullSpeed = 140 - speed*20;
        x = new int[fullDots];
        y = new int[fullDots];
    }
    
    public void initBoard() {
        

        //These need to be reset for when I restart the game
        leftDirection = false;
        rightDirection = false;
        upDirection = false;
        downDirection = false;
        inGame = true;
        inMain = false;
        inSettings = false;
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
                    sp.initSettings();
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