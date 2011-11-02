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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Observable;

 /** BarionetComm mages the cmmunication to Barix hardware
 *   http://www.barix.com/Barionet/511/
 * 
 */
public class BarionetComm extends Observable implements PossibleMsgCmds  {
    private byte[] receiveData = new byte[150];
    private InetAddress ipAdress;
    private UDPComm curUdpcomm = null;
    private RunningCmdResponseThreadRun cmdResponseThread;
    private boolean runningCmdResponseThread = false;
    private Thread thread_CmdRspBarionet;
    private boolean flagTimer = false;
    private final int timeToWait = 1000;
    private Timer timer;
    private TimerTask task;
    private int timerSleep = 100; //default timer sleep
    private int flagTimerResponse = 0;
    private Logger BarionetCommLogger;
  
    /**
     * thread for listening to response from device
     */
    public class RunningCmdResponseThreadRun implements Runnable{
        public String txtmsg = null;
        @Override
        @SuppressWarnings("static-access")
        public void run() {
             while(runningCmdResponseThread){
                try {
                    thread_CmdRspBarionet.sleep(timerSleep);
                    receiveData = new byte[150];
                    receiveData = curUdpcomm.getData();
                    if (receiveData.length > 0 ){
                    try {
                           txtmsg = new String(receiveData, "US-ASCII");
                        } catch (UnsupportedEncodingException ex) {
                            BarionetCommLogger.info("--> Barionet -Error <--");
                        }
                    
                        BarionetCommLogger.info("Barionet-RunningCmdResponseThreadRun---->>receiveUDPdata:   " + txtmsg );
                        
                        if ( txtmsg.indexOf("statechange,207,1") == 0 ){
                            setChanged();
                            notifyObservers("statechange,207,1");
                        }
                        if ( txtmsg.indexOf("statechange,208,1") == 0 ){
                            setChanged();
                            notifyObservers("statechange,208,1");
                        }
                    }
                } catch (InterruptedException ex) {
                    BarionetCommLogger.error(ex);
                }
             }
        }
    }
    
    
    /**
     *  create a barionet manager on port and ip specified
     * @param port
     * @param ip_to_send_data 
     */
    public BarionetComm(String port,
                        String ip_to_send_data)  {
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        BarionetCommLogger = Logger.getLogger(BarionetComm.class);
        BarionetCommLogger.info("--> Barionet <--");
        //TODO: ottimizzare, se barionet non risponde non deve creare altri threads in wait 
        cmdResponseThread = new RunningCmdResponseThreadRun();
        thread_CmdRspBarionet = new Thread(cmdResponseThread,"Barionet - RunningCmdResponseThreadRun");
        runningCmdResponseThread = true;
        
        curUdpcomm = new UDPComm(port);
        thread_CmdRspBarionet.start();

        try {
             ipAdress = InetAddress.getByName(ip_to_send_data);
        } catch (UnknownHostException ex) {
            BarionetCommLogger.error(ex);
        }
    }
    
    /**
     * send a command to barionet 
     * @param curCmdVal 
     * @return
     */
    public JSONObject sendCmd(int curCmdVal){
 
        String curMsgId = null;
        String curTimestamp = null;
        String curCmd = null;
        String curValue = null;
        String dataToSend = null;
        String curSource = null;
        String curTarget = null;
        String curVersion = null;
             
        runningCmdResponseThread = true;
        BarionetCommLogger.info("Barionet-sendcmd---->>comando: " + curCmdVal);
        //parse cmd & send
        switch(curCmdVal){
                case 0: //on
                     dataToSend = onCmd(Integer.toString(curCmdVal));
                break;
                case 1: //off
                     dataToSend = offCmd(Integer.toString(curCmdVal));
                break;
                case 2: //onperc
                     dataToSend = "not-operative";
                break;
                case 3: //offperc
                     dataToSend = "not-operative";
                break;
            }

        System.out.println(dataToSend);
        if (dataToSend != null && curUdpcomm != null){
            curUdpcomm.sendData(ipAdress, dataToSend.getBytes());
        }else{
           BarionetCommLogger.info("Barionet-UDPcomm null!!!!! ");
        }
        
        //get status of sent command
        JSONObject msg = checkCommand("setio", "1");

        BarionetCommLogger.info("Barionet-sendcmd---->>risposta" + msg);
        return msg;

    }

