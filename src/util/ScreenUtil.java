package util;

import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Andreas on 04-02-14.
 */
public class ScreenUtil {
    public static void setFramePosition(JFrame fromFrame, JFrame toFrame) {
        toFrame.setLocationRelativeTo(fromFrame);
    }

    public static void displayError(Component current, Exception e) {
        Log.error(current.getClass().toString(), e);
        JOptionPane.showMessageDialog(current, e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
