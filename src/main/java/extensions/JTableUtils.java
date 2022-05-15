package extensions;

import javax.swing.*;

import static extensions.Message.showErrorMessage;

public class JTableUtils {

    public static Object getSelectedFirstColumnOfRow(JTable table) {
        try {
            return table.getValueAt(table.getSelectedRow(), 0);
        } catch (Exception e) {
            showErrorMessage("선택된 Process가 없습니다.");
        }
        return null;
    }

}
