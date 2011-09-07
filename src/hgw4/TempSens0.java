/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hgw4;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author AlessandroR
 */
public class TempSens0 extends FakeUdpSensor {
    
    InetAddress ipaddressServer;
    public Logger tempSens0Logger;
    public TimerTask task0;
    public Timer timer;
    public byte msg[] = null;
    public String id;
    public Date now = null;
    
    public TempSens0(int portToSend, String endPointId){
        super(portToSend);
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        tempSens0Logger = Logger.getLogger(TempSens0.class.getName());
        tempSens0Logger.info("TempSens0");
        
        timer = new Timer("fake tempsens - "+ endPointId );
        TimerTask task = new MyTimer();
        timer.schedule(task,1000,1000);
        id = endPointId;
    }
    
    public class MyTimer extends TimerTask {
         
            public void run() {
                 tempSens0Logger.info("TempSens0 - timer send mesg ");
                 sendMsg(buildMessage());
            }
    } 
    
    public byte[] buildMessage(){
        tempSens0Logger.info("buildMessage() ");
        //0...3bytes  = nodeId
        //0...25bytes = timestamp
        //0...13bytes = value * N valori separati da virgola
        Random random = new Random();
        now = new Date();
        String msgString = new String(id + "," + now.toString() +"," + generateRandomInt(18,34,random));
        msg = msgString.getBytes();
            return msg;
    }
    
    /**
     * 
     * @param msg 
     */
    public void sendMsg(byte[] msg){
        tempSens0Logger.info("sendMsg(byte[] msg) "+ msg);
        try {
            ipaddressServer = InetAddress.getByName("127.0.0.1");//indirizzo di spedizione
            //ipaddressServer = InetAddress.getByName("localhost");
            tempSens0Logger.info("ipaddressServer "+ ipaddressServer);
            sendData(ipaddressServer,msg);
            
        } catch (UnknownHostException ex) {
            tempSens0Logger.error(ex);
        }
    }

    private int generateRandomInt(int aStart, int aEnd, Random aRandom){
        if ( aStart > aEnd ) {
          throw new IllegalArgumentException("Start cannot exceed End.");
        }

        long range = (long)aEnd - (long)aStart + 1;
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);
        return randomNumber;
  }

}
