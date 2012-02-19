/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rider.core.rss.parser;

import rider.core.rss.loader.IRssInputStream;

/**
 * Interface for classes with Parser functions
 *
 * @author Shaihutdinov Rinat; Mari State University, Yoshkar-Ola 2010; PS-41
 */
public interface IRssParser {

    /**
     * @param inputStream
     * @param listener
     * @throws NullPointerException
     * @see IRssInputStream
     */
    void parse(IRssInputStream inputStream, IRssParserListener listener) throws NullPointerException;
}
