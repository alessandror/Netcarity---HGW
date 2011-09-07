/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hgw4;

import java.io.IOException;
import java.net.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


/**
 *
 * @author AlessandroR
 */
public class FakeUdpSensor {
public DatagramSocket clientSocket=null;
public int curUdpPortServer = 0;
private Logger fakeUdpSensorLogger;

/**
 * 
 * @param udpPortToSend 
 * @param udpPortServer
 */
     public FakeUdpSensor(int udpPortServer,int udpPortToSend){
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        fakeUdpSensorLogger = Logger.getLogger(TestAle.class.getName());
        fakeUdpSensorLogger.info("FakeUdpSensor server + client");
        createUdpSocket(udpPortServer);
        
     }
     
     public FakeUdpSensor(int udpPortToSend){
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        fakeUdpSensorLogger = Logger.getLogger(TestAle.class.getName());
        fakeUdpSensorLogger.info("--> TestAle <--");
        fakeUdpSensorLogger.info("FakeUdpSensor client");
        curUdpPortServer = udpPortToSend;
        createUdpSocket(udpPortToSend);
     }
      
    /**
     * 
     * @param port
     */
    public void createUdpSocket(int port){
         try {
                clientSocket = new DatagramSocket();  
               
        } catch (IOException ex) {
            fakeUdpSensorLogger.error(ex);
        }
    }
    
    /**
     * 
     * @param ipAdress
     * @param dataToSend
     */
    public void sendData(InetAddress ipAdress,byte[] dataToSend){
       
        DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, ipAdress, curUdpPortServer); 
        try {
            fakeUdpSensorLogger.info("sendData");
            clientSocket.send(sendPacket);
        } catch (IOException ex) {
            fakeUdpSensorLogger.error(ex);
        }
    }
}
