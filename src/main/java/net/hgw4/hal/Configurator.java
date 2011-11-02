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
 * 
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

        //read file
        configuratorLogger.info("Configurator - read config file");
        curJsonConfig = new JSONObject(readConfig(configFile));
        
        //read nodes
        JSONObject curJsonConfig1 = new JSONObject(curJsonConfig.getString("hgw3"));
        
        //read node configurations
        JSONArray curNodeConfig = new JSONArray(curJsonConfig1.getString("nodes"));
          
        //create nodes
        configuratorLogger.info("Configurator - create nodes");
       
        for (int i0=0;i0<curNodeConfig.length();i0++){

            JSONObject nodeConfig1 = new JSONObject(curNodeConfig.getString(i0));

            Node node = new Node();

            node.setNodeId(nodeConfig1.getString("nodeid"));
            node.setNodeDescription(nodeConfig1.getString("nodedescription"));
            node.setNodeType(nodeConfig1.getString("nodetype"));
            
            JSONArray curDataEndPointList = new JSONArray(nodeConfig1.getString("dataendpoint"));
            for (int i1=0;i1<curDataEndPointList.length();i1++){
                JSONObject curDataEndPointConfig = new JSONObject(curDataEndPointList.getString(i1));
                 
                JSONObject curJsonCommProtocolConfig = new JSONObject(curJsonConfig.getString(curDataEndPointConfig.getString("commtechid")));
               
                CommTech curCommTech = new CommTech(curJsonCommProtocolConfig);
                
                if (node.getNodeType().compareToIgnoreCase("sensor") >=0) {
                    curCommTech.setTiming(curDataEndPointConfig.getString("timing"));
                }
                curCommTech.setProtocolId(curDataEndPointConfig.getString("commprotocol"));
                
                DataEndPoint curDataEndPoint = new DataEndPoint(curCommTech);
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
                node.setEndPointList(curDataEndPoint);
            
            }
            nodeList.add(node);
        }
    }
    
    
    /**
     * read configuration file   
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
     * get hal ocnfiguration
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
     * get cur node configuration
     * @return json object with entire current config
     */
    public JSONObject getCurNodeConfig(){
        return curJsonConfig;
    }
}

      
    
    

