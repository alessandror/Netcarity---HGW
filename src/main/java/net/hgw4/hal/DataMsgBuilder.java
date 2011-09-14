package net.hgw4.hal;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

/**
 * build JSON response messages
 * @author AlessandroR
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
     * @return
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
            //TODO: ricordati di pulire le altre classi per  gli altri valori json non necessari, mi server solo value
//nc message-------------------------------
   //header--------------------------------
            //genero il message ID
            String msgMsgId = getMsgId();
            //genero il timestamp
            String msgTime = getTime();
            //msg type
            String msgType = "response";
            //msg source
            String msgSource = "hgw3";//TODO: da leggere fa oggetto json
            //msg target
            String msgTarget = "NCCC0";//TODO: da mettere in file di config
            //genero la priorità
            String msgPriority = getPriority();
            // msg version
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
