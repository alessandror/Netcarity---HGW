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


import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * implements the serial protocol 0 between real hardware and hgw
 * translates serial protocol to JSONObject
 * 
 */
public class SerialProtocol {
    /**
     *
     */
    public JSONObject msg;
    String value = null;
    private Logger serialProtocolLogger;
    private int protocolNumber = 0;//default
    
    /**
     *
     */
    public SerialProtocol(int num){
    serialProtocolLogger = Logger.getLogger(SerialProtocol.class.getName());
        serialProtocolLogger.setLevel(Level.ALL);
        if (serialProtocolLogger.isLoggable(Level.INFO)){
                     serialProtocolLogger.info("SerialProtocol ");
        }
        protocolNumber = num;
    }

    /**
     * 
     * @return the message in raw byte to transmit
     */
    public String buildMessage(){
        //TODO: build serial send mesg
        return null;
    }
    
    /**
     * 
     * @param val raw byte msg
     * @return json obj created message
     */
    public JSONObject parseMessage(byte[] val){
        if (serialProtocolLogger.isLoggable(Level.INFO)){
                     serialProtocolLogger.info("parseMessage ");
        }  
        try {
            int lenval = val.length;
            switch (protocolNumber){

                case 0: //serial protocol ->0<- for fake sensor protocol
                //0...3bytes  = nodeId
                //0...25bytes = timestamp
                //0...13bytes = value * N valori separati da virgola
                
                try {
                     value = new String(val, 34, 2, "US-ASCII");
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(SerialProtocol.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
                
                case 1://serial protocol ->1<- for real temp sensor
                    //#40000,0,1,464@ serie
                try {
                    //TODO: cambiare il firmware del sens di temperatura
                    String curdata = new String(val,0,(val.length),"UTF-8");

                    int startPos = curdata.indexOf('#');
                    if (startPos >=0){
                         value = new String(curdata.substring(startPos+11, startPos+14));
                    }else{
                         value="noData";
                    }
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(SerialProtocol.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }

            //response message
            String msgRaw = new String("{"+
                                       "'value':'" + value +
                                       "'}");
            msg = new JSONObject(msgRaw);
 
        } catch (JSONException ex) {
            Logger.getLogger(SerialProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msg;
    }

}
