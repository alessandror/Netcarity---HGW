/*
Copyright 2011 Alex Redaelli

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.hgw4;

import java.io.IOException;
import java.net.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


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
        fakeUdpSensorLogger = Logger.getLogger(HGWTest.class.getName());
        fakeUdpSensorLogger.info("FakeUdpSensor server + client");
        createUdpSocket(udpPortServer);
        
     }
     
     public FakeUdpSensor(int udpPortToSend){
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        fakeUdpSensorLogger = Logger.getLogger(HGWTest.class.getName());
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
