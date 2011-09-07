package HAL;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.json.JSONObject;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * manages the in home hardware, configuration, data collection and command execution
 * @author AlessandroR
 */

public final class Hal {

    private Configurator curConfig;
    private Node curNode;
    private DataEndPoint curEndPoint;
    private Logger halLogger;
    public JSONObject curSensValue = null;
    public JSONObject curNodesConfig = null;
    public MessageIDManager curMessageIdManager = null;
    public EventHandler curEventHandler = null;
    
    /**
     * call Configurator and set curConfig
     */
    public Hal () {
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                                         System.getProperty("file.separator") +
                                                         "configs"+ System.getProperty("file.separator") +"log4j.properties");
        halLogger = Logger.getLogger(Hal.class);
        halLogger.info("--> Hal <--");

        // initialize message id manager
        curMessageIdManager = new MessageIDManager();

        // 1 checks if ther is the overall config file  [exepath]/halConfigs/generalConfigs.properties
        Properties defaultProps = new Properties();
        try {
            FileInputStream in = new FileInputStream(System.getProperty("user.dir") +
                                                     System.getProperty("file.separator") +
                                                     "halConfigs" + System.getProperty("file.separator") +
                                                     "generalConfigs.properties");
            try {
                defaultProps.load(in);
                String curConfigFileName = defaultProps.getProperty("configFilePath");
                if (curConfigFileName.equalsIgnoreCase( "halConfig" )){
                    halLogger.info("cur config file path: " + System.getProperty("user.dir") +
                                            System.getProperty("file.separator") + "halConfigs" +
                                            System.getProperty("file.separator") +  curConfigFileName);

                    // 2 Build system from json file
                    curConfig = new Configurator(System.getProperty("user.dir") +
                                                 System.getProperty("file.separator") +"halConfigs"+
                                                 System.getProperty("file.separator") +  curConfigFileName);
                    
                in.close();
                }else{
                    halLogger.info("Abort - wrong config file name");
                    halLogger.info("cur config file path: "+ System.getProperty("user.dir")  +
                                            System.getProperty("file.separator") +"halConfigs"+
                                            System.getProperty("file.separator") +  curConfigFileName);
                    System.exit(1);
                }
            } catch (IOException ex) {
                halLogger.error(ex);
            }
       } catch (FileNotFoundException ex) {
                halLogger.info("Abort - no config file");
                System.exit(1);
        }
       
   }

    /**
     * Starts internal event handler
     */
    public void startInternalEventHandler(){
        halLogger.info("Hal - startInternalEventHandler ");
        // initialize event handler system
        curEventHandler = new EventHandler();
        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           //iter over endpoints
           for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                curEndPoint = curNode.dataEndPointsList.get(i1);
                curEndPoint.curCommTech.setInternalEventHandler(curEventHandler);
           }
        }
    }
    
    /**
     * starts external event handler
     * @param extObservable 
     */
    public void startExternalEventHandler(Object extObservable){
         halLogger.info("Hal - startExternalEventHandler ");
        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           //iter over endpoints
           for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                curEndPoint = curNode.dataEndPointsList.get(i1);
                curEndPoint.curCommTech.setExternalEventHandler(extObservable);
           }
        }
    }

    /**
     * Starts all subsystem threads for data collecting
     */
    public void startAllDataCollecting(){
        halLogger.info("Hal - startAllDataCollecting ");
        
        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           //iter over endpoints
           for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                curEndPoint = curNode.dataEndPointsList.get(i1);
                curEndPoint.curCommTech.startDataCollecting();
           }
        }
    }

    /**
     * Stops all subsystem threads for data collecting
     */
    public void stopAllDataCollecting(){
        halLogger.info("Hal - stopAllDataCollecting ");

        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           //iter over endpoints
           for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                curEndPoint = curNode.dataEndPointsList.get(i1);
                curEndPoint.curCommTech.stopDataCollecting();
           }
        }
    }

    /**
     * starts all thread for managing commands
     */
    public void startAllDataCommander(){
        halLogger.info("Hal - startAllDataCollecting ");
        
        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           //iter over endpoints
           for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                curEndPoint = curNode.dataEndPointsList.get(i1);
                curEndPoint.curCommTech.setCurDataOut(-1); //reset commands
                curEndPoint.curCommTech.startDataCommander();
           }
        }
    }

    /**
     * stops all thread for managing commands
     */
    public void stopAllDataCommander(){
        halLogger.info("Hal - stopAllDataCollecting ");
        
        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           //iter over endpoints
           for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                curEndPoint = curNode.dataEndPointsList.get(i1);
                curEndPoint.curCommTech.stopDataCommander();
           }
        }
    }



    /**
     *  
     * @param nodeId to poll
     * @param endPointId to poll
     * @return json obj for sensor data description 
     */
    //call back for all sensor data
    public JSONObject pollSensor (String nodeId, String endPointId) {
         halLogger.info("Hal - pollSensors ");
 
        //iter over Nodes hold in Configurator instance
        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           if ( curNode.getNodeId().equalsIgnoreCase(nodeId) ){
               //iter over endpoints
               for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                    curEndPoint = curNode.dataEndPointsList.get(i1);
                    if (curEndPoint.getEndPointId().equalsIgnoreCase(endPointId)){

                    curSensValue = curEndPoint.curCommTech.getCurSensData();
                    
                    //populate msg with other static info
                    DataMsgBuilder curDataMsgBuilder = new DataMsgBuilder(curSensValue,
                                                                          curMessageIdManager,
                                                                          curNode,
                                                                          curEndPoint,
                                                                          nodeId,
                                                                          endPointId);

                    return curDataMsgBuilder.getData();
                        
                    }
               }
           }
        }
        return null;
    }

    /** exe a command for an actuator
     * 
     * @param nodeId to send to the command
     * @param endPointId to send to the command
     * @param cmdValue 
     * @return status of the command
     */
    public JSONObject exeCmd (String nodeId, 
                              String endPointId,
                              int cmdValue) {
        halLogger.info("Hal - exeCmd ");

        JSONObject curCmdStatus = null;
        int timeoutCount = 0;
        //iter over Nodes hold in Configurator instance
        for (int i0=0;i0<curConfig.nodeList.size();i0++){
           curNode = curConfig.nodeList.get(i0);
           if (curNode.getNodeId().equalsIgnoreCase(nodeId) ){
               //iter over endpoints
               for (int i1=0;i1<curNode.dataEndPointsList.size();i1++){
                    curEndPoint = curNode.dataEndPointsList.get(i1);
                    if (curEndPoint.getEndPointId().equalsIgnoreCase(endPointId)){

                         //set commando to exe in comm tech
                         curEndPoint.curCommTech.setCurDataOut(cmdValue);

                         //wait for response
                         while (curEndPoint.curCommTech.getCurCommandStatus() == null){
                             
                             if (timeoutCount > 10000000){ //10
                                  curCmdStatus = null;
                                  timeoutCount = 0;
                                  halLogger.info("--> hal  curCmdStatus = null " + timeoutCount );
                                  break;
                             }else{
                                  timeoutCount++;
                             }
                             //System.out.println("--> hal while execmd <-- ");
                         }

                         //build response message
                         curCmdStatus = curEndPoint.curCommTech.getCurCommandStatus();
                         halLogger.info("--> hal exe cmd cmdstatus: " + curCmdStatus);
                         DataMsgBuilder curDataMsgBuilder = new DataMsgBuilder(curCmdStatus,
                                                                               curMessageIdManager,
                                                                               curNode,
                                                                               curEndPoint,
                                                                               nodeId,
                                                                               endPointId);
                         halLogger.info("--> hal exe returned cmd cmdstatus: " + curCmdStatus);
                         return curCmdStatus;
                         //return curDataMsgBuilder.getData();
                         
                    }
               }
           }
        }
        return null;

    }

  
    /**
     * get node list
     * @return all nodes' description
     */
    public JSONObject getNodeList () {
        halLogger.info("Hal - getNodeList ");
        
        return curConfig.getCurNodeConfig();
    }

    public EventHandler setEventHandlerRef(){
         halLogger.info("Hal - getEventHandlerRef ");

         return curEventHandler;
    }

}

