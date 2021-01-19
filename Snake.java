

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame {

    public Snake() {
        //Here is where we will ask for the stuff! then pass all that into initUI
        int bSize = 10; // top size
        int speed = 10; // top speed
        boolean border = true; //border on or off
        String name = "Luke"; // You will be able to send in your name from the main screen too.
        
        initUI(bSize, speed, border, name);
    }
    
    private void initUI(int bSize, int speed, boolean border, String name) {
        
        add(new Board(bSize, speed, border, name));
        
        setResizable(false);
        pack();
        
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}