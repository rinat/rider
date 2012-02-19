package rider.core.rss.feed;

public interface IRssFeedState {

    public static final int kNotLoadedState = 0;
    public static final int kLoadingState = 1;
    public static final int kLoadedState = 2;
    public static final int kLoadingErrorState = 3;

    public void setState(int state);

    public int getState();
}
