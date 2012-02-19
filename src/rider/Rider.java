package rider;

import rider.controller.MainController;
import rider.core.RssProvider;
import rider.utils.InternalLogger;
import rider.utils.ViewHelper;
import rider.view.DisplayThreadSafe;
import rider.view.MIDletThreadSafe;
import rider.view.standard.MainView;

import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class Rider extends MIDlet {

    private boolean _initialized = false;

    public void startApp() {
        if (!this._initialized) {
            MIDletThreadSafe threadSafeMIDlet = new MIDletThreadSafe(this);
            try {
                RssProvider provider = new RssProvider();
                MainView mainView = new MainView(threadSafeMIDlet);

                mainView.attachController(new MainController(mainView, provider));
                Display.getDisplay(this).setCurrent(mainView);
            } catch (Exception ex) {
                //#ifdef rider.debugEnabled
                InternalLogger.getInstance().fatal(ex);
                //#endif
                showAboutUnableToStartMIDlet(ex.getMessage(), threadSafeMIDlet.getDisplay());
            }
            this._initialized = true;
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    private void showAboutUnableToStartMIDlet(String description, DisplayThreadSafe display) {
        ViewHelper.showMessageBox(display,
                Localization.getInstance().strings().get("kError"),
                description,
                null,
                AlertType.ERROR);
    }
}