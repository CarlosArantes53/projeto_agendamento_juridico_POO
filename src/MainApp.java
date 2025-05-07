import gui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            
            }
        });
    }
}