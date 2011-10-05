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
import java.util.Observer;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Observable;

public class CommTech  {
 
    private boolean runningDataCollectorThread = false;
    private boolean runningDataCommanderThread = false;
    private DataCollectorThreadRun dataCollectorThread;
    private DataCommanderThreadRun dataCommanderThread;
    private String protocolId;
    private int timing;
    public byte[] curDataIn = null;
    private int curDataOut;
    public byte[] sensData;
    public String selectedCommType;
    public String selectedProtocol;
    public UDPComm curUDPComm;
    public BluetoothComm curBtComm;
    public SerialComm curSerialComm;
    private Logger commTechLogger;
    private JSONObject curJsonCommTechConfig;
    public Thread thread_dataCollector;
    public Thread thread_dataCommander;
    public JSONObject curCommandStatus = null;
    public BarionetComm curBarionet;
    private int timerSleep = 500; //default timer sleep
    private int lenDataQueue = 100;
    public WebCamClientComm curWebCamClientComm;
    public ZNetComm curZNetComm;
    public DataQueue curUDPCommDataQueue;
    public DataQueue curSerialCommDataQueue;
    public DataQueue curBarionetDataQueue;
    public DataQueue curWebCamClientCommDataQueue;
    public DataQueue curZNetCommDataQueue;
    public EventHandler curEventHandlerCommTech = null;

    /**
     * manages the data collector thread for sensors
     */
    public class DataCollectorThreadRun implements Runnable{

        @SuppressWarnings("static-access")
        @Override
        public void run() {
            while(runningDataCollectorThread){
                try {
                    thread_dataCollector.sleep(timerSleep);
                    commTechLogger.info("CommTech - in DataCollectorThreadRun");
                    if (selectedCommType.equalsIgnoreCase("socket_udp")) {
                        curUDPCommDataQueue.insertData( curUDPComm.getData() );
                    } else if (selectedCommType.equalsIgnoreCase("serial")) {
                        curSerialCommDataQueue.insertData( curSerialComm.getData() );
                    } else if (selectedCommType.equalsIgnoreCase("zb")) {
                        curDataIn = null;
                    } else if (selectedCommType.equalsIgnoreCase("bt")) {
                        curDataIn = null;
                    } else if (selectedCommType.equalsIgnoreCase("http")) {
                        curWebCamClientCommDataQueue.insertData( curWebCamClientComm.getData() );
                    } else if (selectedCommType.equalsIgnoreCase("znetserial")) {
                        curZNetCommDataQueue.insertData(curZNetComm.getData());
                    } 
                } catch (InterruptedException ex) {
                    commTechLogger.error(ex);
                }
          }
            
        }
    }
    
    /**
     * manages the command collector thread for actuators
     */
    public class DataCommanderThreadRun implements Runnable{
        
        @SuppressWarnings("static-access")
        @Override
        public void run() {
            while(runningDataCommanderThread){
                try {
                    
                    thread_dataCommander.sleep(timerSleep);
                    commTechLogger.info("CommTech - in RunningDataCommanderThread");

                    if (curDataOut != -1) {
                       if(selectedCommType.equalsIgnoreCase("zb")){
                              //TODO: zb

                        }else if(selectedCommType.equalsIgnoreCase("bt")){
                              //TODO: bt
                       
                        }else if(selectedCommType.equalsIgnoreCase("barionet")){
                              curCommandStatus = curBarionet.sendCmd(curDataOut);
                              curDataOut = -1;
                        }else if(selectedCommType.equalsIgnoreCase("http")){
                              curCommandStatus = curWebCamClientComm.sendCmd(curDataOut);
                              curDataOut = -1;
                        }
                        commTechLogger.info("DataCommanderThreadRun-curCommandStatus " + selectedCommType + ":" + curCommandStatus);
                    }

                    //curDataOut = -1;

                } catch (InterruptedException ex) {
                     commTechLogger.error(ex);
                }
           }
        }
    }
    
