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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

/**
 * build JSON response messages
 * 
 */
public class DataMsgBuilder {
private JSONObject curJsonValue = null;
private JSONObject jsonResponseMsg = null;
private Date now = null;
private Node curNode = null;
private DataEndPoint curEndPoint = null;
private MessageIDManager curMessageIdManager = null;
private String curNodeID = null;
private String curEndPointID = null;
private  String msgDataValue = null;
private Logger DataMsgBuilderLogger;

    DataMsgBuilder(JSONObject rcvSensValue,
                   MessageIDManager rcvMessageIdManager,
                   Node rcvNode,
                   DataEndPoint rcvEndPoint,
                   String rcvNodeID,
                   String rcvEndPointID) {
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        DataMsgBuilderLogger = Logger.getLogger(DataMsgBuilder.class);
        DataMsgBuilderLogger.info("--> DataMsgBuilderLogger <--");


        curJsonValue = rcvSensValue;
        curMessageIdManager = rcvMessageIdManager;
        curNode = rcvNode;
        curEndPoint = rcvEndPoint;
        curEndPointID = rcvEndPointID;
        curNodeID = rcvNodeID;

        msgBuilder(curJsonValue);
        
    }
    
    /**
     * return the populated json object
     * @return msgBuilder(curJsonValue)
     */
    public JSONObject getData() {
        return msgBuilder(curJsonValue);
    }

    /**
     * parse json object and populate new one with other data
     * @param val
     */
    private JSONObject msgBuilder(JSONObject val) {
        try {
            
//nc message-------------------------------
   //header--------------------------------
            //create message ID
            String msgMsgId = getMsgId();
            //create timestamp
            String msgTime = getTime();
            //set msg type
            String msgType = "response";
            //set msg source
            String msgSource = "hgw3";//TODO: da leggere fa oggetto json
            //set msg target
            String msgTarget = "NCCC0";//TODO: da mettere in file di config
            //set priority
            String msgPriority = getPriority();
            // set msg version
            String msgVersion ="1.1";//TODO: da mettere in file di config
   //header--------------------------------
   //node,endpoint-------------------------
            // node id
            String msgNodeID = curNodeID;
            // node description
            String msgNodeDescription = curNode.getNodeDescription();
            // node type
            String msgNodeType = curNode.getNodeType();
            // endpoint id
            String msgEndPointID = curEndPoint.getEndPointId();
            // endpoint description
            String msgEndPointDescription = curEndPoint.getEndPointDescr();
            // endpoint datarange min
            String msgEndPointDataRangeMin = curEndPoint.getEndPointDataRangeMin();
            // endpoint datarange max
            String msgEndPointDataRangeMax = curEndPoint.getEndPointDataRangeMax();
            // endpoint position
            String msgEndPointPosition = curEndPoint.getEndPointPosition();
            // endpoint coordinates
            String msgEndPointCoordinates = curEndPoint.getEndPointCoordinates();
            // endpoint value
            if (val != null){
                msgDataValue = new String(val.getString("value"));
            }else{
                msgDataValue = "no-data";
            }
   //node,endpoint-------------------------
//nc message-------------------------------
            //Json msg
            String strResponseMsg = "{" +
                    "'msgid':'"               + msgMsgId                + "'," +
                    "'timestamp':'"           + msgTime                 + "'," +
                    "'type':'"                + msgType                 + "'," +
                    "'source':'"              + msgSource               + "'," +
                    "'target':'"              + msgTarget               + "'," +
                    "'priority':'"            + msgPriority             + "'," +
                    "'msgVersion':'"          + msgVersion              + "'," +
                    "'nodeid':'"              + msgNodeID               + "'," +
                    "'nodedescription':'"     + msgNodeDescription      + "'," +
                    "'nodetype':'"            + msgNodeType             + "'," +
                    "'endpointid':'"          + msgEndPointID           + "'," +
                    "'endpointdescription':'" + msgEndPointDescription  + "'," +
                    "'data':'"                + msgDataValue            + "'," +
                    "'datarange_min':'"       + msgEndPointDataRangeMin + "'," +
                    "'datarange_max':'"       + msgEndPointDataRangeMax + "'," +
                    "'coordinates':'"         + msgEndPointCoordinates  + "'"  +
                    "}";

             jsonResponseMsg = new JSONObject(strResponseMsg);

        } catch (JSONException ex) {
            DataMsgBuilderLogger.error(ex);
        }

        return jsonResponseMsg;
        
    }

    /**
     * generate time
     * @return
     */
    private String getTime(){
        now = new Date();
        return now.toString();
    }

    /**
     * generate message id
     * @return
     */
    private String getMsgId(){
        int val = curMessageIdManager.getSensorMsgId();
        return Integer.toString(val);
    }
    
    /**
     * set priority for message
     * @return
     */
    private String getPriority(){
        return curEndPoint.getEndPointPriority();
    }
}
