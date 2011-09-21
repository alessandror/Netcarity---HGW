/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.hgw4.hal;

import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

/**
 *
 * @author alessandror
 */
public class EventHandler implements Observer {
private Logger eventHandlerLogger = null;

    public EventHandler(){
        eventHandlerLogger = Logger.getLogger(EventHandler.class);
        eventHandlerLogger.info("--> EventHandler <--");
    }
    public void update(Observable obs, Object x) {
      eventHandlerLogger.info("update(" + obs + "," + x + ");");
    }



}
