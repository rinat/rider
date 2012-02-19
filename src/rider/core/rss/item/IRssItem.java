package rider.core.rss.item;

/**
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IRssItem {

    String getLink();

    String getTitle();

    String getDescription();

    IRssItemState getRssItemState();
}