    /**
     * start the communication technology associated with endpoint
     * @param comTech
     */
    public CommTech(JSONObject comTech){
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        commTechLogger = Logger.getLogger(CommTech.class);
        commTechLogger.info("--> CommTech <--");

        timing = 0;
        curJsonCommTechConfig = comTech;
        createAndConfigCommTech(comTech);
        
        dataCollectorThread = new DataCollectorThreadRun();
        dataCommanderThread = new DataCommanderThreadRun();
        try {
            thread_dataCollector = new Thread(dataCollectorThread, "Commetch - dataCollectorThread - " + comTech.getString("comm_type"));
            thread_dataCommander = new Thread(dataCommanderThread, "Commetch - dataCommanderThread - " + comTech.getString("comm_type"));
        } catch (JSONException ex) {
            commTechLogger.error(ex);
        }

        commTechLogger.info("CommTech - create threads");
        
    }

 
    

    /**
     * this method is called from Configurator for setting up the communication
     * technology as ip, serial, bluetooth, zigbee, ...
     * 
     * @param comTech 
     */
    public void createAndConfigCommTech(JSONObject comTech){
        commTechLogger.info("CommTech - in createAndConfigCommTech");

        try {
            selectedCommType = comTech.getString("comm_type");
            
            if(selectedCommType.equalsIgnoreCase("socket_udp")){
                  curUDPComm = new UDPComm(comTech.getString("port"));
                  curUDPCommDataQueue = new DataQueue("checkValue", lenDataQueue );
            }else if(selectedCommType.equalsIgnoreCase("zb")){
                  //TODO: zb

            }else if(selectedCommType.equalsIgnoreCase("bt")){
                  //TODO: bt
                
            }else if(selectedCommType.equalsIgnoreCase("serial")){
                  curSerialComm = new SerialComm(comTech.getString("port"),
                                                 comTech.getString("baudrate"));
                  curSerialCommDataQueue = new DataQueue("checkValue", lenDataQueue );
            }else if(selectedCommType.equalsIgnoreCase("barionet")){
                  curBarionet = new BarionetComm(comTech.getString("port"),
                                                 comTech.getString("ip"));
                  curBarionetDataQueue = new DataQueue("checkValue", lenDataQueue );
                  
            }else if(selectedCommType.equalsIgnoreCase("http")){
                  curWebCamClientComm = new WebCamClientComm(comTech.getString("ip"));
                  curWebCamClientCommDataQueue = new DataQueue("checkValue", lenDataQueue );
            }else if(selectedCommType.equalsIgnoreCase("znetserial")){
                  curZNetComm = new ZNetComm( comTech.getString("port"),
                                              comTech.getString("baudrate"));
                  curZNetCommDataQueue = new DataQueue("checkValue", lenDataQueue );
            }
        
        } catch (JSONException ex) {
             commTechLogger.error(ex);
        }
    }

    
    /**
     * pass event handler to an observable class
    */
    void setInternalEventHandler(EventHandler myEventHandler) {
        commTechLogger.info("CommTech - in setEventHandler");
        curEventHandlerCommTech = myEventHandler;

        //add eventhandler to barionet
        if (curBarionet != null){
            curBarionet.addObserver( curEventHandlerCommTech );
        }

    }

    /**
     * set event handler the receiving class 
    */
    void setExternalEventHandler(Object extObservable) {
        
        //add event handler to barionet
       if (curBarionet != null){
            curBarionet.addObserver( (Observer) extObservable);
        }
       
       
    }

    /**
     * get the status of sent command
     * @return JSONObject
     */
    public JSONObject getCurCommandStatus(){
        //System.out.println("getCurCommandStatus " + selectedCommType + ":" + curCommandStatus);
        return curCommandStatus;
    }
    
