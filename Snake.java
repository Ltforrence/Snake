

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Snake extends JFrame {

    public Snake() {
        //Here is where we will ask for the stuff! then pass all that into initUI
        
        
        initUI(this);
    }
    
    
    private void initUI(Snake s) {
        
        add(new Board(s));
        
        setResizable(false);
        pack();
        setSize(420,420);
        
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