/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.core.rss.parser;

import rider.core.rss.item.IRssItem;

/**
 * May be using as Parser Listener
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 * @see IRssParser
 */
public interface IRssParserListener {

    void onRssItemParsed(IRssItem item);

    void onRssItemParseError(Exception ex);
}