    /**
     * get current sensor data filterd by protocol
     * @return a json description of the raw byte[] message
     */
    public JSONObject getCurSensData(){
        commTechLogger.info("CommTech - in getCurSensData");

        JSONObject curMsg = null;
        String curdata = null;
        byte[] curDataFromDataQueue = null;
        
            //this is  for fake sensor test
            if (protocolId.equalsIgnoreCase("serial0")){
                curDataFromDataQueue = curUDPCommDataQueue.getData();
                if ( curDataFromDataQueue != null ){
                    SerialProtocol curProtocol = new SerialProtocol(0);
                    curMsg = curProtocol.parseMessage(curDataFromDataQueue);
                    commTechLogger.info("serial0 curMsg: " + curMsg);
                }
            }
        
            //this is a real serial temp sensor
            if (protocolId.equalsIgnoreCase("serial1")){
                curDataFromDataQueue = curSerialCommDataQueue.getData();
                if ( curDataFromDataQueue != null ){
                    SerialProtocol curProtocol = new SerialProtocol(1);
                    curMsg = curProtocol.parseMessage(curDataFromDataQueue);
                    commTechLogger.info("serial1 curMsg: " + curMsg);
                }
            }
        
            //this is a webcam switch
            if (protocolId.equalsIgnoreCase("webcamswitch")){
               curDataFromDataQueue = curWebCamClientCommDataQueue.getData();
                if ( curDataFromDataQueue != null ){
                   if (curDataFromDataQueue[3] == 0){
                           curdata = "0";
                       }else if (curDataFromDataQueue[3] == 1){
                           curdata = "1";
                       }

                        String msgRaw = new String("{"+
                                                   "'value':'" + curdata +
                                                   "'}");
                    try {
                        curMsg = new JSONObject(msgRaw);
                        //commTechLogger.info("webcamswitch curMsg: " + curMsg);
                    } catch (JSONException ex) {
                        commTechLogger.error(ex);
                    }
                 commTechLogger.info("webcamswitch curMsg: " + curMsg);
                }
            }

           // for znet sensor
           if (protocolId.equalsIgnoreCase("znet")){
               curDataFromDataQueue = curZNetCommDataQueue.getData();
               String curdata0 = null;
               byte[] curDataIn1 = new byte[curDataFromDataQueue.length];
               int i1=0;
               //filter for json objects I have to remove 0s from bytes
               if(curDataFromDataQueue[0] != -1){
                    for (int i0 = 0; i0 < curDataFromDataQueue.length;i0++){
                        if ( curDataFromDataQueue[i0] == 13 || curDataFromDataQueue[i0] == 10 ){
                            curDataFromDataQueue[i0]=0;
                        }
                        if (curDataFromDataQueue[i0]!=0){
                           curDataIn1[i1]=curDataFromDataQueue[i0];
                           i1++;
                        }
                    }
                    i1=i1-2;
                    byte[] curDataIn2 = new byte[i1];
                    for (int i2=0;i2<i1;i2++){
                        curDataIn2[i2]=curDataIn1[i2];
                    }
                    try {
                        curdata0 = new String(curDataIn1, 0, curDataIn2.length, "UTF-8");
                      } catch (UnsupportedEncodingException ex) {
                           commTechLogger.error(ex);
                      }
                      String msgRaw = new String( "{"+
                             "'value':'" + curdata0 +
                             "'}");

                    commTechLogger.info( "znet curMsg: " + msgRaw );
                    //msgRaw = "{'value':'45:2:3pippo=2.34'}";
                    try {
                        curMsg = new JSONObject(msgRaw);

                    } catch (JSONException ex) {
                        commTechLogger.error(ex);
                    }
                 commTechLogger.info("znet curMsg: " + curMsg);
            }
           }
       
        return curMsg;
    }
    
    /**
     * set json object data to send
     * @param val curDataOut
     */
    public void setCurDataOut (int val) {
        commTechLogger.info("CommTech - in setCurDataOut");
        curDataOut = val;
        
    }
    
    /**
     * retrieve polling time settings for an endpoint
     * @return timing
     */
    public int getTiming() {
        //TODO:vedere la gestione del timing se metterla qui
        return timing;
    }

    /**
     * set polling time for an endpoint
     * @param val timing
     */
    public void setTiming (String val) {
       timing = Integer.parseInt(val);
    }

     /**
     * get protocolid
     * @return
     */
    public String getProtocolId() {
        return protocolId;
    }

    /**
     * set protocol id
     * @param val
     */
    public void setProtocolId(String val) {
        protocolId = val;
    }
    
    /**
     * starts data collecting thread
     */
    public void startDataCollecting(){
        this.runningDataCollectorThread = true;
        thread_dataCollector.start();
    }
    /**
     * stops data collecting thread
     */
    public void stopDataCollecting(){
        this.runningDataCollectorThread = false;
    }
    /**
     * starts exe command thread
     */
    public void startDataCommander(){
        this.runningDataCommanderThread = true;
        thread_dataCommander.start();
    }
    /**
     * stops exe command thread
     */
    public void stopDataCommander(){
        this.runningDataCommanderThread = false;
    }
   


}


