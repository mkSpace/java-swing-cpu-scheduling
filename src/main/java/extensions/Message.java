package extensions;

import javax.swing.*;

public class Message {
    public static void showAlertMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE);
    }
    public static void showErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "에러", JOptionPane.ERROR_MESSAGE);
    }
}
