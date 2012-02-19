package rider.core.rss.item;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IRssItemState {

    public static final int kUnreadState = 0;
    public static final int kReadState = 1;

    public void setState(int state);

    public int getState();
}
