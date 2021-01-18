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
    private Queue<Integer> nextDir = new LinkedList<>();


    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("Images/dotbig.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("Images/headbig.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("Images/dotbig.png");
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
        
        String msg = "HAHAHA LOOOOOser";
        String msg2 = "Score: "+dots;

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);

        g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2, B_HEIGHT / 2 - 24);

        
        

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
            //added this stuff to allow for a new game
            JButton button = new JButton("New Game!");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                {
                    initBoard();
                }
            });
            this.add(button);
            this.revalidate();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
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
            if(nxt == 1 && (!rightDirection)) //left
            {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            else if(nxt == 2 && (!leftDirection)) //right
            {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            else if(nxt == 3 && (!downDirection)) //up
            {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            else if(nxt == 4 && (!upDirection)) //down
            {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
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