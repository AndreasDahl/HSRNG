package util;

import javax.swing.*;

/**
 * Created by Andreas on 04-02-14.
 */
public class ScreenUtil {
    public static void frameTransition(JFrame fromFrame, JFrame toFrame) {
        toFrame.setLocationRelativeTo(fromFrame);
    }
}
