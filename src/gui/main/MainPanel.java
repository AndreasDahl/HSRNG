package gui.main;

import com.esotericsoftware.minlog.Log;

import javax.swing.*;

/**
 * @author Andreas
 * @since 23-02-14
 */
public class MainPanel {
    public static final String PROGRAM_NAME = "HSRNG";
    public static final int LOG_LEVEL = Log.LEVEL_DEBUG;

    private JPanel panel1;
    private JTabbedPane tabbedPane1;

    private static JFrame frame;
    private static MainPanel instance;

    public static MainPanel getInstance() {
        if (instance == null)
            instance = new MainPanel();
        return instance;
    }

    public static JFrame getMainFrame() {
        return frame;
    }

    public static void init() {
        frame = new JFrame(PROGRAM_NAME);
        frame.setContentPane(getInstance().panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Log.set(LOG_LEVEL);
        init();
        JOptionPane.showMessageDialog(frame,
                "Patch Notes:\n" +
                        "- Fixed heroes showing up as cards\n" +
                        "- Removed \"Adrenaline Rush\" card\n" +
                        "- Fixed \"Starfire\" being a common card",
                "Patch Notes", JOptionPane.PLAIN_MESSAGE);
    }
}
