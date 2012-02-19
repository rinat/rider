package rider.view;

import rider.controller.IController;

import javax.microedition.lcdui.Displayable;

/**
 * Interface for all Rider views with complex logic
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IView {

    /**
     * @param controller
     */
    void attachController(IController controller);

    /**
     * @return back display that is activated after IView
     */
    IView getBackDisplay();

    /**
     * update view through controller
     */
    void updateView();

    /**
     * see MidletThreadSafe
     *
     * @return thread safe wrapper of MIDlet
     */
    MIDletThreadSafe getMIDlet();

    /**
     * @return IView as Displayable interface
     */
    Displayable asDisplayable();

    /**
     * return previous selection state
     */
    void rollbackSelection();

    /**
     * save selection state
     */
    void saveSelection();
}
