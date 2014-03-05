package util;

import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andreas
 * @since 04-02-14
 */
public class ScreenUtil {
    public static void setFramePosition(Component fromFrame, JFrame toFrame) {
        toFrame.setLocationRelativeTo(fromFrame);
    }

    public static void displayError(Component current, Exception e) {
        Log.error(current.getClass().toString(), e);
        JOptionPane.showMessageDialog(current, e.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
