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
package net.hgw4.hal;

import java.net.*;
import java.io.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * manages udp end point connections for sensors and actuators
 * 
 */
public class UDPComm {
    
    private int curPort=0;
    public byte[] receiveData = new byte[150]; //TODO: punto pericoloso 60 da parmatrizzare
    private Logger UDPCommLogger;
    public DatagramPacket receivePacket;
    public DatagramSocket serverSocket;
    
     /**
     *  create udp sock on port curPortConfig
     * @param curPortConfig
     */
    public UDPComm(String curPortConfig) {

        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        UDPCommLogger = Logger.getLogger(UDPComm.class);
        UDPCommLogger.info("--> UDPComm <--");

        curPort = Integer.parseInt(curPortConfig);
        createUdpSockServer();

    }
    
    /**
     * udp sock server
     */
    public void createUdpSockServer(){
         UDPCommLogger.info("UDPComm - in createUdpSockServer");
       
         try {
                serverSocket = new DatagramSocket(curPort);  
               
        } catch (IOException ex) {
            UDPCommLogger.error(ex);
        }
    }

    /**
     * get data from node ep
     * @return raw byte message from net
     */
    public byte[] getData(){
        UDPCommLogger.info("UDPComm - in getData");

        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            
            serverSocket.receive(receivePacket);
            
            UDPCommLogger.info("->in getData: "+ receiveData.toString());
            //indirizzo del mittente
            //InetAddress IPAddress_recv = receivePacket.getAddress(); 
            //porta del mittente
            //int port_recv = receivePacket.getPort(); 
            
        } catch (IOException ex) {
            //[ALE] test debug
            UDPCommLogger.error(ex);
        }
       
        return receiveData;
    }
    
    /**
     * send data to node ep
     * @param ipAdress
     * @param dataToSend
     */
    public void sendData(InetAddress ipAdress,byte[] dataToSend){
        UDPCommLogger.info("IpComm - in sendData");
        //mando
        DatagramPacket sendPacket = new DatagramPacket(dataToSend, dataToSend.length, ipAdress, this.curPort); 
        try {
            serverSocket.send(sendPacket);
        } catch (IOException ex) {
            UDPCommLogger.error(ex);
        }
    }
    
}

