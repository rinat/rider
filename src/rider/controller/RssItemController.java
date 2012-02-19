package rider.controller;

import rider.Localization;
import rider.core.rss.item.IRssItem;
import rider.core.rss.item.IRssItemState;
import rider.view.IView;
import rider.view.standard.RssItemView;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public class RssItemController implements IController, CommandListener {

    // commands
    private Command _backCommand = new Command(Localization.getInstance().strings().get("kBack"), Command.BACK, 1);
    // views
    private RssItemView _view = null;
    private IRssItem _item = null;

    public RssItemController(RssItemView view, IRssItem item) {
        this._view = view;
        this._item = item;

        this._view.append(item.getDescription());
        this._view.setTitle(item.getTitle());
        addCommands(this._view);
    }

    private void addCommands(RssItemView view) {
        view.addCommand(this._backCommand);
        this._view.setCommandListener(this);
    }

    public void updateView() {
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == _backCommand) {
            backDisplay(_view);
        }
    }

    private void backDisplay(IView view) {
        changeItemState(this._item);
        IView backDisplay = view.getBackDisplay();
        backDisplay.updateView();
        view.getMIDlet().getDisplay().setCurrent(backDisplay.asDisplayable());
        backDisplay.rollbackSelection();
    }

    private void changeItemState(IRssItem item) {
        if (item.getRssItemState().getState() == IRssItemState.kUnreadState) {
            item.getRssItemState().setState(IRssItemState.kReadState);
        }
        updateView();
    }
}
