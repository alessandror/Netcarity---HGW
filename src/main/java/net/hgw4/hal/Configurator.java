package net.hgw4.hal;
import java.io.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.*;

/**
 * parse a configuration file and creates all the objects:
 * Nodes
 * Commtech
 * DataendPoints for the hgw
 * @author AlessandroR
 */
public class Configurator {
    private String curDir;
    private BufferedReader inData;
    public ArrayList<Node> nodeList;
    public JSONObject curJsonConfig;
    private Logger configuratorLogger;
     
    /**
     * read config file
     * @param configFile
     */
    public Configurator(String configFile) {
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        configuratorLogger = Logger.getLogger(Configurator.class);
        configuratorLogger.info("--> Configurator <--");
        
        try {
            //costruisce il sottosistema
             createNode(configFile);
     
        } catch (JSONException ex) {
            configuratorLogger.error(ex);
        }
    }
    
    /**
     * create a node from json config file description
     * 
     * @param configFile
     * @throws org.json.JSONException
     */
    //per gestire più endpoint
    public void createNode(String configFile) throws JSONException {

        nodeList = new ArrayList();

        configuratorLogger.info("Configurator - createNode");

        //leggo il file intero
        configuratorLogger.info("Configurator - read config file");
        curJsonConfig = new JSONObject(readConfig(configFile));
        //test per conversione XML
        /*
        XML myXml = new XML();
        String jsonToXml = new String("");
        jsonToXml = myXml.toString(curJsonConfig);
        */
        //System.out.println(curJsonConfig.getString("hgw3"));
        
        //leggo il vocabolario dei nodes
        JSONObject curJsonConfig1 = new JSONObject(curJsonConfig.getString("hgw3"));
        
        //leggo array di configurazione dei nodes
        JSONArray curNodeConfig = new JSONArray(curJsonConfig1.getString("nodes"));
          
        //creo i nodi
        configuratorLogger.info("Configurator - create nodes");
       
        for (int i0=0;i0<curNodeConfig.length();i0++){
            //System.out.println(curNodeConfig.getString(i0));
            //prendo la config i-esima 
            JSONObject nodeConfig1 = new JSONObject(curNodeConfig.getString(i0));
            //creo il nodo
            Node node = new Node();
            //leggo la config del nodo
            node.setNodeId(nodeConfig1.getString("nodeid"));
            node.setNodeDescription(nodeConfig1.getString("nodedescription"));
            node.setNodeType(nodeConfig1.getString("nodetype"));
            
            //System.out.println(nodeConfig1.getString("dataendpoint"));
            //creo end point logico associato al fisico
            JSONArray curDataEndPointList = new JSONArray(nodeConfig1.getString("dataendpoint"));
            for (int i1=0;i1<curDataEndPointList.length();i1++){
                JSONObject curDataEndPointConfig = new JSONObject(curDataEndPointList.getString(i1));
                 
                //passo la configurazione della tecnologia di comunicazione(ip,bluetooth,etc) a CommTech
                JSONObject curJsonCommProtocolConfig = new JSONObject(curJsonConfig.getString(curDataEndPointConfig.getString("commtechid")));
               
                //creo la comm tech
                CommTech curCommTech = new CommTech(curJsonCommProtocolConfig);
                
                //leggo la commtech config
                if (node.getNodeType().compareToIgnoreCase("sensor") >=0) {
                    curCommTech.setTiming(curDataEndPointConfig.getString("timing"));
                }
                curCommTech.setProtocolId(curDataEndPointConfig.getString("commprotocol"));
                
                //creo il data end point
                DataEndPoint curDataEndPoint = new DataEndPoint(curCommTech);
                //leggo il data end point config
                curDataEndPoint.setEndPointDescr(curDataEndPointConfig.getString("endpointdescription"));
                curDataEndPoint.setEndPointId(curDataEndPointConfig.getString("endpointid"));
                curDataEndPoint.setEndPointCommTechId(curDataEndPointConfig.getString("endpointid"));
                curDataEndPoint.setEndPointPosition(curDataEndPointConfig.getString("position"));
                curDataEndPoint.setEndPointCoordinates(curDataEndPointConfig.getString("coordinates"));
                if (node.getNodeType().compareToIgnoreCase("sensor") >=0) {
                    curDataEndPoint.setEndPointDataRangeMin(curDataEndPointConfig.getString("data range min"));
                    curDataEndPoint.setEndPointDataRangeMax(curDataEndPointConfig.getString("data range max"));
                    curDataEndPoint.setEndPointPriority(curDataEndPointConfig.getString("priority"));
                }
                //carico endpoints per il nodo 
                node.setEndPointList(curDataEndPoint);
            
            }
            //carico la lista dei nodi
            nodeList.add(node);
           // System.out.println("temp");
        }
    }
    
    
    /**
     *      
     * @param filePath 
     * @return string from file description
     */
    public String readConfig(String filePath){
        
        String curReadConfig = new String();
        File curFile = new File(filePath);

        try {
            inData = new BufferedReader(new FileReader(curFile));
        } catch (FileNotFoundException ex) {
            configuratorLogger.error(ex);
        }
        try {
            while (inData.ready()) {
                String readString = inData.readLine();
                curReadConfig = curReadConfig + readString;
            }
        } catch (IOException ex) {
            configuratorLogger.error(ex);
        }
        return curReadConfig;
    
    }
    
    /**
     *
     */
    public void writeConfig(){
        
    }
    /**
     * 
     * @return json obj from file description
     */
    public JSONObject getHalConfig(){
        if (this.curJsonConfig != null){
            return this.curJsonConfig;
        }else{
            return null;
        }
    }
    /**
     * 
     * @return json object with entire current config
     */
    public JSONObject getCurNodeConfig(){
        return curJsonConfig;
    }
}

      
    
    

