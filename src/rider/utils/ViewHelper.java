package rider.utils;

import rider.view.DisplayThreadSafe;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Image;

public class ViewHelper {

    public static void showMessageBox(DisplayThreadSafe display,
                                      String title,
                                      String text,
                                      Image icon,
                                      AlertType messageBoxType) {
        Alert report = new Alert(
                title,
                text,
                icon,
                messageBoxType);
        report.setTimeout(Alert.FOREVER);
        display.setCurrent(report);
    }
}