    /**
     * check arrived response
     * @param cmd
     * @param value
     * @param msgId
     * @param msgtime
     * @return
     */
    private JSONObject checkCommand(String cmd , String value){
        String dataToSend = new String(cmd);
        
        if (cmd.equalsIgnoreCase("setio")){
            //cmd = "getio," + value.split(",")[0];
            cmd = "getio," + value;
            BarionetCommLogger.info("Barionet-checkCommand---->>comando");
            System.out.println(cmd);
            curUdpcomm.sendData(ipAdress, cmd.getBytes());
            
             BarionetCommLogger.info("Barionet-checkCommand---->>wait for timer");
             if(receiveData != null && receiveData[0] != 0){
                    
                BarionetCommLogger.info("Barionet-checkCommand---->>risposta");
                try {
                    BarionetCommLogger.info(new String(receiveData, "US-ASCII").trim());
                } catch (UnsupportedEncodingException ex) {
                   BarionetCommLogger.error(ex);
                }

                try {
                    if (new String(receiveData, "US-ASCII").trim().equalsIgnoreCase("cmderr")) {

                        String msgRaw = new String("{'value':'" + "cmderr" + "'}");
                        JSONObject msg = null;
                        try {
                            msg = new JSONObject(msgRaw);
                        } catch (JSONException ex) {
                            BarionetCommLogger.error(ex);
                        }
                        receiveData = null;
                        flagTimerResponse = 0;
                        return msg;
                    } else {
                        String[] msgs = new String[3];
                        msgs = new String(receiveData, "US-ASCII").trim().split(",");
                        if (msgs[2].equalsIgnoreCase("1") || msgs[2].equalsIgnoreCase("0" )) {
                       // if (msgs[1].equalsIgnoreCase(value.split(",")[0]) && msgs[2].equalsIgnoreCase(value.split(",")[1])) {
                            String msgRaw = new String("{'value':'" + "ok" + "'}");
                            JSONObject msg = null;
                            try {
                                msg = new JSONObject(msgRaw);
                            } catch (JSONException ex) {
                               BarionetCommLogger.error(ex);
                            }
                            receiveData = null;
                            flagTimerResponse = 0;
                            return msg;
                        }else{
                            String msgRaw = new String("{'value':'" + "ko" + "'}");
                            JSONObject msg = null;
                            try {
                                msg = new JSONObject(msgRaw);
                            } catch (JSONException ex) {
                               BarionetCommLogger.error(ex);
                            }
                            receiveData = null;
                            flagTimerResponse = 0;
                            return msg;
                        }
                    }
                } catch (UnsupportedEncodingException ex) {
                    BarionetCommLogger.error(ex);
                }
              }else{
                 //not responding return error
                 BarionetCommLogger.info("Barionet-checkCommand---->>timeout");
                 //build response message
                 String msgRaw = new String("{'value':'" + "timeout" + "'}");
                 JSONObject msg = null;
                            try {
                                msg = new JSONObject(msgRaw);
                            } catch (JSONException ex) {
                                BarionetCommLogger.error(ex);
                            }
                            receiveData = null;
                            flagTimerResponse = 0;
                            return msg;
              }
                  
              
            } 
        
        return null;
    }

    
  
    @Override
    public String onCmd(String value) {
       if (value.equalsIgnoreCase("0")){
           return ("setio," + "1,1");
       }
       return null;
        
    }
 
    @Override
    public String offCmd(String value) {
        if (value.equalsIgnoreCase("1")){
           return ("setio," + "1,0");
       }
       return null;
    }

   
     @Override
    public String onPercCmd(String val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String offPercCmdString(String val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
