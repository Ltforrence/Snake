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

public class Board{

    private final int B_WIDTH = 460; //these are now base width and base height
    private final int B_HEIGHT = 460; 
    private final int DOT_SIZE = 20; // made the dot half the size! just to check how shit works here
    private final int RAND_POS = 14; // Had to change this to 39 because now the board is 40 x 40

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

    private SettingsPage sp;
    private GamePage gp;
    private VisualPage vp;
    private MainPage mp;

    public Board(Snake s) {
        register8BitFont();
        snake = s;

        initTitle();
    }

    //This inits the objects for each of these pages!. fun!


    //These here are the 3 methods for starting each of the pages. I would like to make one for the game itself, but we will see!
    public void initSettings()
    {
        sp = new SettingsPage(this, snake);
        snake.getContentPane().removeAll(); // this might work? we shall see!
        snake.getContentPane().add(sp);
    }
    public void initTitle()
    {
        mp = new MainPage(this, snake);
        snake.getContentPane().removeAll(); // this might work? we shall see!
        snake.getContentPane().add(mp);
    }
    public void initGraphicsSettings()
    {
        vp = new VisualPage(this, snake);
        snake.getContentPane().removeAll(); // this might work? we shall see!
        snake.getContentPane().add(vp);
    }
    public void initBoard()
    {
        gp = new GamePage(this, snake);
        snake.getContentPane().removeAll(); // this might work? we shall see!
        snake.getContentPane().add(gp);
    }


    //Heres all my setters and getters and stuff!
    
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
    public int getDotSize()
    {
        return DOT_SIZE;
    }
    public Image getBall()
    {
        return ball;
    }

    
    
    //actually init the board and start the game here!
    

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


    
}